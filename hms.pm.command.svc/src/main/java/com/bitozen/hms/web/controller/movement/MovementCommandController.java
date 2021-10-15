package com.bitozen.hms.web.controller.movement;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.type.ProjectType;
import com.bitozen.hms.common.util.LogOpsUtil;
import com.bitozen.hms.pm.common.dto.command.movement.MovementChangeCommandDTO;
import com.bitozen.hms.pm.common.dto.command.movement.MovementCreateCommandDTO;
import com.bitozen.hms.pm.common.dto.command.movement.MovementDeleteCommandDTO;
import com.bitozen.hms.web.hystrix.movement.MovementHystrixCommandService;
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
public class MovementCommandController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpServletRequest httpRequest;

    @Autowired
    private MovementHystrixCommandService service;

    @RequestMapping(value = "/command/get.movement.dummy", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public @ResponseBody
    MovementCreateCommandDTO getDummy() {
        return new MovementCreateCommandDTO().getInstance();
    }

    @RequestMapping(value = "/command/post.movement",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<MovementCreateCommandDTO>> postMovement(@RequestBody MovementCreateCommandDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CQRS, "Movement", MovementCommandController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Command", "Create",
                            dto.getCreatedBy(),
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<MovementCreateCommandDTO> response = service.postMovement(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/command/put.movement",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<MovementChangeCommandDTO>> putMovement(@RequestBody MovementChangeCommandDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CQRS, "Movement", MovementCommandController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Command", "Update",
                            dto.getUpdatedBy(),
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<MovementChangeCommandDTO> response = service.putMovement(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/command/delete.movement",
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<MovementDeleteCommandDTO>> deleteMovement(@RequestBody MovementDeleteCommandDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CQRS, "Movement", MovementCommandController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Command", "Delete",
                            dto.getUpdatedBy(),
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<MovementDeleteCommandDTO> response = service.deleteMovement(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
