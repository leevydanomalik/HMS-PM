package com.bitozen.hms.web.controller.blacklist;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.dto.GetListRequestDTO;
import com.bitozen.hms.common.type.ProjectType;
import com.bitozen.hms.common.util.LogOpsUtil;
import com.bitozen.hms.pm.common.dto.query.blacklist.BlacklistDTO;
import com.bitozen.hms.web.hystrix.blacklist.BlacklistHystrixQueryService;
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
public class BlacklistQueryController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpServletRequest httpRequest;

    @Autowired
    private BlacklistHystrixQueryService service;

    @RequestMapping(value = "/query/get.blacklist.version", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public @ResponseBody
    String getVersionBlacklist() {
        return "Blacklist Query Service - version 1.0.0-SNAPSHOT";
    }

    @RequestMapping(value = "/query/get.blacklist.by.id/{blacklistID}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<BlacklistDTO>> getBlacklistByID(@PathVariable("blacklistID") String blacklistID) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CQRS, "Blacklist", BlacklistQueryController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Query", "getBlacklistByID",
                            "SYSTEM",
                            blacklistID)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<BlacklistDTO> response = service.getBlacklistByID(blacklistID);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/query/get.blacklist.for.web",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<Map<String, Object>>> getAllBlacklistForWeb(@RequestBody GetListRequestDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CQRS, "Blacklist", BlacklistQueryController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Query", "getAllBlacklistForWeb",
                            "SYSTEM",
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<Map<String, Object>> response = service.getBlacklistForWeb(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
