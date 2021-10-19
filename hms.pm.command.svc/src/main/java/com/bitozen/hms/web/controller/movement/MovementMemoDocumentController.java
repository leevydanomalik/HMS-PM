package com.bitozen.hms.web.controller.movement;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.type.ProjectType;
import com.bitozen.hms.common.util.LogOpsUtil;
import com.bitozen.hms.web.hystrix.movement.MovementMemoDocumentHystrixService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@CrossOrigin(methods = {RequestMethod.GET, RequestMethod.DELETE})
@Slf4j
public class MovementMemoDocumentController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpServletRequest httpRequest;

    @Autowired
    private MovementMemoDocumentHystrixService service;

    @PostMapping("/post.movement.memo.document")
    @CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS},
            allowedHeaders = {"Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers"},
            exposedHeaders = {"Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"})
    public ResponseEntity<GenericResponseDTO<String>> uploadMovementDocumentFile(@RequestParam("file") MultipartFile upload, @RequestParam("mvID") String mvID, @RequestParam("empID") String empID, @RequestParam("memoID") String memoID, @RequestParam("docID") String docID) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CQRS, "Movement Memo Document", MovementMemoDocumentController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Command", "Upload",
                            "SYSTEM",
                            memoID)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        if (upload.isEmpty()) {
            return new ResponseEntity("Please select a file.", HttpStatus.OK);
        }

        GenericResponseDTO<String> response = service.saveUploadedFile(upload, mvID, empID, memoID, docID);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/get.movement.memo.document/{memoID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getMovementDocumentURL(@PathVariable("memoID") String memoID) {
        GenericResponseDTO<byte[]> response = new GenericResponseDTO().successResponse();
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CQRS, "Movement Memo Document", MovementMemoDocumentController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Command", "getOrderDocumentUrl",
                            "SYSTEM",
                            memoID)));
            response = service.generateMovementMemoDocumentURLByte(memoID);
            return ResponseEntity.status(HttpStatus.OK).body(response.getData());
        } catch (Exception ex) {
            log.info(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(response.getData());
    }
}
