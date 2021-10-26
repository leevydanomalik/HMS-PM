package com.bitozen.hms.web.service.minio.employmentletter;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.dto.RabbitFileDTO;
import com.bitozen.hms.common.type.ProjectType;
import com.bitozen.hms.common.util.LogOpsUtil;
import com.bitozen.hms.pm.common.EmploymentLetterStatus;
import com.bitozen.hms.pm.repository.employmentletter.EmploymentLetterRepository;
import com.bitozen.hms.projection.employmentletter.EmploymentLetterEntryProjection;
import com.bitozen.hms.rabbit.producer.employmentletter.EmploymentLetterRabbitProducer;
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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Jeremia
 */
@Service
@Slf4j
public class EmploymentLetterMinioService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmploymentLetterRepository employmentLetterRepository;

    @Autowired
    private MinioService minioService;

    @Autowired
    private EmploymentLetterRabbitProducer rabbitProducer;

    /**
     * Producer to rabbit MQ
     *
     * @param upload
     * @param elID
     * @param updatedBy
     * @param updatedDate
     * @return
     */
    public GenericResponseDTO<String> convertAndSendToRabbit(MultipartFile upload, String elID, String updatedBy, String updatedDate) {
        GenericResponseDTO<String> response = new GenericResponseDTO().successResponse();
        try {
            byte[] fileContent = ByteStreams.toByteArray(upload.getInputStream());
            String encodedString = Base64.encodeBase64String(fileContent);

            rabbitProducer.employmentLetterDocumentUploadProducer(new RabbitFileDTO(upload.getOriginalFilename(), upload.getContentType(), encodedString, elID, updatedBy, new SimpleDateFormat("dd-MM-yyyy").parse(updatedDate), null));

            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CQRS, "EmploymentLetter Upload Document", new Date(), "UPLOAD", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
            response.setData(elID);
            return response;
        } catch (Exception ex) {
            try {
                log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CRUD, "EmploymentLetter Upload Document", new Date(), "upload", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex.getStackTrace())));
            } catch (JsonProcessingException jpex) {
                log.info(jpex.getMessage());
            }
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex.getLocalizedMessage());
        }
    }

    /**
     * GENERATE DOC BYTE MINIO
     *
     * @param elID
     * @return
     */
    //GENERATE DOC BYTE
    public GenericResponseDTO<byte[]> generateEmploymentLetterDocumentByte(String elID) {
        try {
            Optional<EmploymentLetterEntryProjection> employmentLetter = employmentLetterRepository.findOneByElIDAndElStatusNot(elID, EmploymentLetterStatus.INACTIVE);
            if (!employmentLetter.isPresent()) {
                log.info(objectMapper.writeValueAsString(LogOpsUtil.getErrorResponse(
                        ProjectType.CQRS, "EmploymentLetter Get Document", new Date(), "GET DOCUMENT", new GenericResponseDTO().successResponse().getCode(),
                        new GenericResponseDTO().successResponse().getMessage())));
                return new GenericResponseDTO().noDataFoundResponse();
            }
            InputStream inputStream = minioService.get(Paths.get(employmentLetter.get().getElDocURL()));
            byte[] document = ByteStreams.toByteArray(inputStream);
            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CQRS, "EmploymentLetter Get Document", new Date(), "GET DOCUMENT", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
            return new GenericResponseDTO().successResponse(document);
        } catch (IOException | MinioException e) {
            log.info(e.getMessage());
            try {
                log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CQRS, "EmploymentLetter Get document", new Date(), "GET DOCUMENT", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
            } catch (JsonProcessingException ex) {
                log.info(ex.getMessage());
            }
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage());
        }
    }

    @Async
    public org.springframework.util.concurrent.ListenableFuture<GenericResponseDTO<byte[]>> generateEmploymentLetterDocumentCodeAsync(String elID) throws Exception {
        return new AsyncResult<>(generateEmploymentLetterDocumentByte(elID));
    }

    @CacheEvict(cacheNames = "getGenerateEmploymentLetterDocumentURLByte", allEntries = true)
    public void purgeCache() {
        log.info("Purging cache");
    }

    @CacheEvict(cacheNames = "url-photo", allEntries = true)
    public void purgeCacheURL() {
        log.info("Purging cache");
    }

}
