package com.bitozen.hms.web.hystrix.employmentletter;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.web.service.migration.employmentletter.EmploymentLetterMigrationService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.netflix.hystrix.exception.HystrixTimeoutException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
public class EmploymentLetterMigrationHystrixService {

    @Autowired
    EmploymentLetterMigrationService processor;

    @SneakyThrows
    @HystrixCommand(fallbackMethod = "defaultSaveUploadedFileFallback")
    @Caching(
            evict = {
                    @CacheEvict(value = "findOneByElIDCache", allEntries = true),
                    @CacheEvict(value = "getAllElWebCache", allEntries = true)
            }
    )
    public GenericResponseDTO<String> saveUploadedEmploymentLetterFile(MultipartFile upload) {
        GenericResponseDTO<String> response = new GenericResponseDTO().successResponse();
        processor.processEmploymentLetter(upload);
        response.setData(upload.getOriginalFilename());
        return response;
    }

    private GenericResponseDTO<String> defaultSaveUploadedFileFallback(MultipartFile upload, Throwable e) throws IOException {
        return new GenericResponseDTO<String>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }
}
