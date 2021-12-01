package com.bitozen.hms.web.hystrix.employmentletter;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.status.ResponseStatus;
import com.bitozen.hms.common.type.ProjectType;
import com.bitozen.hms.common.util.LogOpsUtil;
import com.bitozen.hms.pm.command.employmentletter.EmploymentLetterChangeCommand;
import com.bitozen.hms.pm.command.employmentletter.EmploymentLetterCreateCommand;
import com.bitozen.hms.pm.command.employmentletter.EmploymentLetterDeleteCommand;
import com.bitozen.hms.pm.command.employmentletter.EmploymentLetterStateAndEmploymentLetterStatusChangeCommand;
import com.bitozen.hms.pm.common.dto.command.employmentletter.EmploymentLetterChangeCommandDTO;
import com.bitozen.hms.pm.common.dto.command.employmentletter.EmploymentLetterCreateCommandDTO;
import com.bitozen.hms.pm.common.dto.command.employmentletter.EmploymentLetterDeleteCommandDTO;
import com.bitozen.hms.pm.common.dto.command.employmentletter.EmploymentLetterStateAndEmploymentLetterStatusChangeCommandDTO;
import com.bitozen.hms.web.helper.BizparHelper;
import com.bitozen.hms.web.helper.EmployeeHelper;
import com.bitozen.hms.web.helper.EmploymentLetterHelper;
import com.bitozen.hms.web.helper.PMAssembler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.netflix.hystrix.exception.HystrixTimeoutException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
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
import org.springframework.transaction.TransactionSystemException;

/**
 *
 * @author Jeremia
 */
