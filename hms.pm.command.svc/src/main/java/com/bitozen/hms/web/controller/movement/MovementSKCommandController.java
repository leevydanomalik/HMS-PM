package com.bitozen.hms.web.controller.movement;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.type.ProjectType;
import com.bitozen.hms.common.util.LogOpsUtil;
import com.bitozen.hms.pm.common.dto.command.movement.MVSKChangeStateAndStatusCommandDTO;
import com.bitozen.hms.pm.common.dto.command.movement.MVSKCreateCommandDTO;
import com.bitozen.hms.pm.common.dto.command.movement.MVSKDeleteCommandDTO;
import com.bitozen.hms.web.hystrix.movement.MovementSKHystrixCommandService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@Slf4j
public class MovementSKCommandController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpServletRequest httpRequest;

    @Autowired
    private MovementSKHystrixCommandService service;

    @RequestMapping(value = "/command/get.movement.sk.dummy", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public @ResponseBody
    MVSKCreateCommandDTO getDummy() {
        return new MVSKCreateCommandDTO().getInstance();
    }

    @RequestMapping(value = "/command/post.movement.sk",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<MVSKCreateCommandDTO>> postMovement(@RequestBody MVSKCreateCommandDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CQRS, "Movement", MovementSKCommandController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Command", "Create",
                            "SYSTEM",
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<MVSKCreateCommandDTO> response = service.postMovementSK(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/command/put.movement.sk",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<MVSKCreateCommandDTO>> putMovement(@RequestBody MVSKCreateCommandDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CQRS, "Movement", MovementSKCommandController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Command", "Create",
                            "SYSTEM",
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<MVSKCreateCommandDTO> response = service.putMovementSK(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @RequestMapping(value = "/command/put.movement.sk.state.and.status",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<MVSKChangeStateAndStatusCommandDTO>> putMovementSKStateAndStatus(@RequestBody MVSKChangeStateAndStatusCommandDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CQRS, "Movement", MovementSKCommandController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Command", "Create",
                            "SYSTEM",
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<MVSKChangeStateAndStatusCommandDTO> response = service.putMovementSKChangeStateAndStatus(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/command/delete.movement.sk",
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<MVSKDeleteCommandDTO>> deleteMovement(@RequestBody MVSKDeleteCommandDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CQRS, "Movement", MovementSKCommandController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Command", "Create",
                            "SYSTEM",
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<MVSKDeleteCommandDTO> response = service.deleteMovementSK(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
