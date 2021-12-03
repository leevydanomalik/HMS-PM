package com.bitozen.hms.web.controller.movement;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.type.ProjectType;
import com.bitozen.hms.common.util.LogOpsUtil;
import com.bitozen.hms.pm.common.dto.command.movement.MVMemoChangeStateAndStatusCommandDTO;
import com.bitozen.hms.pm.common.dto.command.movement.MVMemoCreateCommandDTO;
import com.bitozen.hms.pm.common.dto.command.movement.MVMemoDeleteCommandDTO;
import com.bitozen.hms.web.hystrix.movement.MovementMemoHystrixCommandService;
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
public class MovementMemoCommandController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpServletRequest httpRequest;

    @Autowired
    private MovementMemoHystrixCommandService service;

    @RequestMapping(value = "/command/get.movement.memo.dummy", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public @ResponseBody
    MVMemoCreateCommandDTO getDummy() {
        return new MVMemoCreateCommandDTO().getInstance();
    }

    @RequestMapping(value = "/command/post.movement.memo",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<MVMemoCreateCommandDTO>> postMovementMemo(@RequestBody MVMemoCreateCommandDTO dto) {
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
        GenericResponseDTO<MVMemoCreateCommandDTO> response = service.postMovementMemo(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/command/put.movement.memo",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<MVMemoCreateCommandDTO>> putMovementMemo(@RequestBody MVMemoCreateCommandDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CQRS, "Movement", MovementSKCommandController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Command", "Update",
                            "SYSTEM",
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<MVMemoCreateCommandDTO> response = service.putMovementMemo(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/command/delete.movement.memo",
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<MVMemoDeleteCommandDTO>> deleteMovementMemo(@RequestBody MVMemoDeleteCommandDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CQRS, "Movement", MovementSKCommandController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Command", "Delete",
                            "SYSTEM",
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<MVMemoDeleteCommandDTO> response = service.deleteMovementMemo(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @RequestMapping(value = "/command/put.movement.memo.state.and.status",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<MVMemoChangeStateAndStatusCommandDTO>> putMovementMemoStateAndStatus(@RequestBody MVMemoChangeStateAndStatusCommandDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CQRS, "Movement", MovementSKCommandController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Command", "Update",
                            "SYSTEM",
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<MVMemoChangeStateAndStatusCommandDTO> response = service.putMovementMemoStateAndStatus(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
}