@Service
@Slf4j
public class EmploymentLetterHystrixCommandService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    BizparHelper bizparHelper;

    @Autowired
    private EmploymentLetterHelper employmentLetterHelper;

    @Autowired
    EmployeeHelper employeeHelper;

    @Autowired
    PMAssembler pmAssembler;

    private final CommandGateway commandGateway;

    @Autowired
    public EmploymentLetterHystrixCommandService(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;

    }

    @HystrixCommand(fallbackMethod = "defaultPostElFallback")
    @Caching(
            evict = {
                @CacheEvict(value = "findOneByElIDCache", allEntries = true),
                @CacheEvict(value = "findOneByElDocNumberCache", allEntries = true),
                @CacheEvict(value = "getAllElWebCache", allEntries = true),
                @CacheEvict(value = "getAllEmploymentLetterWebESSCache", allEntries = true)

            }
    )
    public GenericResponseDTO<EmploymentLetterCreateCommandDTO> postEmploymentLetter(EmploymentLetterCreateCommandDTO dto) {
        GenericResponseDTO<EmploymentLetterCreateCommandDTO> response = new GenericResponseDTO().successResponse();
        try {
            EmploymentLetterCreateCommand command = new EmploymentLetterCreateCommand(
                    dto.getElID(),
                    dto.getElDocNumber(),
                    dto.getElDocURL(),
                    dto.getElState(),
                    dto.getElStatus(),
                    dto.getIsFinalApproval(),
                    dto.getReason(),
                    dto.getReqDate(),
                    objectMapper.writeValueAsString(bizparHelper.convertBizpar(dto.getReqType())),
                    objectMapper.writeValueAsString(employeeHelper.findEmployeeOptimizeByKey(dto.getRequestor())),
                    objectMapper.writeValueAsString(employmentLetterHelper.toVisaSpecification(dto.getVisaSpec())),
                    objectMapper.writeValueAsString(pmAssembler.toMetadata(dto.getMetadata())),
                    objectMapper.writeValueAsString(dto.getToken()),
                    dto.getCreatedBy(),
                    dto.getCreatedDate(),
                    dto.getRecordID()
            );

            commandGateway.send(command, new CommandCallback<EmploymentLetterCreateCommand, Object>() {
                @Override
                public void onResult(CommandMessage<? extends EmploymentLetterCreateCommand> commandMessage, CommandResultMessage<?> commandResultMessage) {
                    if (commandResultMessage.isExceptional() == false) {
                        try {
                            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                                    ProjectType.CQRS, "Employment Letter", new Date(), "Command", new GenericResponseDTO().successResponse().getCode(),
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
                                    ProjectType.CQRS, "Employment Letter", new Date(), "Command", response.getCode(), commandResultMessage.exceptionResult().getStackTrace())));
                        } catch (JsonProcessingException ex) {
                            log.info(ex.getMessage());
                        }
                    }
                }
            });
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return response;
    }

    private GenericResponseDTO<EmploymentLetterCreateCommandDTO> defaultPostElFallback(EmploymentLetterCreateCommandDTO dto, Throwable e) throws IOException {
        return new GenericResponseDTO<EmploymentLetterCreateCommandDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }

    @HystrixCommand(fallbackMethod = "defaultPutElFallback")
    @Caching(
            evict = {
                @CacheEvict(value = "findOneByElIDCache", allEntries = true),
                @CacheEvict(value = "findOneByElDocNumberCache", allEntries = true),
                @CacheEvict(value = "getAllElWebCache", allEntries = true),
                @CacheEvict(value = "getAllEmploymentLetterWebESSCache", allEntries = true)
            }
    )
    public GenericResponseDTO<EmploymentLetterChangeCommandDTO> putEmploymentLetter(EmploymentLetterChangeCommandDTO dto) {
        GenericResponseDTO<EmploymentLetterChangeCommandDTO> response = new GenericResponseDTO().successResponse();
        try {
            EmploymentLetterChangeCommand command = new EmploymentLetterChangeCommand(
                    dto.getElID(),
                    dto.getElDocNumber(),
                    dto.getElDocURL(),
                    dto.getElState(),
                    dto.getElStatus(),
                    dto.getIsFinalApproval(),
                    dto.getReason(),
                    dto.getReqDate(),
                    objectMapper.writeValueAsString(bizparHelper.convertBizpar(dto.getReqType())),
                    objectMapper.writeValueAsString(employeeHelper.findEmployeeOptimizeByKey(dto.getRequestor())),
                    objectMapper.writeValueAsString(employmentLetterHelper.toVisaSpecification(dto.getVisaSpec())),
                    objectMapper.writeValueAsString(pmAssembler.toMetadata(dto.getMetadata())),
                    objectMapper.writeValueAsString(dto.getToken()),
                    dto.getUpdatedBy(),
                    dto.getUpdatedDate()
            );
            commandGateway.send(command, new CommandCallback<EmploymentLetterChangeCommand, Object>() {
                @Override
                public void onResult(CommandMessage<? extends EmploymentLetterChangeCommand> commandMessage, CommandResultMessage<?> commandResultMessage) {
                    if (commandResultMessage.isExceptional() == false) {
                        try {
                            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                                    ProjectType.CQRS, "Employment Letter", new Date(), "Command", new GenericResponseDTO().successResponse().getCode(),
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
                                    ProjectType.CQRS, "Employment Letter", new Date(), "Command", response.getCode(), commandResultMessage.exceptionResult().getStackTrace())));
                        } catch (JsonProcessingException ex) {
                            log.info(ex.getMessage());
                        }
                    }
                }
            });
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return response;
    }

    private GenericResponseDTO<EmploymentLetterChangeCommandDTO> defaultPutElFallback(EmploymentLetterChangeCommandDTO dto, Throwable e) throws IOException {
        return new GenericResponseDTO<EmploymentLetterChangeCommandDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }

    @HystrixCommand(fallbackMethod = "defaultPutElStateAndElStatusFallback")
    @Caching(
            evict = {
                @CacheEvict(value = "findOneByElIDCache", allEntries = true),
                @CacheEvict(value = "findOneByElDocNumberCache", allEntries = true),
                @CacheEvict(value = "getAllElWebCache", allEntries = true),
                @CacheEvict(value = "getAllEmploymentLetterWebESSCache", allEntries = true)

            }
    )
    public GenericResponseDTO<EmploymentLetterStateAndEmploymentLetterStatusChangeCommandDTO> putEmploymentLetterStateAndEmploymentLetterStatus(EmploymentLetterStateAndEmploymentLetterStatusChangeCommandDTO dto) {
        GenericResponseDTO<EmploymentLetterStateAndEmploymentLetterStatusChangeCommandDTO> response = new GenericResponseDTO().successResponse();
        try {
            EmploymentLetterStateAndEmploymentLetterStatusChangeCommand command = new EmploymentLetterStateAndEmploymentLetterStatusChangeCommand(
                    dto.getElID().toUpperCase(),
                    dto.getElState(),
                    dto.getElStatus(),
                    dto.getIsFinalApproval(),
                    dto.getUpdatedBy() == null ? "SYSTEM" : dto.getUpdatedBy(),
                    dto.getUpdatedDate() == null ? new Date() : dto.getUpdatedDate());

            commandGateway.send(command, new CommandCallback<EmploymentLetterStateAndEmploymentLetterStatusChangeCommand, Object>() {
                @Override
                public void onResult(CommandMessage<? extends EmploymentLetterStateAndEmploymentLetterStatusChangeCommand> commandMessage, CommandResultMessage<?> commandResultMessage) {
                    if (commandResultMessage.isExceptional() == false) {
                        try {
                            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                                    ProjectType.CQRS, "Employment Letter", new Date(), "Command", new GenericResponseDTO().successResponse().getCode(),
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
                                    ProjectType.CQRS, "Employment Letter", new Date(), "Command", response.getCode(), commandResultMessage.exceptionResult().getStackTrace())));
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
                        LogOpsUtil.getErrorResponse(ProjectType.CQRS, "Employment Letter", new Date(), "Command", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
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

    private GenericResponseDTO<EmploymentLetterStateAndEmploymentLetterStatusChangeCommandDTO> defaultPutElStateAndElStatusFallback(EmploymentLetterStateAndEmploymentLetterStatusChangeCommandDTO dTO, Throwable e) throws IOException {
        return new GenericResponseDTO<EmploymentLetterStateAndEmploymentLetterStatusChangeCommandDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }

    @HystrixCommand(fallbackMethod = "defaultDeleteElFallback")
    @Caching(
            evict = {
                @CacheEvict(value = "findOneByElIDCache", allEntries = true),
                @CacheEvict(value = "findOneByElDocNumberCache", allEntries = true),
                @CacheEvict(value = "getAllElWebCache", allEntries = true),
                @CacheEvict(value = "getAllEmploymentLetterWebESSCache", allEntries = true)
            }
    )
    public GenericResponseDTO<EmploymentLetterDeleteCommandDTO> deleteEmploymentLetter(EmploymentLetterDeleteCommandDTO dto) {
        GenericResponseDTO<EmploymentLetterDeleteCommandDTO> response = new GenericResponseDTO().successResponse();
        try {
            EmploymentLetterDeleteCommand command = new EmploymentLetterDeleteCommand(
                    dto.getElID(),
                    dto.getUpdatedBy()
            );
            commandGateway.send(command, new CommandCallback<EmploymentLetterDeleteCommand, Object>() {
                @Override
                public void onResult(CommandMessage<? extends EmploymentLetterDeleteCommand> commandMessage, CommandResultMessage<?> commandResultMessage) {
                    if (commandResultMessage.isExceptional() == false) {
                        try {
                            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                                    ProjectType.CQRS, "Employment Letter", new Date(), "Command", new GenericResponseDTO().successResponse().getCode(),
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
                                    ProjectType.CQRS, "Employment Letter", new Date(), "Command", response.getCode(), commandResultMessage.exceptionResult().getStackTrace())));
                        } catch (JsonProcessingException ex) {
                            log.info(ex.getMessage());
                        }
                    }
                }
            });
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return response;
    }

    private GenericResponseDTO<EmploymentLetterDeleteCommandDTO> defaultDeleteElFallback(EmploymentLetterDeleteCommandDTO dto, Throwable e) throws IOException {
        return new GenericResponseDTO<EmploymentLetterDeleteCommandDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }

}
