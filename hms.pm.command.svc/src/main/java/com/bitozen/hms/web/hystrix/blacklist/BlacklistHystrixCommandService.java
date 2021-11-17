package com.bitozen.hms.web.hystrix.blacklist;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.status.ResponseStatus;
import com.bitozen.hms.common.type.ProjectType;
import com.bitozen.hms.common.util.LogOpsUtil;
import com.bitozen.hms.pm.command.blacklist.BlacklistChangeCommand;
import com.bitozen.hms.pm.command.blacklist.BlacklistCreateCommand;
import com.bitozen.hms.pm.command.blacklist.BlacklistDeleteCommand;
import com.bitozen.hms.pm.command.blacklist.BlacklistStateAndStatusChangeCommand;
import com.bitozen.hms.pm.common.dto.command.blacklist.BlacklistChangeCommandDTO;
import com.bitozen.hms.pm.common.dto.command.blacklist.BlacklistCreateCommandDTO;
import com.bitozen.hms.pm.common.dto.command.blacklist.BlacklistDeleteCommandDTO;
import com.bitozen.hms.pm.common.dto.command.blacklist.BlacklistStateAndBlacklistStatusChangeCommandDTO;
import com.bitozen.hms.web.helper.PMAssembler;
import com.bitozen.hms.web.helper.BizparHelper;
import com.bitozen.hms.web.helper.EmployeeHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.netflix.hystrix.exception.HystrixTimeoutException;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.transaction.TransactionSystemException;

