package com.bitozen.hms.web.service.migration.employmentletter;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.pm.common.dto.command.employmentletter.EmploymentLetterCreateCommandDTO;
import com.bitozen.hms.rabbit.producer.employmentletter.EmploymentLetterRabbitProducer;
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
public class EmploymentLetterMigrationService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmploymentLetterRabbitProducer producer;

    public GenericResponseDTO<String> processEmploymentLetter(MultipartFile upload) throws Exception {
        Reader reader = new BufferedReader(new InputStreamReader(upload.getInputStream()));
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(reader).getAsJsonArray();
        List<EmploymentLetterCreateCommandDTO> dtos = objectMapper.readValue(jsonArray.toString(), new TypeReference<List<EmploymentLetterCreateCommandDTO>>() {});
        dtos.parallelStream().forEachOrdered((o) -> {
            producer.employmentLetterMigrationProducer(o);
        });
        return new GenericResponseDTO().successResponse();
    }
}
