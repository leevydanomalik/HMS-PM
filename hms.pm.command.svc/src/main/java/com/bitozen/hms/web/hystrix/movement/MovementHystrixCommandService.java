package com.bitozen.hms.web.hystrix.movement;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.status.ResponseStatus;
import com.bitozen.hms.common.type.ProjectType;
import com.bitozen.hms.common.util.LogOpsUtil;
import com.bitozen.hms.pm.command.movement.MovementChangeCommand;
import com.bitozen.hms.pm.command.movement.MovementCreateCommand;
import com.bitozen.hms.pm.command.movement.MovementDeleteCommand;
import com.bitozen.hms.pm.common.dto.command.movement.MovementChangeCommandDTO;
import com.bitozen.hms.pm.common.dto.command.movement.MovementCreateCommandDTO;
import com.bitozen.hms.pm.common.dto.command.movement.MovementDeleteCommandDTO;
import com.bitozen.hms.web.assembler.MovementAssembler;
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
import java.util.Date;

@Service
@Slf4j
public class MovementHystrixCommandService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    BizparHelper bizparHelper;

    @Autowired
    EmployeeHelper employeeHelper;

    @Autowired
    PMAssembler pmAssembler;

    @Autowired
    MovementAssembler movAssembler;

    private final CommandGateway commandGateway;

    @Autowired
    public MovementHystrixCommandService(CommandGateway commandGateway){
        this.commandGateway = commandGateway;
    }

    @HystrixCommand(fallbackMethod = "defaultPostMovementFallback")
    @Caching(
            evict = {
                    @CacheEvict(value = "findOneByMovementIDCache", allEntries = true),
                    @CacheEvict(value = "getAllMovementWebCache", allEntries = true)
            }
    )
    public GenericResponseDTO<MovementCreateCommandDTO> postMovement(MovementCreateCommandDTO dto) {
        GenericResponseDTO<MovementCreateCommandDTO> response = new GenericResponseDTO().successResponse();
        try {
            MovementCreateCommand command = new MovementCreateCommand(
                    dto.getMvID(),
                    dto.getMvNotes(),
                    dto.getMvSPKDocNumber(),
                    dto.getMvEffectiveDate(),
                    dto.getIsFinalApprove(),
                    dto.getMvStatus(),
                    dto.getMvState(),
                    objectMapper.writeValueAsString(bizparHelper.convertBizpar(dto.getMvCase())),
                    objectMapper.writeValueAsString(bizparHelper.convertBizpar(dto.getMvType())),
                    objectMapper.writeValueAsString(employeeHelper.findEmployeeOptimizeByKey(dto.getRequestor())),
                    objectMapper.writeValueAsString(movAssembler.toAssignmentDTO(dto.getAssignment())),
                    objectMapper.writeValueAsString(movAssembler.toMVEmployeeDTOs(dto.getEmployees())),
                    objectMapper.writeValueAsString(pmAssembler.convertDocuments(dto.getMvDocs())),
                    objectMapper.writeValueAsString(movAssembler.toBenefitDTO(dto.getMvBenefitBefore())),
                    objectMapper.writeValueAsString(movAssembler.toBenefitDTO(dto.getMvBenefitAfter())),
                    objectMapper.writeValueAsString(movAssembler.toFacilityDTO(dto.getMvFacilityBefore())),
                    objectMapper.writeValueAsString(movAssembler.toFacilityDTO(dto.getMvFacilityAfter())),
                    objectMapper.writeValueAsString(dto.getMvPayroll()),
                    objectMapper.writeValueAsString(movAssembler.toPositionDTO(dto.getMvPosition())),
                    objectMapper.writeValueAsString(movAssembler.toRecRequestDTO(dto.getRefRecRequest())),
                    objectMapper.writeValueAsString(pmAssembler.toMetadata(dto.getMetadata())),
                    objectMapper.writeValueAsString(dto.getToken()),
                    dto.getCreatedBy(),
                    dto.getCreatedDate(),
                    dto.getRecordID()
            );
            commandGateway.send(command, new CommandCallback<MovementCreateCommand, Object>() {
                @Override
                public void onResult(CommandMessage<? extends MovementCreateCommand> commandMessage, CommandResultMessage<?> commandResultMessage) {
                    if (commandResultMessage.isExceptional() == false) {
                        try {
                            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                                    ProjectType.CQRS, "Movement", new Date(), "Command", new GenericResponseDTO().successResponse().getCode(),
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
                                    ProjectType.CQRS, "Movement", new Date(), "Command", response.getCode(), commandResultMessage.exceptionResult().getStackTrace())));
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

    private GenericResponseDTO<MovementCreateCommandDTO> defaultPostMovementFallback(MovementCreateCommandDTO dto, Throwable e) throws IOException {
        return new GenericResponseDTO<MovementCreateCommandDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }

    @HystrixCommand(fallbackMethod = "defaultPutMovementFallback")
    @Caching(
            evict = {
                    @CacheEvict(value = "findOneByMovementIDCache", allEntries = true),
                    @CacheEvict(value = "getAllMovementWebCache", allEntries = true)
            }
    )
    public GenericResponseDTO<MovementChangeCommandDTO> putMovement(MovementChangeCommandDTO dto) {
        GenericResponseDTO<MovementChangeCommandDTO> response = new GenericResponseDTO().successResponse();
        try {
            MovementChangeCommand command = new MovementChangeCommand(
                    dto.getMvID(),
                    dto.getMvNotes(),
                    dto.getMvSPKDocNumber(),
                    dto.getMvEffectiveDate(),
                    dto.getIsFinalApprove(),
                    dto.getMvStatus(),
                    dto.getMvState(),
                    objectMapper.writeValueAsString(bizparHelper.convertBizpar(dto.getMvCase())),
                    objectMapper.writeValueAsString(bizparHelper.convertBizpar(dto.getMvType())),
                    objectMapper.writeValueAsString(employeeHelper.findEmployeeOptimizeByKey(dto.getRequestor())),
                    objectMapper.writeValueAsString(movAssembler.toAssignmentDTO(dto.getAssignment())),
                    objectMapper.writeValueAsString(movAssembler.toMVEmployeeDTOs(dto.getEmployees())),
                    objectMapper.writeValueAsString(pmAssembler.convertDocuments(dto.getMvDocs())),
                    objectMapper.writeValueAsString(movAssembler.toBenefitDTO(dto.getMvBenefitBefore())),
                    objectMapper.writeValueAsString(movAssembler.toBenefitDTO(dto.getMvBenefitAfter())),
                    objectMapper.writeValueAsString(movAssembler.toFacilityDTO(dto.getMvFacilityBefore())),
                    objectMapper.writeValueAsString(movAssembler.toFacilityDTO(dto.getMvFacilityAfter())),
                    objectMapper.writeValueAsString(dto.getMvPayroll()),
                    objectMapper.writeValueAsString(movAssembler.toPositionDTO(dto.getMvPosition())),
                    objectMapper.writeValueAsString(movAssembler.toRecRequestDTO(dto.getRefRecRequest())),
                    objectMapper.writeValueAsString(pmAssembler.toMetadata(dto.getMetadata())),
                    objectMapper.writeValueAsString(dto.getToken()),
                    dto.getUpdatedBy(),
                    dto.getUpdatedDate()
            );
            commandGateway.send(command, new CommandCallback<MovementChangeCommand, Object>() {
                @Override
                public void onResult(CommandMessage<? extends MovementChangeCommand> commandMessage, CommandResultMessage<?> commandResultMessage) {
                    if (commandResultMessage.isExceptional() == false) {
                        try {
                            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                                    ProjectType.CQRS, "Movement", new Date(), "Command", new GenericResponseDTO().successResponse().getCode(),
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
                                    ProjectType.CQRS, "Movement", new Date(), "Command", response.getCode(), commandResultMessage.exceptionResult().getStackTrace())));
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

    private GenericResponseDTO<MovementChangeCommandDTO> defaultPutMovementFallback(MovementChangeCommandDTO dto, Throwable e) throws IOException {
        return new GenericResponseDTO<MovementChangeCommandDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }

    @HystrixCommand(fallbackMethod = "defaultDeleteMovementFallback")
    @Caching(
            evict = {
                    @CacheEvict(value = "findOneByMovementIDCache", allEntries = true),
                    @CacheEvict(value = "getAllMovementWebCache", allEntries = true)
            }
    )
    public GenericResponseDTO<MovementDeleteCommandDTO> deleteMovement(MovementDeleteCommandDTO dto) {
        GenericResponseDTO<MovementDeleteCommandDTO> response = new GenericResponseDTO().successResponse();
        try {
            MovementDeleteCommand command = new MovementDeleteCommand(
                    dto.getMvID(),
                    dto.getUpdatedBy()
            );
            commandGateway.send(command, new CommandCallback<MovementDeleteCommand, Object>() {
                @Override
                public void onResult(CommandMessage<? extends MovementDeleteCommand> commandMessage, CommandResultMessage<?> commandResultMessage) {
                    if (commandResultMessage.isExceptional() == false) {
                        try {
                            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                                    ProjectType.CQRS, "Movement", new Date(), "Command", new GenericResponseDTO().successResponse().getCode(),
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
                                    ProjectType.CQRS, "Movement", new Date(), "Command", response.getCode(), commandResultMessage.exceptionResult().getStackTrace())));
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

    private GenericResponseDTO<MovementDeleteCommandDTO> defaultDeleteMovementFallback(MovementDeleteCommandDTO dto, Throwable e) throws IOException {
        return new GenericResponseDTO<MovementDeleteCommandDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }
}
