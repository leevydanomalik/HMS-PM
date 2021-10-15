package com.bitozen.hms.web.hystrix.movement;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MovementDocumentHystrixService {

    @Autowired
    private ObjectMapper objectMapper;
}
