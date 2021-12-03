package com.bitozen.hms.web.hystrix.movement;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.status.ResponseStatus;
import com.bitozen.hms.common.type.ProjectType;
import com.bitozen.hms.common.util.LogOpsUtil;
import com.bitozen.hms.pm.command.movement.*;
import com.bitozen.hms.pm.common.MVStatus;
import com.bitozen.hms.pm.common.dto.command.movement.MVMemoChangeStateAndStatusCommandDTO;
import com.bitozen.hms.pm.common.dto.command.movement.MVMemoCreateCommandDTO;
import com.bitozen.hms.pm.common.dto.command.movement.MVMemoDeleteCommandDTO;
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
public class MovementMemoHystrixCommandService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    MovementAssembler movAssembler;

    @Autowired
    MovementRepository repository;

    private final CommandGateway commandGateway;

    @Autowired
    public MovementMemoHystrixCommandService(CommandGateway commandGateway){
        this.commandGateway = commandGateway;
    }

    @HystrixCommand(fallbackMethod = "defaultPostMovementMemoFallback")
    @Caching(
            evict = {
                    @CacheEvict(value = "findOneByMovementIDCache", allEntries = true),
                    @CacheEvict(value = "getAllMovementWebCache", allEntries = true)
            }
    )
    public GenericResponseDTO<MVMemoCreateCommandDTO> postMovementMemo(MVMemoCreateCommandDTO dto) {
        GenericResponseDTO<MVMemoCreateCommandDTO> response = new GenericResponseDTO().successResponse();
        try {
            MovementMemoCreateCommand command = new MovementMemoCreateCommand(
                    dto.getMvID(),
                    objectMapper.writeValueAsString(movAssembler.toMemoDTOPostRequest(dto))
            );
            commandGateway.send(command, new CommandCallback<MovementMemoCreateCommand, Object>() {
                @Override
                public void onResult(CommandMessage<? extends MovementMemoCreateCommand> commandMessage, CommandResultMessage<?> commandResultMessage) {
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

    private GenericResponseDTO<MVMemoCreateCommandDTO> defaultPostMovementMemoFallback(MVMemoCreateCommandDTO dto, Throwable e) throws IOException {
        return new GenericResponseDTO<MVMemoCreateCommandDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }

    @HystrixCommand(fallbackMethod = "defaultPutMovementMemoFallback")
    @Caching(
            evict = {
                    @CacheEvict(value = "findOneByMovementIDCache", allEntries = true),
                    @CacheEvict(value = "getAllMovementWebCache", allEntries = true)
            }
    )
    public GenericResponseDTO<MVMemoCreateCommandDTO> putMovementMemo(MVMemoCreateCommandDTO dto) {
        GenericResponseDTO<MVMemoCreateCommandDTO> response = new GenericResponseDTO().successResponse();
        try {
            MovementMemoChangeCommand command = new MovementMemoChangeCommand(
                    dto.getMvID(),
                    objectMapper.writeValueAsString(movAssembler.toMemoDTOPostRequest(dto))
            );
            commandGateway.send(command, new CommandCallback<MovementMemoChangeCommand, Object>() {
                @Override
                public void onResult(CommandMessage<? extends MovementMemoChangeCommand> commandMessage, CommandResultMessage<?> commandResultMessage) {
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

    private GenericResponseDTO<MVMemoCreateCommandDTO> defaultPutMovementMemoFallback(MVMemoCreateCommandDTO dto, Throwable e) throws IOException {
        return new GenericResponseDTO<MVMemoCreateCommandDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }

    @HystrixCommand(fallbackMethod = "defaultDeleteMovementMemoFallback")
    @Caching(
            evict = {
                    @CacheEvict(value = "findOneByMovementIDCache", allEntries = true),
                    @CacheEvict(value = "getAllMovementWebCache", allEntries = true)
            }
    )
    public GenericResponseDTO<MVMemoDeleteCommandDTO> deleteMovementMemo(MVMemoDeleteCommandDTO dto) {
        GenericResponseDTO<MVMemoDeleteCommandDTO> response = new GenericResponseDTO().successResponse();
        Optional<MovementEntryProjection> movement = repository.findOneByMemoID(dto.getMemoID(), MVStatus.INACTIVE.name());
        if(movement.isPresent()) {
            try {
                MovementMemoDeleteCommand command = new MovementMemoDeleteCommand(
                        movement.get().getMvID(),
                        objectMapper.writeValueAsString(movAssembler.toMemoDTODeleteRequest(dto))
                );
                commandGateway.send(command, new CommandCallback<MovementMemoDeleteCommand, Object>() {
                    @Override
                    public void onResult(CommandMessage<? extends MovementMemoDeleteCommand> commandMessage, CommandResultMessage<?> commandResultMessage) {
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

    private GenericResponseDTO<MVMemoDeleteCommandDTO> defaultDeleteMovementMemoFallback(MVMemoDeleteCommandDTO dto, Throwable e) throws IOException {
        return new GenericResponseDTO<MVMemoDeleteCommandDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }
    
    @HystrixCommand(fallbackMethod = "defaultPutMovementMemoStateAndStatusFallback")
    @Caching(
            evict = {
                    @CacheEvict(value = "findOneByMovementIDCache", allEntries = true),
                    @CacheEvict(value = "getAllMovementWebCache", allEntries = true)
            }
    )
    public GenericResponseDTO<MVMemoChangeStateAndStatusCommandDTO> putMovementMemoStateAndStatus(MVMemoChangeStateAndStatusCommandDTO dto) {
        GenericResponseDTO<MVMemoChangeStateAndStatusCommandDTO> response = new GenericResponseDTO().successResponse();
        try {
            MovementMemoChangeStateAndStatusCommand command = new MovementMemoChangeStateAndStatusCommand(
                    dto.getMvID(),
                    objectMapper.writeValueAsString(movAssembler.toMemoDTOUpdateStateAndStatusRequest(dto))
            );
            commandGateway.send(command, new CommandCallback<MovementMemoChangeStateAndStatusCommand, Object>() {
                @Override
                public void onResult(CommandMessage<? extends MovementMemoChangeStateAndStatusCommand> commandMessage, CommandResultMessage<?> commandResultMessage) {
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

    private GenericResponseDTO<MVMemoChangeStateAndStatusCommandDTO> defaultPutMovementMemoStateAndStatusFallback(MVMemoChangeStateAndStatusCommandDTO dto, Throwable e) throws IOException {
        return new GenericResponseDTO<MVMemoChangeStateAndStatusCommandDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }
    
}
