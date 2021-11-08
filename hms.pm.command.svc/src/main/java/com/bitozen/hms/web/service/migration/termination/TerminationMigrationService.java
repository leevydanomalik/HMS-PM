package com.bitozen.hms.web.service.migration.termination;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.pm.common.dto.command.employmentletter.EmploymentLetterCreateCommandDTO;
import com.bitozen.hms.pm.common.dto.command.termination.TerminationCreateCommandDTO;
import com.bitozen.hms.rabbit.producer.termination.TerminationProducer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Service
@Slf4j
public class TerminationMigrationService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TerminationProducer producer;

    public GenericResponseDTO<String> processTermination(MultipartFile upload) throws Exception {
        Reader reader = new BufferedReader(new InputStreamReader(upload.getInputStream()));
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(reader).getAsJsonArray();
        List<TerminationCreateCommandDTO> dtos = objectMapper.readValue(jsonArray.toString(), new TypeReference<List<TerminationCreateCommandDTO>>() {});
        dtos.parallelStream().forEachOrdered((o) -> {
            producer.terminationMigrationProducer(o);
        });
        return new GenericResponseDTO().successResponse();
    }
}
