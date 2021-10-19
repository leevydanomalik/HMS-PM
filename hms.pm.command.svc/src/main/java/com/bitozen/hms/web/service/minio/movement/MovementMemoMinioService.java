package com.bitozen.hms.web.service.minio.movement;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.dto.RabbitFileDTO;
import com.bitozen.hms.common.dto.share.DocumentDTO;
import com.bitozen.hms.common.type.ProjectType;
import com.bitozen.hms.common.util.LogOpsUtil;
import com.bitozen.hms.pm.common.MVMemoStatus;
import com.bitozen.hms.pm.common.MVStatus;
import com.bitozen.hms.pm.common.dto.query.movement.MovementMemoDTOProjection;
import com.bitozen.hms.pm.repository.movement.MovementRepository;
import com.bitozen.hms.projection.movement.MovementEntryProjection;
import com.bitozen.hms.rabbit.producer.movement.MovementRabbitProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.Base64;
import com.google.common.io.ByteStreams;
import com.jlefebure.spring.boot.minio.MinioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Slf4j
public class MovementMemoMinioService {

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
     * @param templateID
     * @return
     */
    public GenericResponseDTO<String> convertAndSendToRabbit(MultipartFile upload, String templateID) {
        GenericResponseDTO<String> response = new GenericResponseDTO().successResponse();
        try {
            byte[] fileContent = ByteStreams.toByteArray(upload.getInputStream());
            String encodedString = Base64.encodeBase64String(fileContent);

            RabbitFileDTO dto = new RabbitFileDTO();
            dto.setOriginalFileName(upload.getOriginalFilename());
            dto.setContentType(upload.getContentType());
            dto.setImageFile(encodedString);
            dto.setId(templateID);

            producer.movementMemoDocumentUploadProducer(dto) ;

            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CQRS, "Movement Memo Upload Document", new Date(), "UPLOAD", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
            response.setData(templateID);
            return response;
        } catch (Exception ex) {
            try {
                log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Movement Memo Upload Document", new Date(), "upload", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex.getStackTrace())));
            } catch (JsonProcessingException jpex) {
                log.info(jpex.getMessage());
            }
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex.getLocalizedMessage());
        }
    }

    /**
     * GENERATE DOC BYTE MINIO
     *
     * @param memoID
     * @return
     */
    //GENERATE DOC BYTE
    public GenericResponseDTO<byte[]> generateMovementMemoDocumentByte(String memoID) {
        try {
            log.info("HERE");
            List<MovementMemoDTOProjection> movementMemo = repository.findMemoByMemoID(memoID, MVMemoStatus.INACTIVE.name());
            log.info("SIZE: " + movementMemo.size());
            if (movementMemo.isEmpty()) {
                log.info(objectMapper.writeValueAsString(LogOpsUtil.getErrorResponse(
                        ProjectType.CQRS, "Movement Memo Get Document", new Date(), "GET DOCUMENT", new GenericResponseDTO().successResponse().getCode(),
                        new GenericResponseDTO().successResponse().getMessage())));
                return new GenericResponseDTO().noDataFoundResponse();
            }

            InputStream inputStream = minioService.get(Paths.get(movementMemo.get(0).getMovement().getMemoDocNumber()));
            byte[] document = ByteStreams.toByteArray(inputStream);
            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CQRS, "Movement Memo Get Document", new Date(), "GET DOCUMENT", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
            return new GenericResponseDTO().successResponse(document);
        } catch (Exception e) {
            log.info(e.getMessage());
            try {
                log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CQRS, "Movement Memo Get document", new Date(), "GET DOCUMENT", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
            } catch (JsonProcessingException ex) {
                log.info(ex.getMessage());
            }
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage());
        }
    }

    @Async
    public org.springframework.util.concurrent.ListenableFuture<GenericResponseDTO<byte[]>> generateMovementMemoDocumentCodeAsync(String memoID) throws Exception {
        return new AsyncResult<>(generateMovementMemoDocumentByte(memoID));
    }
}
