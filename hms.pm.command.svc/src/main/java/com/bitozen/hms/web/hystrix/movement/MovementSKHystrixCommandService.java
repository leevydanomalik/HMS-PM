package com.bitozen.hms.web.hystrix.movement;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.status.ResponseStatus;
import com.bitozen.hms.common.type.ProjectType;
import com.bitozen.hms.common.util.LogOpsUtil;
import com.bitozen.hms.pm.command.movement.MovementSKChangeCommand;
import com.bitozen.hms.pm.command.movement.MovementSKCreateCommand;
import com.bitozen.hms.pm.command.movement.MovementSKDeleteCommand;
import com.bitozen.hms.pm.common.MVStatus;
import com.bitozen.hms.pm.common.dto.command.movement.*;
import com.bitozen.hms.pm.repository.movement.MovementRepository;
import com.bitozen.hms.projection.movement.MovementEntryProjection;
import com.bitozen.hms.web.assembler.MovementAssembler;
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
import java.util.Optional;

@Service
@Slf4j
public class MovementSKHystrixCommandService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    MovementAssembler movAssembler;

    @Autowired
    MovementRepository repository;

    private final CommandGateway commandGateway;

    @Autowired
    public MovementSKHystrixCommandService(CommandGateway commandGateway){
        this.commandGateway = commandGateway;
    }

    @HystrixCommand(fallbackMethod = "defaultPostMovementSKFallback")
    @Caching(
            evict = {
                    @CacheEvict(value = "findOneByMovementIDCache", allEntries = true),
                    @CacheEvict(value = "getAllMovementWebCache", allEntries = true)
            }
    )
    public GenericResponseDTO<MVSKCreateCommandDTO> postMovementSK(MVSKCreateCommandDTO dto) {
        GenericResponseDTO<MVSKCreateCommandDTO> response = new GenericResponseDTO().successResponse();
        try {
            MovementSKCreateCommand command = new MovementSKCreateCommand(
                    dto.getMvID(),
                    objectMapper.writeValueAsString(movAssembler.toSKDTOPostRequest(dto))
            );
            commandGateway.send(command, new CommandCallback<MovementSKCreateCommand, Object>() {
                @Override
                public void onResult(CommandMessage<? extends MovementSKCreateCommand> commandMessage, CommandResultMessage<?> commandResultMessage) {
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

    private GenericResponseDTO<MVSKCreateCommandDTO> defaultPostMovementSKFallback(MVSKCreateCommandDTO dto, Throwable e) throws IOException {
        return new GenericResponseDTO<MVSKCreateCommandDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }

    @HystrixCommand(fallbackMethod = "defaultPutMovementSKFallback")
    @Caching(
            evict = {
                    @CacheEvict(value = "findOneByMovementIDCache", allEntries = true),
                    @CacheEvict(value = "getAllMovementWebCache", allEntries = true)
            }
    )
    public GenericResponseDTO<MVSKCreateCommandDTO> putMovementSK(MVSKCreateCommandDTO dto) {
        GenericResponseDTO<MVSKCreateCommandDTO> response = new GenericResponseDTO().successResponse();
        try {
            MovementSKChangeCommand command = new MovementSKChangeCommand(
                    dto.getMvID(),
                    objectMapper.writeValueAsString(movAssembler.toSKDTOPostRequest(dto))
            );
            commandGateway.send(command, new CommandCallback<MovementSKChangeCommand, Object>() {
                @Override
                public void onResult(CommandMessage<? extends MovementSKChangeCommand> commandMessage, CommandResultMessage<?> commandResultMessage) {
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

    private GenericResponseDTO<MVSKCreateCommandDTO> defaultPutMovementSKFallback(MVSKCreateCommandDTO dto, Throwable e) throws IOException {
        return new GenericResponseDTO<MVSKCreateCommandDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }

    @HystrixCommand(fallbackMethod = "defaultDeleteMovementSKFallback")
    @Caching(
            evict = {
                    @CacheEvict(value = "findOneByMovementIDCache", allEntries = true),
                    @CacheEvict(value = "getAllMovementWebCache", allEntries = true)
            }
    )
    public GenericResponseDTO<MVSKDeleteCommandDTO> deleteMovementSK(MVSKDeleteCommandDTO dto) {
        GenericResponseDTO<MVSKDeleteCommandDTO> response = new GenericResponseDTO().successResponse();
        Optional<MovementEntryProjection> movement = repository.findOneBySkID(dto.getSkID(), MVStatus.INACTIVE.name());
        if(movement.isPresent()) {
            try {
                MovementSKDeleteCommand command = new MovementSKDeleteCommand(
                        movement.get().getMvID(),
                        objectMapper.writeValueAsString(movAssembler.toSKDTODeleteRequest(dto))
                );
                commandGateway.send(command, new CommandCallback<MovementSKDeleteCommand, Object>() {
                    @Override
                    public void onResult(CommandMessage<? extends MovementSKDeleteCommand> commandMessage, CommandResultMessage<?> commandResultMessage) {
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
        } else {
            try {
                log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(LogOpsUtil.getLogResponse(
                        ProjectType.CQRS, "Movement", new Date(), "Query", new GenericResponseDTO().noDataFoundResponse().getCode(),
                        new GenericResponseDTO().noDataFoundResponse().getMessage())));
                return new GenericResponseDTO().noDataFoundResponse();
            } catch(Exception e) {
                log.info(e.getMessage());
            }
        }
        return response;
    }

    private GenericResponseDTO<MVSKDeleteCommandDTO> defaultDeleteMovementSKFallback(MVSKDeleteCommandDTO dto, Throwable e) throws IOException {
        return new GenericResponseDTO<MVSKDeleteCommandDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }
}
