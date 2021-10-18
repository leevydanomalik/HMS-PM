package com.bitozen.hms.web.hystrix.movement;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.dto.GetListRequestDTO;
import com.bitozen.hms.common.type.ProjectType;
import com.bitozen.hms.common.util.LogOpsUtil;
import com.bitozen.hms.pm.common.dto.query.movement.MovementDTO;
import com.bitozen.hms.web.handler.movement.query.CountAllMovementForWebQuery;
import com.bitozen.hms.web.handler.movement.query.GetAllMovementForWebQuery;
import com.bitozen.hms.web.handler.movement.query.GetMovementByIDQuery;
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
public class MovementHystrixQueryService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private transient QueryGateway gateway;

    @SneakyThrows
    @HystrixCommand(fallbackMethod = "defaultGetMovementByIDFallback")
    @Cacheable(value = "findOneByMovementIDCache", key = "#p0")
    public GenericResponseDTO<MovementDTO> getMovementByID(String movementID) {
        try {
            CompletableFuture<MovementDTO> result = gateway.query(new GetMovementByIDQuery(movementID), MovementDTO.class);
            if(result.get() == null) {
                log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(LogOpsUtil.getLogResponse(
                        ProjectType.CQRS, "Movement", new Date(), "Query", new GenericResponseDTO().noDataFoundResponse().getCode(),
                        new GenericResponseDTO().noDataFoundResponse().getMessage())));
                return new GenericResponseDTO().noDataFoundResponse();
            }
            log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CQRS, "Movement", new Date(), "Query", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
            return new GenericResponseDTO().successResponse(result.get());
        } catch (Exception e) {
            log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                    LogOpsUtil.getErrorResponse(ProjectType.CQRS, "Movement", new Date(), "Query", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage());
        }
    }

    public GenericResponseDTO<MovementDTO> defaultGetMovementByIDFallback(String movementID, Throwable e) throws IOException {
        return new GenericResponseDTO<MovementDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }

    @SneakyThrows
    @HystrixCommand(fallbackMethod = "defaultGetAllMovementWebFallback")
    @Cacheable(value = "getAllMovementWebCache", key = "#p0")
    public GenericResponseDTO<Map<String, Object>> getMovementForWeb(GetListRequestDTO dto) {
        try {
            CompletableFuture<List<MovementDTO>> result = gateway.query(new GetAllMovementForWebQuery(dto), ResponseTypes.multipleInstancesOf(MovementDTO.class));
            if (result.get() == null || result.get().isEmpty()) {
                log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(LogOpsUtil.getLogResponse(
                        ProjectType.CQRS, "Movement", new Date(), "Query", new GenericResponseDTO().noDataFoundResponse().getCode(),
                        new GenericResponseDTO().noDataFoundResponse().getMessage())));
                return new GenericResponseDTO().noDataFoundResponse();
            }
            log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CQRS, "Movement", new Date(), "Query", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
            Map<String, Object> resp = new HashMap<>();
            resp.put("datas", result.get());
            CompletableFuture<Integer> count = gateway.query(new CountAllMovementForWebQuery(dto), Integer.class);
            resp.put("totalData", count.get());
            return new GenericResponseDTO().successResponse(resp);
        } catch (Exception e) {
            log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                    LogOpsUtil.getErrorResponse(ProjectType.CQRS, "Movement", new Date(), "Query", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage());
        }
    }

    public GenericResponseDTO<Map<String, Object>> defaultGetAllMovementWebFallback(GetListRequestDTO dto, Throwable e) throws IOException {
        return new GenericResponseDTO<Map<String, Object>>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }
}
