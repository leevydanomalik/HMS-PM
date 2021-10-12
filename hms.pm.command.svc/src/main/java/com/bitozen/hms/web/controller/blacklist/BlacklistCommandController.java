package com.bitozen.hms.web.controller.blacklist;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.type.ProjectType;
import com.bitozen.hms.common.util.LogOpsUtil;
import com.bitozen.hms.pm.common.dto.command.blacklist.BlacklistChangeCommandDTO;
import com.bitozen.hms.pm.common.dto.command.blacklist.BlacklistCreateCommandDTO;
import com.bitozen.hms.pm.common.dto.command.blacklist.BlacklistDeleteCommandDTO;
import com.bitozen.hms.web.hystrix.blacklist.BlacklistHystrixCommandService;
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
public class BlacklistCommandController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpServletRequest httpRequest;

    @Autowired
    private BlacklistHystrixCommandService service;

    @RequestMapping(value = "/command/get.blacklist.dummy", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public @ResponseBody
    BlacklistCreateCommandDTO getDummy() {
        return new BlacklistCreateCommandDTO().getInstance();
    }

    @RequestMapping(value = "/command/post.blacklist",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<BlacklistCreateCommandDTO>> postBlacklist(@RequestBody BlacklistCreateCommandDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CQRS, "Blacklist", BlacklistCommandController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Command", "Create",
                            dto.getCreatedBy(),
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<BlacklistCreateCommandDTO> response = service.postBlacklist(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/command/put.blacklist",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<BlacklistChangeCommandDTO>> putBlacklist(@RequestBody BlacklistChangeCommandDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CQRS, "Blacklist", BlacklistCommandController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Command", "Update",
                            dto.getUpdatedBy(),
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<BlacklistChangeCommandDTO> response = service.putBlacklist(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/command/delete.blacklist",
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<BlacklistDeleteCommandDTO>> deleteBlacklist(@RequestBody BlacklistDeleteCommandDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CQRS, "Blacklist", BlacklistCommandController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Command", "Delete",
                            dto.getUpdatedBy(),
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<BlacklistDeleteCommandDTO> response = service.deleteBlacklist(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
