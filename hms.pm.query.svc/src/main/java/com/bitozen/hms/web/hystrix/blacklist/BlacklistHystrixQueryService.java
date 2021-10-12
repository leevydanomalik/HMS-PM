package com.bitozen.hms.web.hystrix.blacklist;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.dto.GetListRequestDTO;
import com.bitozen.hms.common.type.ProjectType;
import com.bitozen.hms.common.util.LogOpsUtil;
import com.bitozen.hms.pm.common.dto.query.blacklist.BlacklistDTO;
import com.bitozen.hms.web.handler.blacklist.query.CountAllForWebQuery;
import com.bitozen.hms.web.handler.blacklist.query.GetAllForWebQuery;
import com.bitozen.hms.web.handler.blacklist.query.GetByIDQuery;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.netflix.hystrix.exception.HystrixTimeoutException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class BlacklistHystrixQueryService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private transient QueryGateway gateway;

    @SneakyThrows
    @HystrixCommand(fallbackMethod = "defaultGetBlacklistByIDFallback")
    @Cacheable(value = "findOneByBlacklistIDCache", key = "#p0")
    public GenericResponseDTO<BlacklistDTO> getBlacklistByID(String blacklistID) {
        try {
            CompletableFuture<BlacklistDTO> result = gateway.query(new GetByIDQuery(blacklistID), BlacklistDTO.class);
            if(result.get() == null) {
                log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(LogOpsUtil.getLogResponse(
                        ProjectType.CQRS, "Blacklist", new Date(), "Query", new GenericResponseDTO().noDataFoundResponse().getCode(),
                        new GenericResponseDTO().noDataFoundResponse().getMessage())));
                return new GenericResponseDTO().noDataFoundResponse();
            }
            log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CQRS, "Blacklist", new Date(), "Query", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
            return new GenericResponseDTO().successResponse(result.get());
        } catch (Exception e) {
            log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                    LogOpsUtil.getErrorResponse(ProjectType.CQRS, "Blacklist", new Date(), "Query", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage());
        }
    }

    public GenericResponseDTO<BlacklistDTO> defaultGetBlacklistByIDFallback(String blacklistID, Throwable e) throws IOException {
        return new GenericResponseDTO<BlacklistDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }

    @SneakyThrows
    @HystrixCommand(fallbackMethod = "defaultGetAllBlacklistWebFallback")
    @Cacheable(value = "getAllBlacklistWebCache", key = "#p0")
    public GenericResponseDTO<Map<String, Object>> getBlacklistForWeb(GetListRequestDTO dto) {
        try {
            CompletableFuture<List<BlacklistDTO>> result = gateway.query(new GetAllForWebQuery(dto), ResponseTypes.multipleInstancesOf(BlacklistDTO.class));
            if (result.get() == null || result.get().isEmpty()) {
                log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(LogOpsUtil.getLogResponse(
                        ProjectType.CQRS, "Blacklist", new Date(), "Query", new GenericResponseDTO().noDataFoundResponse().getCode(),
                        new GenericResponseDTO().noDataFoundResponse().getMessage())));
                return new GenericResponseDTO().noDataFoundResponse();
            }
            log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CQRS, "Blacklist", new Date(), "Query", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
            Map<String, Object> resp = new HashMap<>();
            resp.put("datas", result.get());
            CompletableFuture<Integer> count = gateway.query(new CountAllForWebQuery(dto), Integer.class);
            resp.put("totalData", count.get());
            return new GenericResponseDTO().successResponse(resp);
        } catch (Exception e) {
            log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                    LogOpsUtil.getErrorResponse(ProjectType.CQRS, "Blacklist", new Date(), "Query", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage());
        }
    }

    public GenericResponseDTO<Map<String, Object>> defaultGetAllBlacklistWebFallback(GetListRequestDTO dto, Throwable e) throws IOException {
        return new GenericResponseDTO<Map<String, Object>>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }
}
