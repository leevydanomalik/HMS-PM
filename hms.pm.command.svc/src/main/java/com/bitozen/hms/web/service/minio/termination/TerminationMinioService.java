package com.bitozen.hms.web.service.minio.termination;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.dto.RabbitFileDTO;
import com.bitozen.hms.common.type.ProjectType;
import com.bitozen.hms.common.util.LogOpsUtil;
import com.bitozen.hms.pm.common.TerminationStatus;
import com.bitozen.hms.pm.common.dto.query.termination.TerminationDocumentDTO;
import com.bitozen.hms.pm.repository.termination.TerminationRepository;
import com.bitozen.hms.projection.termination.TerminationEntryProjection;
import com.bitozen.hms.rabbit.producer.termination.TerminationProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.Base64;
import com.google.common.io.ByteStreams;
import com.jlefebure.spring.boot.minio.MinioException;
import com.jlefebure.spring.boot.minio.MinioService;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class TerminationMinioService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TerminationRepository repository;

    @Autowired
    private MinioService minioService;

    @Autowired
    private TerminationProducer rabbitProducer;

    public GenericResponseDTO<String> convertAndSendToRabbit(MultipartFile upload, String terminationID) {
        GenericResponseDTO<String> response = new GenericResponseDTO().successResponse();
        try {

            byte[] fileContent = ByteStreams.toByteArray(upload.getInputStream());
            String encodedString = Base64.encodeBase64String(fileContent);

            RabbitFileDTO dto = new RabbitFileDTO();
            dto.setOriginalFileName(upload.getOriginalFilename());
            dto.setContentType(upload.getContentType());
            dto.setImageFile(encodedString);
            dto.setId(terminationID);

            rabbitProducer.uploadDocumentProducer(dto);

            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CRUD, "Termination Upload Document", new Date(), "UPLOAD", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
            response.setData(terminationID);
            return response;
        } catch (Exception ex) {
            try {
                log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Termination Upload Document", new Date(), "UPLOAD", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex.getStackTrace())));
            } catch (JsonProcessingException jpex) {
                log.info(jpex.getMessage());
            }
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex.getLocalizedMessage());
        }
    }

    public GenericResponseDTO<byte[]> generateTerminationDocumentByte(String terminationID) {
        try {
            /* Split id and detail id */
            String[] parts = terminationID.split(Pattern.quote("*"));
            String tmnID = parts[0];
            String docID = parts[1];

            /* get data from database */
            Optional<TerminationEntryProjection> termination = repository.findOneByTmnIDAndTmnStatusNot(tmnID.toUpperCase(), TerminationStatus.INACTIVE);
            if (!termination.isPresent()) {
                log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                        ProjectType.CRUD, "Termination Upload Document", new Date(), "GET DOCUMENT", new GenericResponseDTO().successResponse().getCode(),
                        new GenericResponseDTO().successResponse().getMessage())));
                return new GenericResponseDTO().noDataFoundResponse();
            }
            /* get data Detail */
            List<TerminationDocumentDTO> details = termination.get().getTmnDocs() == null ? new ArrayList<>() : termination.get().getTmnDocs();
            /* find detail by id */
            TerminationDocumentDTO detail = details.stream().filter(o -> o.getDocID().equalsIgnoreCase(docID)).findFirst().orElse(null);
            if (detail == null || detail.getDocURL() == null) {
                log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                        ProjectType.CRUD, "Termination Upload Document", new Date(), "GET DOCUMENT", new GenericResponseDTO().successResponse().getCode(),
                        new GenericResponseDTO().successResponse().getMessage())));
                return new GenericResponseDTO().noDataFoundResponse();
            }
            /* get data from Minio */
            InputStream inputStream = minioService.get(Paths.get(detail.getDocURL()));
            byte[] image = ByteStreams.toByteArray(inputStream);
            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CRUD, "Termination Upload Document", new Date(), "GET DOCUMENT", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
            return new GenericResponseDTO().successResponse(image);
        } catch (IOException | MinioException e) {
            log.info(e.getMessage());
            try {
                log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Termination Upload Document", new Date(), "GET DOCUMENT", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
            } catch (JsonProcessingException ex) {
                log.info(ex.getMessage());
            }
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage());
        }
    }

    @Async
    public org.springframework.util.concurrent.ListenableFuture<GenericResponseDTO<byte[]>> generateTerminationDocumentCodeAsync(String terminationID) throws Exception {
        return new AsyncResult<>(generateTerminationDocumentByte(terminationID));
    }

}
