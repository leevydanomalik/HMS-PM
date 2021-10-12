package com.bitozen.hms.web.hystrix.Termination;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.type.ProjectType;
import com.bitozen.hms.common.util.LogOpsUtil;
import com.bitozen.hms.web.service.minio.termination.TerminationMinioService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.netflix.hystrix.exception.HystrixTimeoutException;
import java.io.IOException;
import java.util.Date;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
@Slf4j
public class TerminationDocumentHystrixService {
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    TerminationMinioService minioService;
    
    @SneakyThrows
    @HystrixCommand(fallbackMethod = "defaultUploadedTerminationDocumentFileFallback")
    @Caching(
            evict = {
                @CacheEvict(value = "findOneByTerminationIDCache", allEntries = true),
                @CacheEvict(value = "getAllTerminationWebCache", allEntries = true),
                @CacheEvict(value = "generateTerminationDocumentByteCache", allEntries = true) 
            }
    )
    public GenericResponseDTO<String> saveUploadedTerminationDocumentFile(MultipartFile upload, String terminationID, String docID) {
        try { 
            String concatID = terminationID.concat("*").concat(docID);
            GenericResponseDTO<String> response = new GenericResponseDTO().successResponse();
            minioService.convertAndSendToRabbit(upload, concatID);
            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CRUD, "Termination Document Upload", new Date(), 
                    "upload", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
            response.setData(concatID);
            return response;
        } catch (IOException ex) {
            try {
                log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Termination Document Upload", 
                                new Date(), "upload", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex.getStackTrace())));
            } catch (JsonProcessingException jpex) {
                log.info(jpex.getMessage());
            }
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex.getLocalizedMessage());
        }
    }
    
     private GenericResponseDTO<String> defaultUploadedTerminationDocumentFileFallback(MultipartFile upload, String terminationID, String docID, Throwable e) throws IOException {
        return new GenericResponseDTO<String>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }
     
      @Cacheable(cacheNames = "generateTerminationDocumentByteCache", sync = true)
    @HystrixCommand(fallbackMethod = "defaultGenerateTerminationDocumentByteFallback")
    public GenericResponseDTO<byte[]> generateTerminationDocumentByte(String terminationID, String docID) throws Exception {
        try {  
        	String concatID = terminationID.concat("*").concat(docID);
            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CRUD, "Termination Document Upload", new Date(), "generate", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
            return minioService.generateTerminationDocumentCodeAsync(concatID).get();
        } catch (IOException ex) {
            try {
                log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Termination Document Upload", new Date(), "generate", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex.getStackTrace())));
            } catch (JsonProcessingException jpex) {
                log.info(jpex.getMessage());
            }
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex.getLocalizedMessage());
        }
    }
    
     private GenericResponseDTO<byte[]> defaultGenerateTerminationDocumentByteFallback(String terminationID, String docID, Throwable e) throws IOException {
        return new GenericResponseDTO<byte[]>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    } 
    
    
    
    
}
