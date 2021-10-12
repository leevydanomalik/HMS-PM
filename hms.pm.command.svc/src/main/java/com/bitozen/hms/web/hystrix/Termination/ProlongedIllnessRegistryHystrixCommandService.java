package com.bitozen.hms.web.hystrix.Termination;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.status.ResponseStatus;
import com.bitozen.hms.common.type.ProjectType;
import com.bitozen.hms.common.util.LogOpsUtil;
import com.bitozen.hms.pm.command.termination.ProlongedIllnessRegistryChangeCommand;
import com.bitozen.hms.pm.command.termination.ProlongedIllnessRegistryCreateCommand;
import com.bitozen.hms.pm.command.termination.ProlongedIllnessRegistryDeleteCommand;
import com.bitozen.hms.pm.common.dto.command.termination.ProlongedIllnessRegistryChangeCommandDTO;
import com.bitozen.hms.pm.common.dto.command.termination.ProlongedIllnessRegistryCreateCommandDTO;
import com.bitozen.hms.pm.common.dto.command.termination.ProlongedIllnessRegistryDeleteCommandDTO;
import com.bitozen.hms.web.helper.AssemblerHelper;
import com.bitozen.hms.web.helper.BizparHelper;
import com.bitozen.hms.web.helper.EmployeeHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.netflix.hystrix.exception.HystrixTimeoutException;
import java.io.IOException;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 *
 * @author Dumayangsari
 */
@Service
@Slf4j
public class ProlongedIllnessRegistryHystrixCommandService {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    BizparHelper bizparHelper;

    @Autowired
    EmployeeHelper employeeHelper;

    @Autowired
    AssemblerHelper assemblerHelper;

    private final CommandGateway commandGateway;
    
    @Autowired
    public ProlongedIllnessRegistryHystrixCommandService(CommandGateway commandGateway){
        this.commandGateway = commandGateway;
    }
    
