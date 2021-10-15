package com.bitozen.hms.rabbit.consumer.movement;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.dto.RabbitFileDTO;
import com.bitozen.hms.common.dto.share.DocumentDTO;
import com.bitozen.hms.common.type.ProjectType;
import com.bitozen.hms.common.util.LogOpsUtil;
import com.bitozen.hms.pm.common.MVStatus;
import com.bitozen.hms.pm.repository.movement.MovementRepository;
import com.bitozen.hms.projection.movement.MovementEntryProjection;
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
import java.util.*;
import java.util.regex.Pattern;

@Service
@Slf4j
public class MovementRabbitConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MovementRepository repository;

    @Autowired
    private MinioService minioService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "q.movement-upload-image", durable = "true"),
            exchange = @Exchange(name = "x-hms-pm", type = ExchangeTypes.TOPIC, durable = "true"),
            key = "movement-upload-image",
            ignoreDeclarationExceptions = "true"), concurrency = "1")
    public void saveUploadedFileToMinioConsumer(RabbitFileDTO dataParam) {
        try {
            byte[] decodedBytes = Base64.decodeBase64(dataParam.getImageFile());
            InputStream dataDocument = new ByteArrayInputStream(decodedBytes);

            String[] parts = dataParam.getId().split(Pattern.quote("*"));
            String mvID = parts[0];
            String docID = parts[1];

            Path path = Paths.get("MOVEMENT_DOC_" + mvID + "_" + docID + "_" + dataParam.getOriginalFileName());
            Map<String, String> header = new HashMap<>();
            header.put("Movement", dataParam.getId());
            Optional<MovementEntryProjection> movement = repository.findOneByMvIDAndMvStatusNot(dataParam.getId(), MVStatus.INACTIVE);

            List<DocumentDTO> details = movement.get().getMvDocs()  == null ? new ArrayList<>() : movement.get().getMvDocs();

            details.stream().forEach(o -> {
                if (o.getDocID().equalsIgnoreCase(docID)) {
                    o.setDocURL(path.toString());
                }
            });

            movement.get().setMvDocs(details);
            movement.get().getCreational().setModifiedBy(dataParam.getUpdatedBy());
            movement.get().getCreational().setModifiedDate(new Date());

            minioService.upload(path, dataDocument, dataParam.getContentType(), header);
            repository.save(movement.get());
            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CQRS, "Movement Upload Document", new Date(), "UPLOAD", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
        } catch (JsonProcessingException | MinioException ex) {
            try {
                log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CQRS, "Movement Upload Document", new Date(), "UPLOAD", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex.getStackTrace())));
            } catch (JsonProcessingException jpex) {
                log.info(jpex.getMessage());
            }
        }
    }
}
