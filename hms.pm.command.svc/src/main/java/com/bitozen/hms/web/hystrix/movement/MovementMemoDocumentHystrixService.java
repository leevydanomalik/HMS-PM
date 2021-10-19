package com.bitozen.hms.web.hystrix.movement;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.type.ProjectType;
import com.bitozen.hms.common.util.LogOpsUtil;
import com.bitozen.hms.web.service.minio.movement.MovementMemoMinioService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.netflix.hystrix.exception.HystrixTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@Service
@Slf4j
public class MovementMemoDocumentHystrixService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MovementMemoMinioService documentService;

    @HystrixCommand(fallbackMethod = "defaultSaveUploadedFileFallback")
    @Caching(
            evict = {
                    @CacheEvict(value = "findOneByMovementIDCache", allEntries = true),
                    @CacheEvict(value = "getAllMovementWebCache", allEntries = true),
                    @CacheEvict(value = "getGenerateMovementDocumentURLByte", allEntries = true)
            }
    )
    public GenericResponseDTO<String> saveUploadedFile(MultipartFile upload, String mvID, String empID, String memoID, String docID) {

        try {
            String concatID = mvID.concat("*").concat(empID).concat("*").concat(memoID).concat("*").concat(docID);
            GenericResponseDTO<String> response = new GenericResponseDTO().successResponse();
            documentService.convertAndSendToRabbit(upload, concatID);
            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CQRS, "Movement Memo Upload Document", new Date(), "UPLOAD", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
            response.setData(concatID);
            return response;
        } catch (IOException ex) {
            try {
                log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CQRS, "Movement Memo Upload Document", new Date(), "UPLOAD", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex.getStackTrace())));
            } catch (JsonProcessingException jpex) {
                log.info(jpex.getMessage());
            }
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex.getLocalizedMessage());
        }
    }

    private GenericResponseDTO<String> defaultSaveUploadedFileFallback(MultipartFile upload, String mvID, String empID, String memoID, String docID, Throwable e) throws IOException {
        return new GenericResponseDTO<String>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }

    @HystrixCommand(fallbackMethod = "defaultGetGenerateMovementMemoDocumentURLByteFallback")
    @Cacheable(cacheNames = "getGenerateMovementMemoDocumentURLByte", sync = true)
    public GenericResponseDTO<byte[]> generateMovementMemoDocumentURLByte(String memoID) throws Exception {
        try {
            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CQRS, "Movement Memo Get Document", new Date(), "GET DOCUMENT", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
            return documentService.generateMovementMemoDocumentCodeAsync(memoID).get();
        } catch (IOException e) {
            log.info(e.getMessage());
            try {
                log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CQRS, "Movement Memo Get Document", new Date(), "GET DOCUMENT", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
            } catch (JsonProcessingException ex) {
                log.info(ex.getMessage());
            }
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage());
        }
    }

    public GenericResponseDTO<byte[]> defaultGetGenerateMovementMemoDocumentURLByteFallback(String memoID, Throwable e) throws IOException {
        return new GenericResponseDTO<byte[]>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }
}
