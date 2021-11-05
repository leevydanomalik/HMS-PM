package com.bitozen.hms.web.controller.movement;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.web.hystrix.movement.MovementMigrationHystrixService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE})
@Slf4j
public class MovementMigrationController {

    @Autowired
    private MovementMigrationHystrixService service;

    @SneakyThrows
    @PostMapping("/post.movement.migration")
    @CrossOrigin(origins = "*",
            methods = {RequestMethod.POST, RequestMethod.OPTIONS},
            allowedHeaders = {"Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers"},
            exposedHeaders = {"Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"})
    public ResponseEntity<GenericResponseDTO<String>> uploadTemplateDocumentFile(@RequestParam("file") MultipartFile upload) {
        if (upload.isEmpty()) {
            return new ResponseEntity("Please select a file.", HttpStatus.OK);
        }

        GenericResponseDTO<String> response = service.saveUploadedMovementFile(upload);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
