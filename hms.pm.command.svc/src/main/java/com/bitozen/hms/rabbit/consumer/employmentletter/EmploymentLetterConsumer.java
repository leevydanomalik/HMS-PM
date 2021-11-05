package com.bitozen.hms.rabbit.consumer.employmentletter;

import com.bitozen.hms.common.dto.CreationalSpecificationDTO;
import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.dto.RabbitFileDTO;
import com.bitozen.hms.common.status.ResponseStatus;
import com.bitozen.hms.common.type.ProjectType;
import com.bitozen.hms.common.util.LogOpsUtil;
import com.bitozen.hms.pm.command.employmentletter.EmploymentLetterCreateCommand;
import com.bitozen.hms.pm.common.EmploymentLetterStatus;
import com.bitozen.hms.pm.common.dto.command.employmentletter.EmploymentLetterCreateCommandDTO;
import com.bitozen.hms.pm.common.dto.command.movement.MovementCreateCommandDTO;
import com.bitozen.hms.pm.repository.employmentletter.EmploymentLetterRepository;
import com.bitozen.hms.projection.employmentletter.EmploymentLetterEntryProjection;
import com.bitozen.hms.projection.movement.MovementEntryProjection;
import com.bitozen.hms.web.helper.BizparHelper;
import com.bitozen.hms.web.helper.EmployeeHelper;
import com.bitozen.hms.web.helper.EmploymentLetterHelper;
import com.bitozen.hms.web.helper.PMAssembler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.Base64;
import com.jlefebure.spring.boot.minio.MinioService;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jeremia
 */
@Service
@Slf4j
public class EmploymentLetterConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmploymentLetterRepository repository;

    @Autowired
    private MinioService minioService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    BizparHelper bizparHelper;

    @Autowired
    private EmploymentLetterHelper employmentLetterHelper;

    @Autowired
    EmployeeHelper employeeHelper;

    @Autowired
    PMAssembler pmAssembler;

    private final CommandGateway commandGateway;

    @Autowired
    public EmploymentLetterConsumer(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "q.employmentletter-upload-document", durable = "true"),
            exchange = @Exchange(name = "x-hms-pm", type = ExchangeTypes.TOPIC, durable = "true"),
            key = "employmentletter-upload-document",
            ignoreDeclarationExceptions = "true"), concurrency = "1")
    public void uploadImageConsumer(RabbitFileDTO dataParam) {
        try {
            byte[] decodedBytes = Base64.decodeBase64(dataParam.getImageFile());
            InputStream dataImage = new ByteArrayInputStream(decodedBytes);

            Path path = Paths.get("EMPLOYMENT_DOCUMENT_" + dataParam.getId() + "-" + dataParam.getOriginalFileName());
            Map<String, String> header = new HashMap<>();
            header.put("employmentletter", dataParam.getId());
            Optional<EmploymentLetterEntryProjection> employmentLetter = repository.findOneByElIDAndElStatusNot(dataParam.getId(), EmploymentLetterStatus.INACTIVE);
            employmentLetter.get().setElDocURL(path.toString());

            minioService.upload(path, dataImage, dataParam.getContentType(), header);
            repository.save(employmentLetter.get());
            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CRUD, " EmploymentLetter Upload Document", new Date(), "UPLOAD", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
        } catch (Exception ex) {
            try {
                log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CRUD, " EmploymentLetter Upload Document", new Date(), "UPLOAD", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex.getStackTrace())));
            } catch (JsonProcessingException jpex) {
                log.info(jpex.getMessage());
            }
        }
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "q.employmentletter-migration", durable = "true"),
            exchange = @Exchange(name = "x-hms-pm", type = ExchangeTypes.TOPIC, durable = "true"),
            key = "employmentletter-migration",
            ignoreDeclarationExceptions = "true"), concurrency = "1")
    public void postEmploymentLetterMigrationRequest(EmploymentLetterCreateCommandDTO dto) {
        log.info("Start ===>>>>", dto);
        try {
            EmploymentLetterCreateCommand command = new EmploymentLetterCreateCommand(
                    dto.getElID(),
                    dto.getElDocNumber(),
                    dto.getElDocURL(),
                    dto.getElState(),
                    dto.getElStatus(),
                    dto.getIsFinalApproval(),
                    dto.getReason(),
                    dto.getReqDate(),
                    objectMapper.writeValueAsString(bizparHelper.convertBizpar(dto.getReqType())),
                    objectMapper.writeValueAsString(employeeHelper.findEmployeeOptimizeByKey(dto.getRequestor())),
                    objectMapper.writeValueAsString(employmentLetterHelper.toVisaSpecification(dto.getVisaSpec())),
                    objectMapper.writeValueAsString(pmAssembler.toMetadata(dto.getMetadata())),
                    objectMapper.writeValueAsString(dto.getToken()),
                    dto.getCreatedBy(),
                    dto.getCreatedDate(),
                    dto.getRecordID()
            );

            commandGateway.send(command, new CommandCallback<EmploymentLetterCreateCommand, Object>() {
                @Override
                public void onResult(CommandMessage<? extends EmploymentLetterCreateCommand> commandMessage, CommandResultMessage<?> commandResultMessage) {
                    try {
                        log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                                ProjectType.CQRS, "Employment Letter", new Date(), "Command", new GenericResponseDTO().successResponse().getCode(),
                                new GenericResponseDTO().successResponse().getMessage())));
                    } catch (JsonProcessingException ex) {
                        log.info(ex.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }
}
