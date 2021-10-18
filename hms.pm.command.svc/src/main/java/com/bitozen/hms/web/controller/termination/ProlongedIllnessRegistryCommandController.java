package com.bitozen.hms.web.controller.termination;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.type.ProjectType;
import com.bitozen.hms.common.util.LogOpsUtil;
import com.bitozen.hms.pm.common.dto.command.termination.ProlongedIllnessRegistryChangeCommandDTO;
import com.bitozen.hms.pm.common.dto.command.termination.ProlongedIllnessRegistryCreateCommandDTO;
import com.bitozen.hms.pm.common.dto.command.termination.ProlongedIllnessRegistryDeleteCommandDTO;
import com.bitozen.hms.web.hystrix.Termination.ProlongedIllnessRegistryHystrixCommandService;
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

/**
 *
 * @author Dumayangsari
 */
@RestController
@Slf4j
public class ProlongedIllnessRegistryCommandController {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpServletRequest httpRequest;

    @Autowired
    private ProlongedIllnessRegistryHystrixCommandService service;

    @RequestMapping(value = "/command/get.prolonged.illness.registry.dummy", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public @ResponseBody
    ProlongedIllnessRegistryCreateCommandDTO getDummy() {
        return new ProlongedIllnessRegistryCreateCommandDTO().getInstance();
    }

    @RequestMapping(value = "/command/post.prolonged.illness.registry",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<ProlongedIllnessRegistryCreateCommandDTO>> postProlongedIllnessRegistry(@RequestBody ProlongedIllnessRegistryCreateCommandDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CQRS, "Prolonged Illness Registry", ProlongedIllnessRegistryCommandController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Command", "Create",
                            dto.getCreatedBy(),
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<ProlongedIllnessRegistryCreateCommandDTO> response = service.postProlongedIllnessRegistry(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/command/put.prolonged.illness.registry",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<ProlongedIllnessRegistryChangeCommandDTO>> putProlongedIllnessRegistry(@RequestBody ProlongedIllnessRegistryChangeCommandDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CQRS, "Prolonged Illness Registry", ProlongedIllnessRegistryCommandController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Command", "Update",
                            dto.getUpdatedBy(),
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<ProlongedIllnessRegistryChangeCommandDTO> response = service.putProlongedIllnessRegistry(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/command/delete.prolonged.illness.registry",
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<ProlongedIllnessRegistryDeleteCommandDTO>> deleteProlongedIllnessRegistry(@RequestBody ProlongedIllnessRegistryDeleteCommandDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CQRS, "Prolonged Illness Registry", ProlongedIllnessRegistryCommandController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Command", "Delete",
                            dto.getUpdatedBy(),
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<ProlongedIllnessRegistryDeleteCommandDTO> response = service.deleteProlongedIllnessRegistry(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
