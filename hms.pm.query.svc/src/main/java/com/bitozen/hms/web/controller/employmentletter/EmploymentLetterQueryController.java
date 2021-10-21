package com.bitozen.hms.web.controller.employmentletter;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.dto.GetListRequestDTO;
import com.bitozen.hms.common.type.ProjectType;
import com.bitozen.hms.common.util.LogOpsUtil;
import com.bitozen.hms.pm.common.dto.query.employmentletter.EmploymentLetterDTO;
import com.bitozen.hms.web.hystrix.employmentletter.EmploymentLetterHystrixQueryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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
public class EmploymentLetterQueryController {
    
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpServletRequest httpRequest;

    @Autowired
    private EmploymentLetterHystrixQueryService service;
    
    @RequestMapping(value = "/query/get.employment.letter.version", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public @ResponseBody
    String getVersionEmploymentLetter() {
        return "Employment Letter Query Service - version 1.0.0-SNAPSHOT";
    }
    
    @RequestMapping(value = "/query/get.employment.letter.by.id/{elID}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<EmploymentLetterDTO>> getEmploymentLetterByID(@PathVariable("elID") String elID) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CQRS, "Employment Letter", EmploymentLetterQueryController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Query", "getEmploymentLetterByID",
                            "SYSTEM",
                            elID)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<EmploymentLetterDTO> response = service.getEmploymentLetterByID(elID);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @RequestMapping(value = "/query/get.employment.letter.for.web",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<Map<String, Object>>> getAllEmploymentLetterForWeb(@RequestBody GetListRequestDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CQRS, "Employment Letter", EmploymentLetterQueryController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Query", "getAllEmploymentLetterForWeb",
                            "SYSTEM",
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<Map<String, Object>> response = service.getEmploymentLetterForWeb(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
}
