package com.bitozen.hms.rabbit.consumer.termination;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.dto.RabbitFileDTO;
import com.bitozen.hms.common.status.ResponseStatus;
import com.bitozen.hms.common.type.ProjectType;
import com.bitozen.hms.common.util.LogOpsUtil;
import com.bitozen.hms.pm.command.termination.ProlongedIllnessRegistryCreateCommand;
import com.bitozen.hms.pm.common.ProlongedIllnessStatus;
import com.bitozen.hms.pm.common.dto.command.termination.ProlongedIllnessRegistryCreateCommandDTO;
import com.bitozen.hms.pm.repository.termination.ProlongedIllnessRegistryRepository;
import com.bitozen.hms.projection.termination.ProlongedIllnessRegistryEntryProjection;
import com.bitozen.hms.web.helper.BizparHelper;
import com.bitozen.hms.web.helper.EmployeeHelper;
import com.bitozen.hms.web.helper.PMAssembler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.Base64;
import com.jlefebure.spring.boot.minio.MinioException;
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
 * @author Dumayangsari
 */
@Service
@Slf4j
public class ProlongedIllnessRegistryRabbitConsumer {
    
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProlongedIllnessRegistryRepository repository;

    @Autowired
    private MinioService minioService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    BizparHelper bizparHelper;

    @Autowired
    EmployeeHelper employeeHelper;

    @Autowired
    PMAssembler pmAssembler;

    private final CommandGateway commandGateway;

    @Autowired
    public ProlongedIllnessRegistryRabbitConsumer(CommandGateway commandGateway){
        this.commandGateway = commandGateway;
    }

    /**
     * upload file to minio as consumer rabbitmq
     *
     * @param dataParam
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "q.prolonged-illness-registry-upload-image", durable = "true"),
            exchange = @Exchange(name = "x-hms-pm", type = ExchangeTypes.TOPIC, durable = "true"),
            key = "prolonged-illness-registry-upload-image",
            ignoreDeclarationExceptions = "true"), concurrency = "1")
    public void saveUploadedFileToMinioConsumer(RabbitFileDTO dataParam) {
        try {
            byte[] decodedBytes = Base64.decodeBase64(dataParam.getImageFile());
            InputStream dataDocument = new ByteArrayInputStream(decodedBytes);

            Path path = Paths.get("PROLONGED_DOC_" + dataParam.getId() + "_" + dataParam.getOriginalFileName());
            Map<String, String> header = new HashMap<>();
            header.put("ProlongedIllnessRegistry", dataParam.getId());
            Optional<ProlongedIllnessRegistryEntryProjection> pi = repository.findOneByPiIDAndPiStatus(dataParam.getId(), ProlongedIllnessStatus.ACTIVE);

            pi.get().setDocURL(path.toString());

            pi.get().getCreational().setModifiedBy(dataParam.getUpdatedBy());
            pi.get().getCreational().setModifiedDate(new Date());

            minioService.upload(path, dataDocument, dataParam.getContentType(), header);
            repository.save(pi.get());
            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CQRS, "Prolonged Illness Registry Upload Document", new Date(), "UPLOAD", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
        } catch (JsonProcessingException | MinioException ex) {
            try {
                log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CQRS, "Prolonged Illness Registry Upload Document", new Date(), "UPLOAD", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex.getStackTrace())));
            } catch (JsonProcessingException jpex) {
                log.info(jpex.getMessage());
            }
        }
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "q.prolonged-illness-registry-migration", durable = "true"),
            exchange = @Exchange(name = "x-hms-pm", type = ExchangeTypes.TOPIC, durable = "true"),
            key = "prolonged-illness-registry-migration",
            ignoreDeclarationExceptions = "true"), concurrency = "1")
    public void postTerminationMigrationRequest(ProlongedIllnessRegistryCreateCommandDTO dto) {
        log.info("Start ===>>>>", dto);
        try {
            ProlongedIllnessRegistryCreateCommand command = new ProlongedIllnessRegistryCreateCommand(
                    dto.getPiID(),
                    dto.getStartDate(),
                    dto.getEndDate(),
                    dto.getDocURL(),
                    objectMapper.writeValueAsString(employeeHelper.findEmployeeOptimizeByKey(dto.getEmployee())),
                    objectMapper.writeValueAsString(employeeHelper.findEmployeeOptimizeByKey(dto.getRequestor())),
                    objectMapper.writeValueAsString(bizparHelper.convertBizpar(dto.getIlnessType())),
                    dto.getPiStatus(),
                    dto.getReason(),
                    objectMapper.writeValueAsString(pmAssembler.toMetadata(dto.getMetadata())),
                    objectMapper.writeValueAsString(dto.getToken()),
                    dto.getRecordID(),
                    dto.getCreatedBy(),
                    dto.getCreatedDate()
            );
            commandGateway.send(command, new CommandCallback<ProlongedIllnessRegistryCreateCommand, Object>() {
                @Override
                public void onResult(CommandMessage<? extends ProlongedIllnessRegistryCreateCommand> commandMessage, CommandResultMessage<?> commandResultMessage) {
                    try {
                        log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                                ProjectType.CQRS, "Prolonged Illness Registry", new Date(), "Command", new GenericResponseDTO().successResponse().getCode(),
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
