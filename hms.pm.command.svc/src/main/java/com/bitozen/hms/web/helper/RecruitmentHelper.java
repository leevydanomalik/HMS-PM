package com.bitozen.hms.web.helper;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.type.ProjectType;
import com.bitozen.hms.common.util.LogOpsUtil;
import com.bitozen.hms.pm.common.util.RequestUtil;
import com.bitozen.hms.pm.common.util.RestClientUtil;
import com.bitozen.hms.recruitment.common.dto.query.RecruitmentRequestDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class RecruitmentHelper {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RequestUtil requestUtil;

    @Autowired
    private RestClientUtil restClientUtil;

    @Value("${hms.recruitment.query.url}")
    private String RECRUITMENT_QRY_URL;

    @Value("${hms.recruitment.request.get.by.id}")
    private String RECRUITMENT_REQUEST_GETBYID_URI;

    public GenericResponseDTO<RecruitmentRequestDTO> findRecruitmentRequestByID(String recruitmentRequestID)  {
        try {
            if (recruitmentRequestID != null && !recruitmentRequestID.trim().equalsIgnoreCase("")) {
                ResponseEntity<GenericResponseDTO<RecruitmentRequestDTO>> data = restClientUtil.restServiceExchange(
                        RECRUITMENT_QRY_URL,
                        RECRUITMENT_REQUEST_GETBYID_URI,
                        HttpMethod.GET,
                        requestUtil.getPreFormattedRequestWithToken(),
                        GenericResponseDTO.class,
                        recruitmentRequestID);
                log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                        ProjectType.CQRS, "RECRUITMENT REQUEST-findRecruitmentRequestByID", new Date(), "REST", data.getBody().getCode(),
                        data.getBody().getData())));
                return data.getBody();
            }
            return null;
        } catch(Exception e) {
            log.info(e.getMessage());
        }
        return null;
    }
}
