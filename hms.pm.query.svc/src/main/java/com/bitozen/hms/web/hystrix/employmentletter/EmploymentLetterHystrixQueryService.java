package com.bitozen.hms.web.hystrix.employmentletter;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.dto.GetListRequestDTO;
import com.bitozen.hms.common.type.ProjectType;
import com.bitozen.hms.common.util.LogOpsUtil;
import com.bitozen.hms.pm.common.dto.query.employmentletter.EmploymentLetterDTO;
import com.bitozen.hms.web.handler.employmentletter.query.CountAllEmploymentLetterForWebQuery;
import com.bitozen.hms.web.handler.employmentletter.query.GetAllEmploymentLetterForWebQuery;
import com.bitozen.hms.web.handler.employmentletter.query.GetEmploymentLetterByElDocNumberQuery;
import com.bitozen.hms.web.handler.employmentletter.query.GetEmploymentLetterByIDQuery;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.netflix.hystrix.exception.HystrixTimeoutException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jeremia
 */
@Service
@Slf4j
public class EmploymentLetterHystrixQueryService {
    
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private transient QueryGateway gateway;
    
    @SneakyThrows
    @HystrixCommand(fallbackMethod = "defaultGetElByIDFallback")
    @Cacheable(value = "findOneByElIDCache", key = "#p0")
    public GenericResponseDTO<EmploymentLetterDTO> getEmploymentLetterByID(String elID) {
        try {
            CompletableFuture<EmploymentLetterDTO> result = gateway.query(new GetEmploymentLetterByIDQuery(elID), EmploymentLetterDTO.class);
            if(result.get() == null) {
                log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(LogOpsUtil.getLogResponse(
                        ProjectType.CQRS, "Employment Letter", new Date(), "Query", new GenericResponseDTO().noDataFoundResponse().getCode(),
                        new GenericResponseDTO().noDataFoundResponse().getMessage())));
                return new GenericResponseDTO().noDataFoundResponse();
            }
            log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CQRS, "Employment Letter", new Date(), "Query", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
            return new GenericResponseDTO().successResponse(result.get());
        } catch (Exception e) {
            log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                    LogOpsUtil.getErrorResponse(ProjectType.CQRS, "Employment Letter", new Date(), "Query", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage());
        }
    }

    public GenericResponseDTO<EmploymentLetterDTO> defaultGetElByIDFallback(String elID, Throwable e) throws IOException {
        return new GenericResponseDTO<EmploymentLetterDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }
    
    @SneakyThrows
    @HystrixCommand(fallbackMethod = "defaultGetElByDocNumberFallback")
    @Cacheable(value = "findOneByElDocNumberCache", key = "#p0")
    public GenericResponseDTO<EmploymentLetterDTO> getEmploymentLetterByElDocNumber(String elDocNumber) {
        try {
            CompletableFuture<EmploymentLetterDTO> result = gateway.query(new GetEmploymentLetterByElDocNumberQuery(elDocNumber), EmploymentLetterDTO.class);
            if(result.get() == null) {
                log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(LogOpsUtil.getLogResponse(
                        ProjectType.CQRS, "Employment Letter", new Date(), "Query", new GenericResponseDTO().noDataFoundResponse().getCode(),
                        new GenericResponseDTO().noDataFoundResponse().getMessage())));
                return new GenericResponseDTO().noDataFoundResponse();
            }
            log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CQRS, "Employment Letter", new Date(), "Query", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
            return new GenericResponseDTO().successResponse(result.get());
        } catch (Exception e) {
            log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                    LogOpsUtil.getErrorResponse(ProjectType.CQRS, "Employment Letter", new Date(), "Query", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage());
        }
    }

    public GenericResponseDTO<EmploymentLetterDTO> defaultGetElByDocNumberFallback(String elDocNumber, Throwable e) throws IOException {
        return new GenericResponseDTO<EmploymentLetterDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }
    
    @SneakyThrows
    @HystrixCommand(fallbackMethod = "defaultGetAllElWebFallback")
    @Cacheable(value = "getAllElWebCache", key = "#p0")
    public GenericResponseDTO<Map<String, Object>> getEmploymentLetterForWeb(GetListRequestDTO dto) {
        try {
            CompletableFuture<List<EmploymentLetterDTO>> result = gateway.query(new GetAllEmploymentLetterForWebQuery(dto), ResponseTypes.multipleInstancesOf(EmploymentLetterDTO.class));
            if (result.get() == null || result.get().isEmpty()) {
                log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(LogOpsUtil.getLogResponse(
                        ProjectType.CQRS, "Employment Letter", new Date(), "Query", new GenericResponseDTO().noDataFoundResponse().getCode(),
                        new GenericResponseDTO().noDataFoundResponse().getMessage())));
                return new GenericResponseDTO().noDataFoundResponse();
            }
            log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CQRS, "Employment Letter", new Date(), "Query", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
            Map<String, Object> resp = new HashMap<>();
            resp.put("datas", result.get());
            CompletableFuture<Integer> count = gateway.query(new CountAllEmploymentLetterForWebQuery(dto), Integer.class);
            resp.put("totalData", count.get());
            return new GenericResponseDTO().successResponse(resp);
        } catch (Exception e) {
            log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                    LogOpsUtil.getErrorResponse(ProjectType.CQRS, "Employment Letter", new Date(), "Query", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage());
        }
    }

    public GenericResponseDTO<Map<String, Object>> defaultGetAllElWebFallback(GetListRequestDTO dto, Throwable e) throws IOException {
        return new GenericResponseDTO<Map<String, Object>>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }
    
}
