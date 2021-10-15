package com.bitozen.hms.web.service.minio.movement;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.dto.RabbitFileDTO;
import com.bitozen.hms.common.dto.share.DocumentDTO;
import com.bitozen.hms.common.type.ProjectType;
import com.bitozen.hms.common.util.LogOpsUtil;
import com.bitozen.hms.pm.common.MVStatus;
import com.bitozen.hms.pm.repository.movement.MovementRepository;
import com.bitozen.hms.projection.movement.MovementEntryProjection;
import com.bitozen.hms.rabbit.producer.movement.MovementRabbitProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.Base64;
import com.google.common.io.ByteStreams;
import com.jlefebure.spring.boot.minio.MinioException;
import com.jlefebure.spring.boot.minio.MinioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Slf4j
public class MovementMinioService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MovementRepository repository;

    @Autowired
    private MinioService minioService;

    @Autowired
    private MovementRabbitProducer producer;

    /**
     * Producer to rabbit MQ
     *
     * @param upload
     * @param mvID
     * @param updatedBy
     * @param updatedDate
     * @return
     */
    public GenericResponseDTO<String> convertAndSendToRabbit(MultipartFile upload, String mvID, String updatedBy, String updatedDate) {
        GenericResponseDTO<String> response = new GenericResponseDTO().successResponse();
        try {
            byte[] fileContent = ByteStreams.toByteArray(upload.getInputStream());
            String encodedString = Base64.encodeBase64String(fileContent);

            producer.movementDocumentUploadProducer(new RabbitFileDTO(upload.getOriginalFilename(), upload.getContentType(), encodedString, mvID, updatedBy, new SimpleDateFormat("dd-MM-yyyy").parse(updatedDate), null));

            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CQRS, "Movement Upload Document", new Date(), "UPLOAD", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
            response.setData(mvID);
            return response;
        } catch (Exception ex) {
            try {
                log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Movement Upload Document", new Date(), "upload", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex.getStackTrace())));
            } catch (JsonProcessingException jpex) {
                log.info(jpex.getMessage());
            }
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex.getLocalizedMessage());
        }
    }

    /**
     * GENERATE DOC BYTE MINIO
     *
     * @param objectID
     * @return
     */
    //GENERATE DOC BYTE
    public GenericResponseDTO<byte[]> generateMovementDocumentByte(String objectID) {
        try {
            /* Split id and detail id */
            String[] parts = objectID.split(Pattern.quote("*"));
            String mvID = parts[0];
            String docID = parts[1];

            Optional<MovementEntryProjection> movement = repository.findOneByMvIDAndMvStatusNot(mvID, MVStatus.INACTIVE);
            if (!movement.isPresent()) {
                log.info(objectMapper.writeValueAsString(LogOpsUtil.getErrorResponse(
                        ProjectType.CQRS, "Movement Get Document", new Date(), "GET DOCUMENT", new GenericResponseDTO().successResponse().getCode(),
                        new GenericResponseDTO().successResponse().getMessage())));
                return new GenericResponseDTO().noDataFoundResponse();
            }
            /* get data Detail */
            List<DocumentDTO> details = movement.get().getMvDocs() == null ? new ArrayList<>() : movement.get().getMvDocs();
            /* find detail by id */
            DocumentDTO detail = details.stream().filter(o -> o.getDocID().equalsIgnoreCase(docID)).findFirst().orElse(null);
            if (detail == null || detail.getDocURL() == null) {
                log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                        ProjectType.CRUD, "Termination Upload Document", new Date(), "GET DOCUMENT", new GenericResponseDTO().successResponse().getCode(),
                        new GenericResponseDTO().successResponse().getMessage())));
                return new GenericResponseDTO().noDataFoundResponse();
            }

            InputStream inputStream = minioService.get(Paths.get(detail.getDocURL()));
            byte[] document = ByteStreams.toByteArray(inputStream);
            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CQRS, "Movement Get Document", new Date(), "GET DOCUMENT", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
            return new GenericResponseDTO().successResponse(document);
        } catch (IOException | MinioException e) {
            log.info(e.getMessage());
            try {
                log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CQRS, "Movement Get document", new Date(), "GET DOCUMENT", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
            } catch (JsonProcessingException ex) {
                log.info(ex.getMessage());
            }
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage());
        }
    }

    @Async
    public org.springframework.util.concurrent.ListenableFuture<GenericResponseDTO<byte[]>> generateMovementDocumentCodeAsync(String mvID) throws Exception {
        return new AsyncResult<>(generateMovementDocumentByte(mvID));
    }

}
