package com.bitozen.hms.rabbit.consumer.blacklist;

import com.bitozen.hms.common.dto.*;
import com.bitozen.hms.common.type.ProjectType;
import com.bitozen.hms.common.util.LogOpsUtil;
import com.bitozen.hms.pm.common.BlacklistStatus;
import com.bitozen.hms.pm.common.dto.command.blacklist.BlacklistCreateCommandDTO;
import com.bitozen.hms.pm.repository.blacklist.BlacklistRepository;
import com.bitozen.hms.projection.blacklist.BlacklistEntryProjection;
import com.bitozen.hms.web.helper.PMAssembler;
import com.bitozen.hms.web.helper.BizparHelper;
import com.bitozen.hms.web.helper.EmployeeHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.Base64;
import com.jlefebure.spring.boot.minio.MinioException;
import com.jlefebure.spring.boot.minio.MinioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class BlacklistRabbitConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BlacklistRepository repository;

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

    /**
     * upload file to minio as consumer rabbitmq
     *
     * @param dataParam
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "q.blacklist-upload-image", durable = "true"),
            exchange = @Exchange(name = "x-hms-pm", type = ExchangeTypes.TOPIC, durable = "true"),
            key = "blacklist-upload-image",
            ignoreDeclarationExceptions = "true"), concurrency = "1")
    public void saveUploadedFileToMinioConsumer(RabbitFileDTO dataParam) {
        try {
            byte[] decodedBytes = Base64.decodeBase64(dataParam.getImageFile());
            InputStream dataDocument = new ByteArrayInputStream(decodedBytes);

            Path path = Paths.get("BLACKLIST_DOC_" + dataParam.getId() + "_" + dataParam.getOriginalFileName());
            Map<String, String> header = new HashMap<>();
            header.put("Blacklist", dataParam.getId());
            Optional<BlacklistEntryProjection> blacklist = repository.findOneByBlacklistIDAndBlacklistStatusNot(dataParam.getId(), BlacklistStatus.INACTIVE);

            blacklist.get().setBlacklistDocURL(path.toString());

            blacklist.get().getCreational().setModifiedBy(dataParam.getUpdatedBy());
            blacklist.get().getCreational().setModifiedDate(new Date());

            minioService.upload(path, dataDocument, dataParam.getContentType(), header);
            repository.save(blacklist.get());
            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CQRS, "Blacklist Upload Document", new Date(), "UPLOAD", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
        } catch (JsonProcessingException | MinioException ex) {
            try {
                log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CQRS, "Blacklist Upload Document", new Date(), "UPLOAD", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex.getStackTrace())));
            } catch (JsonProcessingException jpex) {
                log.info(jpex.getMessage());
            }
        }
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "q.blacklist-migration", durable = "true"),
            exchange = @Exchange(name = "x-hms-pm", type = ExchangeTypes.TOPIC, durable = "true"),
            key = "blacklist-migration",
            ignoreDeclarationExceptions = "true"), concurrency = "1")
    public void postBlacklistMigrationRequest(BlacklistCreateCommandDTO dto) {
        log.info("Start ===>>>>", dto);
        try {
            repository.save(new BlacklistEntryProjection(
                    repository.count() == 0 ? 1 : repository.findFirstByOrderByIdDesc().get().getId() + 1,
                    dto.getBlacklistID(),
                    dto.getBlacklistSPKNumber(),
                    dto.getBlacklistStartDate(),
                    dto.getBlacklistEndDate(),
                    dto.getBlacklistNotes(),
                    bizparHelper.convertBizpar(dto.getBlacklistType()),
                    dto.getIsPermanent(),
                    employeeHelper.findEmployeeOptimizeByKey(dto.getEmployee()),
                    employeeHelper.findEmployeeOptimizeByKey(dto.getRequestor()),
                    dto.getBlacklistDocURL(),
                    dto.getIsFinalApprove(),
                    dto.getBlacklistStatus(),
                    dto.getBlacklistState(),
                    dto.getCompRegulationChapter(),
                    dto.getCompRegulationChapterDesc(),
                    pmAssembler.toMetadata(dto.getMetadata()),
                    dto.getToken(),
                    new CreationalSpecificationDTO(dto.getCreatedBy(),
                            dto.getCreatedDate(),
                            null,
                            null),
                    dto.getRecordID()
            ));
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }
}
