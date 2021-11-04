package com.bitozen.hms.web.service.migration.blacklist;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.pm.common.dto.command.blacklist.BlacklistCreateCommandDTO;
import com.bitozen.hms.rabbit.producer.blacklist.BlacklistRabbitProducer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

@Service
@Slf4j
public class BlacklistMigrationService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BlacklistRabbitProducer producer;

    public GenericResponseDTO<String> processBlacklist(MultipartFile upload) throws Exception {
        Reader reader = new BufferedReader(new InputStreamReader(upload.getInputStream()));
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(reader).getAsJsonArray();
        List<BlacklistCreateCommandDTO> dtos = objectMapper.readValue(jsonArray.toString(), new TypeReference<List<BlacklistCreateCommandDTO>>() {});
        dtos.parallelStream().forEachOrdered((o) -> {
            producer.blacklistMigrationProducer(o);
        });
        return new GenericResponseDTO().successResponse();
    }
}