    @HystrixCommand(fallbackMethod = "defaultPostProlongedIllnessRegistryFallback")
    @Caching(
            evict = {
                    @CacheEvict(value = "findOneByProlongedIllnessRegistryIDCache", allEntries = true),
                    @CacheEvict(value = "getAllProlongedIllnessRegistryWebCache", allEntries = true)
            }
    )
    public GenericResponseDTO<ProlongedIllnessRegistryCreateCommandDTO> postProlongedIllnessRegistry(ProlongedIllnessRegistryCreateCommandDTO dto) {
        GenericResponseDTO<ProlongedIllnessRegistryCreateCommandDTO> response = new GenericResponseDTO().successResponse();
        try {
            ProlongedIllnessRegistryCreateCommand command = new ProlongedIllnessRegistryCreateCommand(
                    dto.getPiID(),
                    dto.getStartDate(),
                    dto.getEndDate(),
                    dto.getDocURL(),
                    objectMapper.writeValueAsString(employeeHelper.findEmployeeOptimizeByKey(dto.getEmployee())),
                    objectMapper.writeValueAsString(employeeHelper.findEmployeeOptimizeByKey(dto.getRequestor())),
                    objectMapper.writeValueAsString(bizparHelper.convertBizpar(dto.getIlnessType())),
                    dto.getPiStatus(),
                    dto.getReason(),
                    objectMapper.writeValueAsString(assemblerHelper.toMetadata(dto.getMetadata())),
                    objectMapper.writeValueAsString(dto.getToken()),
                    dto.getRecordID(),
                    dto.getCreatedBy(),
                    dto.getCreatedDate()
            );
            commandGateway.send(command, new CommandCallback<ProlongedIllnessRegistryCreateCommand, Object>() {
                @Override
                public void onResult(CommandMessage<? extends ProlongedIllnessRegistryCreateCommand> commandMessage, CommandResultMessage<?> commandResultMessage) {
                    if (commandResultMessage.isExceptional() == false) {
                        try {
                            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                                    ProjectType.CQRS, "Prolonged Illness Registry", new Date(), "Command", new GenericResponseDTO().successResponse().getCode(),
                                    new GenericResponseDTO().successResponse().getMessage())));
                        } catch (JsonProcessingException ex) {
                            log.info(ex.getMessage());
                        }
                    } else {
                        response.setStatus(ResponseStatus.F);
                        response.setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
                        response.setMessage(commandResultMessage.exceptionResult().getLocalizedMessage());
                        try {
                            log.info(objectMapper.writeValueAsString(LogOpsUtil.getErrorResponse(
                                    ProjectType.CQRS, "Prolonged Illness Registry", new Date(), "Command", response.getCode(), commandResultMessage.exceptionResult().getStackTrace())));
                        } catch (JsonProcessingException ex) {
                            log.info(ex.getMessage());
                        }
                    }
                }
            });
        } catch(Exception e) {
            log.info(e.getMessage());
        }
        return response;
    }

    private GenericResponseDTO<ProlongedIllnessRegistryCreateCommandDTO> defaultPostProlongedIllnessRegistryFallback(ProlongedIllnessRegistryCreateCommandDTO dto, Throwable e) throws IOException {
        return new GenericResponseDTO<ProlongedIllnessRegistryCreateCommandDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }
    
    @HystrixCommand(fallbackMethod = "defaultPutProlongedIllnessRegistryFallback")
    @Caching(
            evict = {
                    @CacheEvict(value = "findOneByProlongedIllnessRegistryIDCache", allEntries = true),
                    @CacheEvict(value = "getAllProlongedIllnessRegistryWebCache", allEntries = true)
            }
    )
    public GenericResponseDTO<ProlongedIllnessRegistryChangeCommandDTO> putProlongedIllnessRegistry(ProlongedIllnessRegistryChangeCommandDTO dto) {
        GenericResponseDTO<ProlongedIllnessRegistryChangeCommandDTO> response = new GenericResponseDTO().successResponse();
        try {
            ProlongedIllnessRegistryChangeCommand command = new ProlongedIllnessRegistryChangeCommand(
                    dto.getPiID(),
                    dto.getStartDate(),
                    dto.getEndDate(),
                    dto.getDocURL(),
                    objectMapper.writeValueAsString(employeeHelper.findEmployeeOptimizeByKey(dto.getEmployee())),
                    objectMapper.writeValueAsString(employeeHelper.findEmployeeOptimizeByKey(dto.getRequestor())),
                    objectMapper.writeValueAsString(bizparHelper.convertBizpar(dto.getIlnessType())),
                    dto.getPiStatus(),
                    dto.getReason(),
                    objectMapper.writeValueAsString(assemblerHelper.toMetadata(dto.getMetadata())),
                    objectMapper.writeValueAsString(dto.getToken()),
                    dto.getUpdatedBy(),
                    dto.getUpdatedDate()
            );
            commandGateway.send(command, new CommandCallback<ProlongedIllnessRegistryChangeCommand, Object>() {
                @Override
                public void onResult(CommandMessage<? extends ProlongedIllnessRegistryChangeCommand> commandMessage, CommandResultMessage<?> commandResultMessage) {
                    if (commandResultMessage.isExceptional() == false) {
                        try {
                            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                                    ProjectType.CQRS, "Prolonged Illness Registry", new Date(), "Command", new GenericResponseDTO().successResponse().getCode(),
                                    new GenericResponseDTO().successResponse().getMessage())));
                        } catch (JsonProcessingException ex) {
                            log.info(ex.getMessage());
                        }
                    } else {
                        response.setStatus(ResponseStatus.F);
                        response.setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
                        response.setMessage(commandResultMessage.exceptionResult().getLocalizedMessage());
                        try {
                            log.info(objectMapper.writeValueAsString(LogOpsUtil.getErrorResponse(
                                    ProjectType.CQRS, "Prolonged Illness Registry", new Date(), "Command", response.getCode(), commandResultMessage.exceptionResult().getStackTrace())));
                        } catch (JsonProcessingException ex) {
                            log.info(ex.getMessage());
                        }
                    }
                }
            });
        } catch(Exception e) {
            log.info(e.getMessage());
        }
        return response;
    }

    private GenericResponseDTO<ProlongedIllnessRegistryChangeCommandDTO> defaultPutProlongedIllnessRegistryFallback(ProlongedIllnessRegistryChangeCommandDTO dto, Throwable e) throws IOException {
        return new GenericResponseDTO<ProlongedIllnessRegistryChangeCommandDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }

    @HystrixCommand(fallbackMethod = "defaultDeleteProlongedIllnessRegistryFallback")
    @Caching(
            evict = {
                    @CacheEvict(value = "findOneByProlongedIllnessRegistryIDCache", allEntries = true),
                    @CacheEvict(value = "getAllProlongedIllnessRegistryWebCache", allEntries = true)
            }
    )
    public GenericResponseDTO<ProlongedIllnessRegistryDeleteCommandDTO> deleteProlongedIllnessRegistry(ProlongedIllnessRegistryDeleteCommandDTO dto) {
        GenericResponseDTO<ProlongedIllnessRegistryDeleteCommandDTO> response = new GenericResponseDTO().successResponse();
        try {
            ProlongedIllnessRegistryDeleteCommand command = new ProlongedIllnessRegistryDeleteCommand(
                    dto.getPiID(),
                    dto.getUpdatedBy()
            );
            commandGateway.send(command, new CommandCallback<ProlongedIllnessRegistryDeleteCommand, Object>() {
                @Override
                public void onResult(CommandMessage<? extends ProlongedIllnessRegistryDeleteCommand> commandMessage, CommandResultMessage<?> commandResultMessage) {
                    if (commandResultMessage.isExceptional() == false) {
                        try {
                            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                                    ProjectType.CQRS, "Prolonged Illness Registry", new Date(), "Command", new GenericResponseDTO().successResponse().getCode(),
                                    new GenericResponseDTO().successResponse().getMessage())));
                        } catch (JsonProcessingException ex) {
                            log.info(ex.getMessage());
                        }
                    } else {
                        response.setStatus(ResponseStatus.F);
                        response.setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
                        response.setMessage(commandResultMessage.exceptionResult().getLocalizedMessage());
                        try {
                            log.info(objectMapper.writeValueAsString(LogOpsUtil.getErrorResponse(
                                    ProjectType.CQRS, "Prolonged Illness Registry", new Date(), "Command", response.getCode(), commandResultMessage.exceptionResult().getStackTrace())));
                        } catch (JsonProcessingException ex) {
                            log.info(ex.getMessage());
                        }
                    }
                }
            });
        } catch(Exception e) {
            log.info(e.getMessage());
        }
        return response;
    }

    private GenericResponseDTO<ProlongedIllnessRegistryDeleteCommandDTO> defaultDeleteProlongedIllnessRegistryFallback(ProlongedIllnessRegistryDeleteCommandDTO dto, Throwable e) throws IOException {
        return new GenericResponseDTO<ProlongedIllnessRegistryDeleteCommandDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }
}
