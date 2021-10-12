package com.bitozen.hms.web.hystrix.termination;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.status.ResponseStatus;
import com.bitozen.hms.common.type.ProjectType;
import com.bitozen.hms.common.util.LogOpsUtil;
import com.bitozen.hms.pm.command.termination.TerminationChangeCommand;
import com.bitozen.hms.pm.command.termination.TerminationCreateCommand;
import com.bitozen.hms.pm.command.termination.TerminationDeleteCommand;
import com.bitozen.hms.pm.common.dto.command.termination.TerminationChangeCommandDTO;
import com.bitozen.hms.pm.common.dto.command.termination.TerminationCreateCommandDTO;
import com.bitozen.hms.pm.common.dto.command.termination.TerminationDeleteCommandDTO;
import com.bitozen.hms.web.helper.AssemblerHelper;
import com.bitozen.hms.web.helper.BizparHelper;
import com.bitozen.hms.web.helper.EmployeeHelper;
import com.bitozen.hms.web.helper.TerminationHelper;
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



@Service
@Slf4j
public class TerminationHystrixCommandService {
    
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    BizparHelper bizparHelper;

    @Autowired
    EmployeeHelper employeeHelper;

    @Autowired
    AssemblerHelper assemblerHelper;
    
    @Autowired
    TerminationHelper terminationHelper;
    
    private final CommandGateway commandGateway;
    
    @Autowired
    public TerminationHystrixCommandService(CommandGateway commandGateway){
        this.commandGateway = commandGateway;
    }
    
