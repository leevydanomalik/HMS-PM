package com.bitozen.hms.web.hystrix.termination;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.dto.GetListRequestDTO;
import com.bitozen.hms.common.type.ProjectType;
import com.bitozen.hms.common.util.LogOpsUtil;
import com.bitozen.hms.pm.common.dto.query.termination.ProlongedIllnessRegistryDTO;
import com.bitozen.hms.web.handler.termination.query.CountAllForWebQuery;
import com.bitozen.hms.web.handler.termination.query.GetAllForWebQuery;
import com.bitozen.hms.web.handler.termination.query.GetByIDQuery;
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
 * @author Dumayangsari
 */
@Service
@Slf4j
public class ProlongedIllnessRegistryHystrixQueryService {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private transient QueryGateway gateway;

    @SneakyThrows
    @HystrixCommand(fallbackMethod = "defaultGetProlongedIllnessRegistryByIDFallback")
    @Cacheable(value = "findOneByProlongedIllnessRegistryIDCache", key = "#p0")
    public GenericResponseDTO<ProlongedIllnessRegistryDTO> getProlongedIllnessRegistryByID(String piID) {
        try {
            CompletableFuture<ProlongedIllnessRegistryDTO> result = gateway.query(new GetByIDQuery(piID), ProlongedIllnessRegistryDTO.class);
            if(result.get() == null) {
                log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(LogOpsUtil.getLogResponse(
                        ProjectType.CQRS, "Prolonged Illness Registry", new Date(), "Query", new GenericResponseDTO().noDataFoundResponse().getCode(),
                        new GenericResponseDTO().noDataFoundResponse().getMessage())));
                return new GenericResponseDTO().noDataFoundResponse();
            }
            log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CQRS, "Prolonged Illness Registry", new Date(), "Query", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
            return new GenericResponseDTO().successResponse(result.get());
        } catch (Exception e) {
            log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                    LogOpsUtil.getErrorResponse(ProjectType.CQRS, "Prolonged Illness Registry", new Date(), "Query", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage());
        }
    }

    public GenericResponseDTO<ProlongedIllnessRegistryDTO> defaultGetProlongedIllnessRegistryByIDFallback(String piID, Throwable e) throws IOException {
        return new GenericResponseDTO<ProlongedIllnessRegistryDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }

    @SneakyThrows
    @HystrixCommand(fallbackMethod = "defaultGetAllProlongedIllnessRegistryWebFallback")
    @Cacheable(value = "getAllProlongedIllnessRegistryWebCache", key = "#p0")
    public GenericResponseDTO<Map<String, Object>> getProlongedIllnessRegistryForWeb(GetListRequestDTO dto) {
        try {
            CompletableFuture<List<ProlongedIllnessRegistryDTO>> result = gateway.query(new GetAllForWebQuery(dto), ResponseTypes.multipleInstancesOf(ProlongedIllnessRegistryDTO.class));
            if (result.get() == null || result.get().isEmpty()) {
                log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(LogOpsUtil.getLogResponse(
                        ProjectType.CQRS, "Prolonged Illness Registry", new Date(), "Query", new GenericResponseDTO().noDataFoundResponse().getCode(),
                        new GenericResponseDTO().noDataFoundResponse().getMessage())));
                return new GenericResponseDTO().noDataFoundResponse();
            }
            log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CQRS, "Prolonged Illness Registry", new Date(), "Query", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
            Map<String, Object> resp = new HashMap<>();
            resp.put("datas", result.get());
            CompletableFuture<Integer> count = gateway.query(new CountAllForWebQuery(dto), Integer.class);
            resp.put("totalData", count.get());
            return new GenericResponseDTO().successResponse(resp);
        } catch (Exception e) {
            log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                    LogOpsUtil.getErrorResponse(ProjectType.CQRS, "Prolonged Illness Registry", new Date(), "Query", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage());
        }
    }

    public GenericResponseDTO<Map<String, Object>> defaultGetAllProlongedIllnessRegistryWebFallback(GetListRequestDTO dto, Throwable e) throws IOException {
        return new GenericResponseDTO<Map<String, Object>>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }
}
