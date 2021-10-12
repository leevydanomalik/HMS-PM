package com.bitozen.hms.web.helper;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.dto.share.ESOptimizeDTO;
import com.bitozen.hms.common.status.ResponseStatus;
import com.bitozen.hms.pm.common.util.RequestUtil;
import com.bitozen.hms.pm.common.util.RestClientUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ESHelper {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RequestUtil requestUtil;

    @Autowired
    private RestClientUtil restClientUtil;

    @Value("${hms.es.url}")
    private String HMS_ES_URL;

    @Value("${hms.es.url.get.by.id}")
    private String HMS_ES_URL_GET_BY_ID;

    public ESOptimizeDTO findEsByID(String esID) {
        if (esID != null && !esID.equalsIgnoreCase("")) {
            ResponseEntity<GenericResponseDTO<ESOptimizeDTO>> data = restClientUtil.restServiceExchange(
                    HMS_ES_URL,
                    HMS_ES_URL_GET_BY_ID,
                    HttpMethod.GET,
                    requestUtil.getPreFormattedRequestWithToken(),
                    GenericResponseDTO.class,
                    esID);
            if (data.getBody().getStatus().equals(ResponseStatus.S)) {
                return objectMapper.convertValue(data.getBody().getData(), ESOptimizeDTO.class);
            }
        }
        return null;
    }
}
