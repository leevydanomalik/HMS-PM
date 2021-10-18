package com.bitozen.hms.web.controller.termination;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.type.ProjectType;
import com.bitozen.hms.common.util.LogOpsUtil;
import com.bitozen.hms.web.hystrix.Termination.ProlongedIllnessRegistryDocumentHystrixService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Dumayangsari
 */
@RestController
@CrossOrigin(methods = {RequestMethod.GET, RequestMethod.DELETE})
@Slf4j
public class ProlongedIllnessRegistryDocumentController {
    public static final long ONE_SECOND = 15000;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpServletRequest httpRequest;

    @Autowired
    private ProlongedIllnessRegistryDocumentHystrixService service;

    @PostMapping("/post.prolonged.illness.registry.document")
    @CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS},
            allowedHeaders = {"Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers"},
            exposedHeaders = {"Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"})
    public ResponseEntity<GenericResponseDTO<String>> uploadProlongedIllnessRegistryDocumentFile(@RequestParam("file") MultipartFile upload, @RequestParam("piID") String piID, @RequestParam("updatedBy") String updatedBy, @RequestParam("updatedDate") String updatedDate) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CQRS, "Prolonged Illness Registry Document", ProlongedIllnessRegistryDocumentController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Command", "Upload",
                            "SYSTEM",
                            piID)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        if (upload.isEmpty()) {
            return new ResponseEntity("Please select a file.", HttpStatus.OK);
        }

        GenericResponseDTO<String> response = service.saveUploadedFile(upload, piID, updatedBy, updatedDate);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/get.prolonged.illness.registry.document/{piID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getProlongedIllnessRegistryDocumentURL(@PathVariable("piID") String piID) {
        GenericResponseDTO<byte[]> response = new GenericResponseDTO().successResponse();
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CQRS, "Prolonged Illness Registry Document", ProlongedIllnessRegistryDocumentController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Command", "getOrderDocumentUrl",
                            "SYSTEM",
                            piID)));
            response = service.generateProlongedIllnessRegistryDocumentURLByte(piID);
            return ResponseEntity.status(HttpStatus.OK).body(response.getData());
        } catch (Exception ex) {
            log.info(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(response.getData());
    }
}
