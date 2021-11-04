package com.bitozen.hms.web.controller.employmentletter;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.type.ProjectType;
import com.bitozen.hms.common.util.LogOpsUtil;
import com.bitozen.hms.pm.common.dto.command.employmentletter.EmploymentLetterChangeCommandDTO;
import com.bitozen.hms.pm.common.dto.command.employmentletter.EmploymentLetterCreateCommandDTO;
import com.bitozen.hms.pm.common.dto.command.employmentletter.EmploymentLetterDeleteCommandDTO;
import com.bitozen.hms.pm.common.dto.command.employmentletter.EmploymentLetterStateAndEmploymentLetterStatusChangeCommandDTO;
import com.bitozen.hms.web.controller.blacklist.BlacklistCommandController;
import com.bitozen.hms.web.controller.termination.TerminationCommandController;
import com.bitozen.hms.web.hystrix.employmentletter.EmploymentLetterHystrixCommandService;
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
 * @author Jeremia
 */
@RestController
@Slf4j
public class EmploymentLetterCommandController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpServletRequest httpRequest;

    @Autowired
    private EmploymentLetterHystrixCommandService service;

    @RequestMapping(value = "/command/get.employment.letter.dummy", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public @ResponseBody
    EmploymentLetterCreateCommandDTO getDummy() {
        return new EmploymentLetterCreateCommandDTO().getInstance();
    }

    @RequestMapping(value = "/command/post.employment.letter",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<EmploymentLetterCreateCommandDTO>> postEmploymentLetter(@RequestBody EmploymentLetterCreateCommandDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CQRS, "Employment Letter", EmploymentLetterCommandController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Command", "Create",
                            dto.getCreatedBy(),
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<EmploymentLetterCreateCommandDTO> response = service.postEmploymentLetter(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/command/put.employment.letter",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<EmploymentLetterChangeCommandDTO>> putEmploymentLetter(@RequestBody EmploymentLetterChangeCommandDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CQRS, "EmploymentLetter", EmploymentLetterCommandController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Command", "Update",
                            dto.getUpdatedBy(),
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<EmploymentLetterChangeCommandDTO> response = service.putEmploymentLetter(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/command/put.employment.letter.state.and.status",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<EmploymentLetterStateAndEmploymentLetterStatusChangeCommandDTO>> updateEmploymentLetterStateAndStatus(@RequestBody EmploymentLetterStateAndEmploymentLetterStatusChangeCommandDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CQRS, "Employment Letter", TerminationCommandController.class.getName(),
                            httpRequest.getRequestURL() + httpRequest.getRequestURI(),
                            new Date(), "Command", "Update",
                            dto.getUpdatedBy(),
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<EmploymentLetterStateAndEmploymentLetterStatusChangeCommandDTO> response = service.putEmploymentLetterStateAndEmploymentLetterStatus(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/command/delete.employment.letter",
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<EmploymentLetterDeleteCommandDTO>> deleteEmploymentLetter(@RequestBody EmploymentLetterDeleteCommandDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CQRS, "Employment Letter", BlacklistCommandController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Command", "Delete",
                            dto.getUpdatedBy(),
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<EmploymentLetterDeleteCommandDTO> response = service.deleteEmploymentLetter(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