    @HystrixCommand(fallbackMethod = "defaultPostTerminationFallback")
    @Caching(
            evict = {
                    @CacheEvict(value = "findOneByTerminationIDCache", allEntries = true),
                    @CacheEvict(value = "getAllTerminationWebCache", allEntries = true),
                    @CacheEvict(value = "generateTerminationDocumentByteCache", allEntries = true)
            }
    )
    public GenericResponseDTO<TerminationCreateCommandDTO> postTermination(TerminationCreateCommandDTO dto) {
        GenericResponseDTO<TerminationCreateCommandDTO> response = new GenericResponseDTO().successResponse();
        try {
            TerminationCreateCommand command = new TerminationCreateCommand(
                    dto.getTmnID(),
                    dto.getTmnNotes(),
                    dto.getBpjsHTDocNumber(),
                    dto.getBpjsPensionDocNumber(),
                    objectMapper.writeValueAsString(dto.getDocCopies()),
                    dto.getIsCancelFinalApprove(),
                    dto.getIsExecuted(),
                    dto.getIsFinalApprove(),
                    dto.getMemoDocNumber(),
                    objectMapper.writeValueAsString(employeeHelper.findEmployeeOptimizeByKey(dto.getEmployee())),
                    objectMapper.writeValueAsString(employeeHelper.findEmployeeOptimizeByKey(dto.getRequestor())),
                    dto.getSkDocNumber(),
                    dto.getSkdtDocNumber(),
                    objectMapper.writeValueAsString(terminationHelper.toDocumentDTOs(dto.getTmnDocs())),
                    dto.getTmnEffectiveDate(),
                    dto.getTmnReqDate(),
                    dto.getTmnPphEndDate(),
                    objectMapper.writeValueAsString(bizparHelper.convertBizpar(dto.getTmnReason())),
                    dto.getTmnState(),
                    dto.getTmnStatus(),
                    objectMapper.writeValueAsString(dto.getTmnPension()),
                    objectMapper.writeValueAsString(assemblerHelper.toMetadata(dto.getMetadata())),
                    objectMapper.writeValueAsString(dto.getToken()),
                    dto.getCreatedBy(),
                    dto.getCreatedDate(),
                    dto.getRecordID()
            );
            commandGateway.send(command, new CommandCallback<TerminationCreateCommand, Object>() {
                @Override
                public void onResult(CommandMessage<? extends TerminationCreateCommand> commandMessage, CommandResultMessage<?> commandResultMessage) {
                    if (commandResultMessage.isExceptional() == false) {
                        try {
                            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                                    ProjectType.CQRS, "Termination", new Date(), "Command", new GenericResponseDTO().successResponse().getCode(),
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
                                    ProjectType.CQRS, "Termination", new Date(), "Command", response.getCode(), commandResultMessage.exceptionResult().getStackTrace())));
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

    private GenericResponseDTO<TerminationCreateCommandDTO> defaultPostTerminationFallback(TerminationCreateCommandDTO dto, Throwable e) throws IOException {
        return new GenericResponseDTO<TerminationCreateCommandDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }
    
    
    @HystrixCommand(fallbackMethod = "defaultPutTerminationFallback")
    @Caching(
            evict = {
                    @CacheEvict(value = "findOneByTerminationIDCache", allEntries = true),
                    @CacheEvict(value = "getAllTerminationWebCache", allEntries = true),
                    @CacheEvict(value = "generateTerminationDocumentByteCache", allEntries = true)
            }
    )
    public GenericResponseDTO<TerminationChangeCommandDTO> putTermination(TerminationChangeCommandDTO dto) {
        GenericResponseDTO<TerminationChangeCommandDTO> response = new GenericResponseDTO().successResponse();
        try {
            TerminationChangeCommand command = new TerminationChangeCommand(
                    dto.getTmnID(),
                    dto.getTmnNotes(),
                    dto.getBpjsHTDocNumber(),
                    dto.getBpjsPensionDocNumber(),
                    objectMapper.writeValueAsString(dto.getDocCopies()),
                    dto.getIsCancelFinalApprove(),
                    dto.getIsExecuted(),
                    dto.getIsFinalApprove(),
                    dto.getMemoDocNumber(),
                    objectMapper.writeValueAsString(employeeHelper.findEmployeeOptimizeByKey(dto.getEmployee())),
                    objectMapper.writeValueAsString(employeeHelper.findEmployeeOptimizeByKey(dto.getRequestor())),
                    dto.getSkDocNumber(),
                    dto.getSkdtDocNumber(),
                    objectMapper.writeValueAsString(terminationHelper.toDocumentDTOs(dto.getTmnDocs())),
                    dto.getTmnEffectiveDate(),
                    dto.getTmnReqDate(),
                    dto.getTmnPphEndDate(),
                    objectMapper.writeValueAsString(bizparHelper.convertBizpar(dto.getTmnReason())),
                    dto.getTmnState(),
                    dto.getTmnStatus(),
                    objectMapper.writeValueAsString(dto.getTmnPension()),
                    objectMapper.writeValueAsString(assemblerHelper.toMetadata(dto.getMetadata())),
                    objectMapper.writeValueAsString(dto.getToken()),
                    dto.getUpdatedBy(),
                    dto.getUpdatedDate()
            );
            commandGateway.send(command, new CommandCallback<TerminationChangeCommand, Object>() {
                @Override
                public void onResult(CommandMessage<? extends TerminationChangeCommand> commandMessage, CommandResultMessage<?> commandResultMessage) {
                    if (commandResultMessage.isExceptional() == false) {
                        try {
                            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                                    ProjectType.CQRS, "Termination", new Date(), "Command", new GenericResponseDTO().successResponse().getCode(),
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
                                    ProjectType.CQRS, "Termination", new Date(), "Command", response.getCode(), commandResultMessage.exceptionResult().getStackTrace())));
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

    private GenericResponseDTO<TerminationChangeCommandDTO> defaultPutTerminationFallback(TerminationChangeCommandDTO dto, Throwable e) throws IOException {
        return new GenericResponseDTO<TerminationChangeCommandDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }
    
     @HystrixCommand(fallbackMethod = "defaultDeleteTerminationFallback")
    @Caching(
            evict = {
                    @CacheEvict(value = "findOneByTerminationIDCache", allEntries = true),
                    @CacheEvict(value = "getAllTerminationWebCache", allEntries = true),
                    @CacheEvict(value = "generateTerminationDocumentByteCache", allEntries = true)
            }
    )
    public GenericResponseDTO<TerminationDeleteCommandDTO> deleteTermination(TerminationDeleteCommandDTO dto) {
        GenericResponseDTO<TerminationDeleteCommandDTO> response = new GenericResponseDTO().successResponse();
        try {
            TerminationDeleteCommand command = new TerminationDeleteCommand(
                    dto.getTmnID(),
                    dto.getUpdatedBy()
            );
            commandGateway.send(command, new CommandCallback<TerminationDeleteCommand, Object>() {
                @Override
                public void onResult(CommandMessage<? extends TerminationDeleteCommand> commandMessage, CommandResultMessage<?> commandResultMessage) {
                    if (commandResultMessage.isExceptional() == false) {
                        try {
                            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                                    ProjectType.CQRS, "Termination", new Date(), "Command", new GenericResponseDTO().successResponse().getCode(),
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
                                    ProjectType.CQRS, "Termination", new Date(), "Command", response.getCode(), commandResultMessage.exceptionResult().getStackTrace())));
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

    private GenericResponseDTO<TerminationDeleteCommandDTO> defaultDeleteTerminationFallback(TerminationDeleteCommandDTO dto, Throwable e) throws IOException {
        return new GenericResponseDTO<TerminationDeleteCommandDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }
    
    
    
}
