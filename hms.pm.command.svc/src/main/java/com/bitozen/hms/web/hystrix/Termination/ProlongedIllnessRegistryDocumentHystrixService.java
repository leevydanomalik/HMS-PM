package com.bitozen.hms.web.hystrix.Termination;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.type.ProjectType;
import com.bitozen.hms.common.util.LogOpsUtil;
import com.bitozen.hms.web.service.minio.termination.ProlongedIllnessRegistryMinioService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.netflix.hystrix.exception.HystrixTimeoutException;
import java.io.IOException;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Dumayangsari
 */
@Service
@Slf4j
public class ProlongedIllnessRegistryDocumentHystrixService {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProlongedIllnessRegistryMinioService documentService;

    @HystrixCommand(fallbackMethod = "defaultSaveUploadedFileFallback")
    @Caching(
            evict = {
                    @CacheEvict(value = "findOneByPiIDCache", allEntries = true),
                    @CacheEvict(value = "getAllProlongedIllnessRegistryWebCache", allEntries = true),
                    @CacheEvict(value = "getGenerateProlongedIllnessRegistryDocumentURLByte", allEntries = true)
            }
    )
    public GenericResponseDTO<String> saveUploadedFile(MultipartFile upload, String piID,String updatedBy, String updatedDate) 
    {
        GenericResponseDTO<String> response = new GenericResponseDTO().successResponse();
        try {
            documentService.convertAndSendToRabbit(upload, piID, updatedBy, updatedDate);
            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CQRS, "Prolonged Illness Registry Upload Document", new Date(), "UPLOAD", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
            response.setData(piID);
            return response;
        } catch (IOException ex) {
            try {
                log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CQRS, "Prolonged Illness Registry Upload Document", new Date(), "UPLOAD", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex.getStackTrace())));
            } catch (JsonProcessingException jpex) {
                log.info(jpex.getMessage());
            }
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex.getLocalizedMessage());
        }
    }

    private GenericResponseDTO<String> defaultSaveUploadedFileFallback(MultipartFile upload, String piID, String updatedBy, String updatedDate, Throwable e) throws IOException {
        return new GenericResponseDTO<String>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }

    @HystrixCommand(fallbackMethod = "defaultGetGenerateProlongedIllnessRegistryDocumentURLByteFallback")
    @Cacheable(cacheNames = "getGenerateProlongedIllnessRegistryDocumentURLByte", sync = true)
    public GenericResponseDTO<byte[]> generateProlongedIllnessRegistryDocumentURLByte(String piID) throws Exception {
        try {
            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CQRS, "Prolonged Illness Registry Get Document", new Date(), "GET DOCUMENT", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
            return documentService.generateProlongedIllnessRegistryDocumentCodeAsync(piID).get();
        } catch (IOException e) {
            log.info(e.getMessage());
            try {
                log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CQRS, "Prolonged Illness Registry Get Document", new Date(), "GET DOCUMENT", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
            } catch (JsonProcessingException ex) {
                log.info(ex.getMessage());
            }
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage());
        }
    }

    public GenericResponseDTO<byte[]> defaultGetGenerateProlongedIllnessRegistryDocumentURLByteFallback(String piID, Throwable e) throws IOException {
        return new GenericResponseDTO<byte[]>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }
}
