package com.bitozen.hms.rabbit.consumer.termination;

import com.bitozen.hms.common.dto.CreationalSpecificationDTO;
import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.dto.RabbitFileDTO;
import com.bitozen.hms.common.status.ResponseStatus;
import com.bitozen.hms.common.type.ProjectType;
import com.bitozen.hms.common.util.LogOpsUtil;
import com.bitozen.hms.pm.command.termination.TerminationCreateCommand;
import com.bitozen.hms.pm.common.TerminationStatus;
import com.bitozen.hms.pm.common.dto.command.employmentletter.EmploymentLetterCreateCommandDTO;
import com.bitozen.hms.pm.common.dto.command.termination.TerminationCreateCommandDTO;
import com.bitozen.hms.pm.common.dto.query.termination.TerminationDocumentDTO;
import com.bitozen.hms.pm.repository.termination.TerminationRepository;
import com.bitozen.hms.projection.employmentletter.EmploymentLetterEntryProjection;
import com.bitozen.hms.projection.termination.TerminationEntryProjection;
import com.bitozen.hms.web.helper.BizparHelper;
import com.bitozen.hms.web.helper.EmployeeHelper;
import com.bitozen.hms.web.helper.PMAssembler;
import com.bitozen.hms.web.helper.TerminationHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.Base64;
import com.jlefebure.spring.boot.minio.MinioService;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TerminationConsumer {
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private TerminationRepository repository;
    
    @Autowired
    private MinioService minioService;

    @Autowired
    BizparHelper bizparHelper;

    @Autowired
    EmployeeHelper employeeHelper;

    @Autowired
    PMAssembler pmAssembler;

    @Autowired
    TerminationHelper terminationHelper;

    private final CommandGateway commandGateway;

    @Autowired
    public TerminationConsumer(CommandGateway commandGateway){
        this.commandGateway = commandGateway;
    }
    
      @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "q.termination-upload-document", durable = "true"),
            exchange = @Exchange(name = "x-hms-pm", type = ExchangeTypes.TOPIC, durable = "true"),
            key = "termination-upload-document",
            ignoreDeclarationExceptions = "true"), concurrency = "1")
    public void uploadDocumentConsumer(RabbitFileDTO dataParam) {
        try {
            byte[] decodedBytes = Base64.decodeBase64(dataParam.getImageFile());
            InputStream dataImage = new ByteArrayInputStream(decodedBytes);
            String[] parts = dataParam.getId().split(Pattern.quote("*"));
            String terminationID = parts[0];
            String docID = parts[1];
            Path path = Paths.get(terminationID + "-" + docID + "-" + dataParam.getOriginalFileName());
            
            Optional<TerminationEntryProjection> termination = repository.findOneByTmnIDAndTmnStatusNot(terminationID, TerminationStatus.INACTIVE);
            
            List<TerminationDocumentDTO> details = termination.get().getTmnDocs() == null ? new ArrayList<>() : termination.get().getTmnDocs();
           
            details.stream().forEach(o -> {
                if (o.getDocID().equalsIgnoreCase(docID)) {
                    o.setDocURL(path.toString());
                }
            });
            
            termination.get().setTmnDocs(details);
            termination.get().getCreational().setModifiedBy("SYSTEM");
            termination.get().getCreational().setModifiedDate(new Date());
            
            Map<String, String> header = new HashMap<>();
            header.put("termination", dataParam.getId());
            minioService.upload(path, dataImage, dataParam.getContentType(), header);
            
            repository.save(termination.get());
            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CRUD, "Termination Upload Document", new Date(), "UPLOAD", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
        } catch (Exception ex) {
            try {
                log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Termination Upload Document", new Date(), "UPLOAD", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex.getStackTrace())));
            } catch (JsonProcessingException jpex) {
                log.info(jpex.getMessage());
            }
        }
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "q.termination-migration", durable = "true"),
            exchange = @Exchange(name = "x-hms-pm", type = ExchangeTypes.TOPIC, durable = "true"),
            key = "termination-migration",
            ignoreDeclarationExceptions = "true"), concurrency = "1")
    public void postTerminationMigrationRequest(TerminationCreateCommandDTO dto) {
        log.info("Start ===>>>>", dto);
        try {
            TerminationCreateCommand command = new TerminationCreateCommand(
                    dto.getTmnID(),
                    dto.getTmnNotes(),
                    dto.getBpjsHTDocNumber(),
                    dto.getBpjsPensionDocNumber(),
                    objectMapper.writeValueAsString(dto.getDocCopies()),
                    dto.getIsCancelFinalApprove(),
                    dto.getIsExecuted(),
                    dto.getIsFinalApprove(),
                    dto.getMemoDocNumber(),
                    objectMapper.writeValueAsString(employeeHelper.findEmployeeOptimizeByKey(dto.getEmployee())),
                    objectMapper.writeValueAsString(employeeHelper.findEmployeeOptimizeByKey(dto.getRequestor())),
                    dto.getSkDocNumber(),
                    dto.getSkdtDocNumber(),
                    objectMapper.writeValueAsString(terminationHelper.toDocumentDTOs(dto.getTmnDocs())),
                    dto.getTmnEffectiveDate(),
                    dto.getTmnReqDate(),
                    dto.getTmnPphEndDate(),
                    objectMapper.writeValueAsString(bizparHelper.convertBizpar(dto.getTmnReason())),
                    dto.getTmnState(),
                    dto.getTmnStatus(),
                    objectMapper.writeValueAsString(pmAssembler.toMetadata(dto.getMetadata())),
                    objectMapper.writeValueAsString(dto.getToken()),
                    objectMapper.writeValueAsString(dto.getBagPensionSpec()),
                    objectMapper.writeValueAsString(terminationHelper.toBagProlongedIllness(dto.getBagProlongedIllnessSpec())),
                    dto.getCreatedBy(),
                    dto.getCreatedDate(),
                    dto.getRecordID()
            );
            commandGateway.send(command, new CommandCallback<TerminationCreateCommand, Object>() {
                @Override
                public void onResult(CommandMessage<? extends TerminationCreateCommand> commandMessage, CommandResultMessage<?> commandResultMessage) {
                        try {
                            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                                    ProjectType.CQRS, "Termination", new Date(), "Command", new GenericResponseDTO().successResponse().getCode(),
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
