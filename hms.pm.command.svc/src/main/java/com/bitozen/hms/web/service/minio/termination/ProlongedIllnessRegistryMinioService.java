package com.bitozen.hms.web.service.minio.termination;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.dto.RabbitFileDTO;
import com.bitozen.hms.common.type.ProjectType;
import com.bitozen.hms.common.util.LogOpsUtil;
import com.bitozen.hms.pm.common.ProlongedIllnessStatus;
import com.bitozen.hms.pm.repository.termination.ProlongedIllnessRegistryRepository;
import com.bitozen.hms.projection.termination.ProlongedIllnessRegistryEntryProjection;
import com.bitozen.hms.rabbit.producer.termination.ProlongedIllnessRegistryRabbitProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.Base64;
import com.google.common.io.ByteStreams;
import com.jlefebure.spring.boot.minio.MinioException;
import com.jlefebure.spring.boot.minio.MinioService;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Dumayangsari
 */
@Service
@Slf4j
public class ProlongedIllnessRegistryMinioService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProlongedIllnessRegistryRepository repository;

    @Autowired
    private MinioService minioService;

    @Autowired
    private ProlongedIllnessRegistryRabbitProducer producer;

    /**
     * Producer to rabbit MQ
     *
     * @param upload
     * @param piID
     * @param updatedBy
     * @param updatedDate
     * @return
     */
    public GenericResponseDTO<String> convertAndSendToRabbit(MultipartFile upload, String piID, String updatedBy, String updatedDate) {
        GenericResponseDTO<String> response = new GenericResponseDTO().successResponse();
        try {
            byte[] fileContent = ByteStreams.toByteArray(upload.getInputStream());
            String encodedString = Base64.encodeBase64String(fileContent);

            producer.ProlongedIllnessRegistryDocumentUploadProducer(new RabbitFileDTO(upload.getOriginalFilename(), upload.getContentType(), encodedString, piID, updatedBy, new SimpleDateFormat("dd-MM-yyyy").parse(updatedDate), null));

            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CQRS, "Prolonged Illness Registry Upload Document", new Date(), "UPLOAD", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
            response.setData(piID);
            return response;
        } catch (Exception ex) {
            try {
                log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Prolonged Illness Registrytt Upload Document", new Date(), "upload", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex.getStackTrace())));
            } catch (JsonProcessingException jpex) {
                log.info(jpex.getMessage());
            }
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex.getLocalizedMessage());
        }
    }

    /**
     * GENERATE DOC BYTE MINIO
     *
     * @param piID
     * @return
     */
    //GENERATE DOC BYTE
    public GenericResponseDTO<byte[]> generateProlongedIllnessRegistryDocumentByte(String piID) {
        try {
            Optional<ProlongedIllnessRegistryEntryProjection> pi = repository.findOneByPiIDAndPiStatus(piID, ProlongedIllnessStatus.ACTIVE);
            if (!pi.isPresent()) {
                log.info(objectMapper.writeValueAsString(LogOpsUtil.getErrorResponse(
                        ProjectType.CQRS, "Prolonged Illness Registry Get Document", new Date(), "GET DOCUMENT", new GenericResponseDTO().successResponse().getCode(),
                        new GenericResponseDTO().successResponse().getMessage())));
                return new GenericResponseDTO().noDataFoundResponse();
            }
            InputStream inputStream = minioService.get(Paths.get(pi.get().getDocURL()));
            byte[] document = ByteStreams.toByteArray(inputStream);
            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CQRS, "Prolonged Illness Registry Get Document", new Date(), "GET DOCUMENT", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
            return new GenericResponseDTO().successResponse(document);
        } catch (IOException | MinioException e) {
            log.info(e.getMessage());
            try {
                log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CQRS, "Prolonged Illness Registry Get document", new Date(), "GET DOCUMENT", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
            } catch (JsonProcessingException ex) {
                log.info(ex.getMessage());
            }
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage());
        }
    }

    @Async
    public org.springframework.util.concurrent.ListenableFuture<GenericResponseDTO<byte[]>> generateProlongedIllnessRegistryDocumentCodeAsync(String piID) throws Exception {
        return new AsyncResult<>(generateProlongedIllnessRegistryDocumentByte(piID));
    }
}
