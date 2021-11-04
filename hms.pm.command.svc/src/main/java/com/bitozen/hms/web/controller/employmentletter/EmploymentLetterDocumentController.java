package com.bitozen.hms.web.controller.employmentletter;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.type.ProjectType;
import com.bitozen.hms.common.util.LogOpsUtil;
import com.bitozen.hms.web.hystrix.employmentletter.EmploymentLetterDocumentHystrixService;
import com.bitozen.hms.web.service.minio.employmentletter.EmploymentLetterMinioService;
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
 * @author Jeremia
 */
@RestController
@CrossOrigin(methods = {RequestMethod.GET, RequestMethod.DELETE})
@Slf4j
public class EmploymentLetterDocumentController {

    public static final long ONE_SECOND = 15000;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpServletRequest httpRequest;

    @Autowired
    private EmploymentLetterDocumentHystrixService service;

    @Autowired
    private EmploymentLetterMinioService employmentDocumentUrlService;

    @PostMapping("/post.employment.letter.document")
    @CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS},
            allowedHeaders = {"Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers"},
            exposedHeaders = {"Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"})
    public ResponseEntity<GenericResponseDTO<String>> uploadEmploymentLetterDocumentFile(@RequestParam("file") MultipartFile upload, @RequestParam("elID") String elID, @RequestParam("updatedBy") String updatedBy, @RequestParam("updatedDate") String updatedDate) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CQRS, "EmploymentLetter Document", EmploymentLetterDocumentController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Command", "Upload",
                            "SYSTEM",
                            elID)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        if (upload.isEmpty()) {
            return new ResponseEntity("Please select a file.", HttpStatus.OK);
        }

        GenericResponseDTO<String> response = service.saveUploadedFile(upload, elID, updatedBy, updatedDate);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/get.employment.letter.document/{elID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getEmploymentLetterDocumentURL(@PathVariable("elID") String elID) {
        GenericResponseDTO<byte[]> response = new GenericResponseDTO().successResponse();
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CQRS, "EmploymentLetter Document", EmploymentLetterDocumentController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Command", "getOrderDocumentUrl",
                            "SYSTEM",
                            elID)));
            response = service.generateEmploymentLetterDocumentURLByte(elID);
            return ResponseEntity.status(HttpStatus.OK).body(response.getData());
        } catch (Exception ex) {
            log.info(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(response.getData());
    }

    

}
