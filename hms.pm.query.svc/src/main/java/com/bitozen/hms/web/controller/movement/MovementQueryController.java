package com.bitozen.hms.web.controller.movement;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.dto.GetListRequestDTO;
import com.bitozen.hms.common.type.ProjectType;
import com.bitozen.hms.common.util.LogOpsUtil;
import com.bitozen.hms.pm.common.dto.query.movement.MovementDTO;
import com.bitozen.hms.web.hystrix.movement.MovementHystrixQueryService;
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
import java.util.Map;

@RestController
@Slf4j
public class MovementQueryController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpServletRequest httpRequest;

    @Autowired
    private MovementHystrixQueryService service;

    @RequestMapping(value = "/query/get.movement.version", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public @ResponseBody
    String getVersionMovement() {
        return "Movement Query Service - version 1.0.0-SNAPSHOT";
    }

    @RequestMapping(value = "/query/get.movement.by.id/{movementID}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<MovementDTO>> getMovementByID(@PathVariable("movementID") String movementID) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CQRS, "Movement", MovementQueryController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Query", "getMovementByID",
                            "SYSTEM",
                            movementID)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<MovementDTO> response = service.getMovementByID(movementID);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/query/get.movement.for.web",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<Map<String, Object>>> getAllMovementForWeb(@RequestBody GetListRequestDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CQRS, "Movement", MovementQueryController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Query", "getAllMovementForWeb",
                            "SYSTEM",
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<Map<String, Object>> response = service.getMovementForWeb(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