@Service
@Slf4j
public class BlacklistHystrixCommandService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    BizparHelper bizparHelper;

    @Autowired
    EmployeeHelper employeeHelper;

    @Autowired
    PMAssembler pmAssembler;

    private final CommandGateway commandGateway;

    @Autowired
    public BlacklistHystrixCommandService(CommandGateway commandGateway){
        this.commandGateway = commandGateway;
    }

    @HystrixCommand(fallbackMethod = "defaultPostBlacklistFallback")
    @Caching(
            evict = {
                    @CacheEvict(value = "findOneByBlacklistIDCache", allEntries = true),
                    @CacheEvict(value = "getAllBlacklistWebCache", allEntries = true)
            }
    )
    public GenericResponseDTO<BlacklistCreateCommandDTO> postBlacklist(BlacklistCreateCommandDTO dto) {
        GenericResponseDTO<BlacklistCreateCommandDTO> response = new GenericResponseDTO().successResponse();
        try {
            BlacklistCreateCommand command = new BlacklistCreateCommand(
                    dto.getBlacklistID(),
                    dto.getBlacklistSPKNumber(),
                    dto.getBlacklistStartDate(),
                    dto.getBlacklistEndDate(),
                    dto.getBlacklistNotes(),
                    objectMapper.writeValueAsString(bizparHelper.convertBizpar(dto.getBlacklistType())),
                    dto.getIsPermanent(),
                    objectMapper.writeValueAsString(employeeHelper.findEmployeeOptimizeByKey(dto.getEmployee())),
                    objectMapper.writeValueAsString(employeeHelper.findEmployeeOptimizeByKey(dto.getRequestor())),
                    dto.getBlacklistDocURL(),
                    dto.getIsFinalApprove(),
                    dto.getBlacklistStatus(),
                    dto.getBlacklistState(),
                    dto.getCompRegulationChapter(),
                    dto.getCompRegulationChapterDesc(),
                    objectMapper.writeValueAsString(pmAssembler.toMetadata(dto.getMetadata())),
                    objectMapper.writeValueAsString(dto.getToken()),
                    dto.getCreatedBy(),
                    dto.getCreatedDate(),
                    dto.getRecordID()
            );
            commandGateway.send(command, new CommandCallback<BlacklistCreateCommand, Object>() {
                @Override
                public void onResult(CommandMessage<? extends BlacklistCreateCommand> commandMessage, CommandResultMessage<?> commandResultMessage) {
                    if (commandResultMessage.isExceptional() == false) {
                        try {
                            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                                    ProjectType.CQRS, "Blacklist", new Date(), "Command", new GenericResponseDTO().successResponse().getCode(),
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
                                    ProjectType.CQRS, "Blacklist", new Date(), "Command", response.getCode(), commandResultMessage.exceptionResult().getStackTrace())));
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

    private GenericResponseDTO<BlacklistCreateCommandDTO> defaultPostBlacklistFallback(BlacklistCreateCommandDTO dto, Throwable e) throws IOException {
        return new GenericResponseDTO<BlacklistCreateCommandDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }

    @HystrixCommand(fallbackMethod = "defaultPutBlacklistFallback")
    @Caching(
            evict = {
                    @CacheEvict(value = "findOneByBlacklistIDCache", allEntries = true),
                    @CacheEvict(value = "getAllBlacklistWebCache", allEntries = true)
            }
    )
    public GenericResponseDTO<BlacklistChangeCommandDTO> putBlacklist(BlacklistChangeCommandDTO dto) {
        GenericResponseDTO<BlacklistChangeCommandDTO> response = new GenericResponseDTO().successResponse();
        try {
            BlacklistChangeCommand command = new BlacklistChangeCommand(
                    dto.getBlacklistID(),
                    dto.getBlacklistSPKNumber(),
                    dto.getBlacklistStartDate(),
                    dto.getBlacklistEndDate(),
                    dto.getBlacklistNotes(),
                    objectMapper.writeValueAsString(bizparHelper.convertBizpar(dto.getBlacklistType())),
                    dto.getIsPermanent(),
                    objectMapper.writeValueAsString(employeeHelper.findEmployeeOptimizeByKey(dto.getEmployee())),
                    objectMapper.writeValueAsString(employeeHelper.findEmployeeOptimizeByKey(dto.getRequestor())),
                    dto.getBlacklistDocURL(),
                    dto.getIsFinalApprove(),
                    dto.getBlacklistStatus(),
                    dto.getBlacklistState(),
                    dto.getCompRegulationChapter(),
                    dto.getCompRegulationChapterDesc(),
                    objectMapper.writeValueAsString(pmAssembler.toMetadata(dto.getMetadata())),
                    objectMapper.writeValueAsString(dto.getToken()),
                    dto.getUpdatedBy(),
                    dto.getUpdatedDate()
            );
            commandGateway.send(command, new CommandCallback<BlacklistChangeCommand, Object>() {
                @Override
                public void onResult(CommandMessage<? extends BlacklistChangeCommand> commandMessage, CommandResultMessage<?> commandResultMessage) {
                    if (commandResultMessage.isExceptional() == false) {
                        try {
                            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                                    ProjectType.CQRS, "Blacklist", new Date(), "Command", new GenericResponseDTO().successResponse().getCode(),
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
                                    ProjectType.CQRS, "Blacklist", new Date(), "Command", response.getCode(), commandResultMessage.exceptionResult().getStackTrace())));
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

    private GenericResponseDTO<BlacklistChangeCommandDTO> defaultPutBlacklistFallback(BlacklistChangeCommandDTO dto, Throwable e) throws IOException {
        return new GenericResponseDTO<BlacklistChangeCommandDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }

    @HystrixCommand(fallbackMethod = "defaultDeleteBlacklistFallback")
    @Caching(
            evict = {
                    @CacheEvict(value = "findOneByBlacklistIDCache", allEntries = true),
                    @CacheEvict(value = "getAllBlacklistWebCache", allEntries = true)
            }
    )
    public GenericResponseDTO<BlacklistDeleteCommandDTO> deleteBlacklist(BlacklistDeleteCommandDTO dto) {
        GenericResponseDTO<BlacklistDeleteCommandDTO> response = new GenericResponseDTO().successResponse();
        try {
            BlacklistDeleteCommand command = new BlacklistDeleteCommand(
                    dto.getBlacklistID(),
                    dto.getUpdatedBy()
            );
            commandGateway.send(command, new CommandCallback<BlacklistDeleteCommand, Object>() {
                @Override
                public void onResult(CommandMessage<? extends BlacklistDeleteCommand> commandMessage, CommandResultMessage<?> commandResultMessage) {
                    if (commandResultMessage.isExceptional() == false) {
                        try {
                            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                                    ProjectType.CQRS, "Blacklist", new Date(), "Command", new GenericResponseDTO().successResponse().getCode(),
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
                                    ProjectType.CQRS, "Blacklist", new Date(), "Command", response.getCode(), commandResultMessage.exceptionResult().getStackTrace())));
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

    private GenericResponseDTO<BlacklistDeleteCommandDTO> defaultDeleteBlacklistFallback(BlacklistDeleteCommandDTO dto, Throwable e) throws IOException {
        return new GenericResponseDTO<BlacklistDeleteCommandDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }
    
    @HystrixCommand(fallbackMethod = "defaultPutBlacklistStateAndStatusFallback")
    @Caching(
            evict = {
                    @CacheEvict(value = "findOneByBlacklistIDCache", allEntries = true),
                    @CacheEvict(value = "getAllBlacklistWebCache", allEntries = true)
            }
    )
    public GenericResponseDTO<BlacklistStateAndBlacklistStatusChangeCommandDTO> changeBlacklistStateAndStatus(BlacklistStateAndBlacklistStatusChangeCommandDTO dto) {
        GenericResponseDTO<BlacklistStateAndBlacklistStatusChangeCommandDTO> response = new GenericResponseDTO().successResponse();
        try {
            BlacklistStateAndStatusChangeCommand command = new BlacklistStateAndStatusChangeCommand(
                    dto.getBlacklistID().toUpperCase(),
                    dto.getBlacklistState(),
                    dto.getBlacklistStatus(),
                    dto.getIsFinalApprove(),
                    dto.getUpdatedBy() == null ? "SYSTEM" : dto.getUpdatedBy(),
                    dto.getUpdatedDate() == null ? new Date() : dto.getUpdatedDate());

            commandGateway.send(command, new CommandCallback<BlacklistStateAndStatusChangeCommand, Object>() {
                @Override
                public void onResult(CommandMessage<? extends BlacklistStateAndStatusChangeCommand> commandMessage, CommandResultMessage<?> commandResultMessage) {
                    if (commandResultMessage.isExceptional() == false) {
                        try {
                            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                                    ProjectType.CQRS, "Blacklist", new Date(), "Command", new GenericResponseDTO().successResponse().getCode(),
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
                                    ProjectType.CQRS, "Blacklist", new Date(), "Command", response.getCode(), commandResultMessage.exceptionResult().getStackTrace())));
                        } catch (JsonProcessingException ex) {
                            log.info(ex.getMessage());
                        }
                    }
                }
            });
            return response;
        } catch (Exception e) {
            log.info(e.getMessage());
            try {
                log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CQRS, "Blacklist", new Date(), "Command", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
            } catch (JsonProcessingException ex) {
                log.info(ex.getMessage());
            }

            if (e instanceof TransactionSystemException) {
                if (((TransactionSystemException) e).getRootCause() instanceof ConstraintViolationException) {
                    Set<ConstraintViolation<?>> data = ((ConstraintViolationException) ((TransactionSystemException) e).getRootCause()).getConstraintViolations();
                    String msg = new ArrayList<>(data).get(0).getMessage();
                    return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), msg);
                }
            }
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage());
        }
    }
    
    private GenericResponseDTO<BlacklistStateAndBlacklistStatusChangeCommandDTO> defaultPutBlacklistStateAndStatusFallback(BlacklistStateAndBlacklistStatusChangeCommandDTO dTO, Throwable e) throws IOException {
        return new GenericResponseDTO<BlacklistStateAndBlacklistStatusChangeCommandDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }
}
