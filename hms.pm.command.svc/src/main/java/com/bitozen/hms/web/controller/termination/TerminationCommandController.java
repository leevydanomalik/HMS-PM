package com.bitozen.hms.web.controller.termination;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.type.ProjectType;
import com.bitozen.hms.common.util.LogOpsUtil;
import com.bitozen.hms.pm.common.dto.command.termination.TerminationChangeCommandDTO;
import com.bitozen.hms.pm.common.dto.command.termination.TerminationCreateCommandDTO;
import com.bitozen.hms.pm.common.dto.command.termination.TerminationDeleteCommandDTO;
import com.bitozen.hms.pm.common.dto.command.termination.TerminationStateAndStatusChangeCommandDTO;
import com.bitozen.hms.web.hystrix.termination.TerminationHystrixCommandService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TerminationCommandController {
    
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpServletRequest httpRequest;

    @Autowired
    private TerminationHystrixCommandService service;
    
    @RequestMapping(value = "/command/get.termination.dummy", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public @ResponseBody
    TerminationCreateCommandDTO getDummyTermination() {
        return new TerminationCreateCommandDTO().getInstance();
    }
    
    @RequestMapping(value = "/command/post.termination",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<TerminationCreateCommandDTO>> postTermination(@RequestBody TerminationCreateCommandDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CQRS, "Termination", TerminationCommandController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Command", "Create",
                            dto.getCreatedBy(),
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<TerminationCreateCommandDTO> response = service.postTermination(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @RequestMapping(value = "/command/put.termination",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<TerminationChangeCommandDTO>> putTermination(@RequestBody TerminationChangeCommandDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CQRS, "Termination", TerminationCommandController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Command", "Update",
                            dto.getUpdatedBy(),
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<TerminationChangeCommandDTO> response = service.putTermination(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/command/put.termination.state.and.status",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<TerminationStateAndStatusChangeCommandDTO>> updateTerminationStateAndStatus(@RequestBody TerminationStateAndStatusChangeCommandDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CQRS, "Termination", TerminationCommandController.class.getName(),
                            httpRequest.getRequestURL() + httpRequest.getRequestURI(),
                            new Date(), "Command", "Change",
                            dto.getUpdatedBy(),
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<TerminationStateAndStatusChangeCommandDTO> response = service.changeTerminationStateAndStatus(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @RequestMapping(value = "/command/delete.termination",
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<TerminationDeleteCommandDTO>> deleteTermination(@RequestBody TerminationDeleteCommandDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CQRS, "Termination", TerminationCommandController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Command", "Delete",
                            dto.getUpdatedBy(),
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<TerminationDeleteCommandDTO> response = service.deleteTermination(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
