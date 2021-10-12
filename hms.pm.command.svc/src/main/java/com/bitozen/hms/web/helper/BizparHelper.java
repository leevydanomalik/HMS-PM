package com.bitozen.hms.web.helper;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.dto.share.BizparDTO;
import com.bitozen.hms.common.dto.share.BizparOptimizeDTO;
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
public class BizparHelper {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RequestUtil requestUtil;

    @Autowired
    private RestClientUtil restClientUtil;

    @Value("${hms.bizpar.url}")
    private String HMS_BIZPAR_URL;

    @Value("${hms.bizpar.url.get.by.key}")
    private String HMS_BIZPAR_URL_GET_BY_KEY;

    public BizparDTO findBizparByKey(String key) {
        if (key != null && !key.equalsIgnoreCase("")) {
            ResponseEntity<GenericResponseDTO<BizparDTO>> data = restClientUtil.restServiceExchange(
                    HMS_BIZPAR_URL,
                    HMS_BIZPAR_URL_GET_BY_KEY,
                    HttpMethod.GET,
                    requestUtil.getPreFormattedRequestWithToken(),
                    GenericResponseDTO.class,
                    key);
            if (data.getBody().getStatus().equals(ResponseStatus.S)) {
                return objectMapper.convertValue(data.getBody().getData(), BizparDTO.class);
            }
        }
        return null;
    }

    public BizparOptimizeDTO convertBizpar(String key) {
        BizparDTO dto = findBizparByKey(key);
        if (dto != null) {
            BizparOptimizeDTO dtoBizpar = new BizparOptimizeDTO();
            dtoBizpar.setKey(dto.getKey());
            dtoBizpar.setValue(dto.getValue());
            dtoBizpar.setCategory(dto.getCategory());
            return dtoBizpar;
        }
        return null;
    }
}

