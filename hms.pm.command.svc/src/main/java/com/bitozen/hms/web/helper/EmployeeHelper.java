package com.bitozen.hms.web.helper;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.dto.share.EmployeeOptimizeDTO;
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

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class EmployeeHelper {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RequestUtil requestUtil;

    @Autowired
    private RestClientUtil restClientUtil;

    @Value("${hms.employee.query.url}")
    private String HMS_EMP_QRY_URL;

    @Value("${hms.employee.get.by.id.optimize}")
    private String HMS_EMP_GET_BY_ID_OPTIMIZED;

    public EmployeeOptimizeDTO findEmployeeOptimizeByKey(String key) {
        if (key != null && !key.equalsIgnoreCase("")) {
            ResponseEntity<GenericResponseDTO<EmployeeOptimizeDTO>> data = restClientUtil.restServiceExchange(
                    HMS_EMP_QRY_URL,
                    HMS_EMP_GET_BY_ID_OPTIMIZED,
                    HttpMethod.GET,
                    requestUtil.getPreFormattedRequestWithToken(),
                    GenericResponseDTO.class,
                    key);
            if (data.getBody().getStatus().equals(ResponseStatus.S)) {
                return objectMapper.convertValue(data.getBody().getData(), EmployeeOptimizeDTO.class);
            }
        }
        return null;
    }

    public List<EmployeeOptimizeDTO> findEmployees(List<String> employeeIDs) {
        List<EmployeeOptimizeDTO> employees = new ArrayList<>();
        employeeIDs.stream().forEach((o) -> {
            employees.add(findEmployeeOptimizeByKey(o));
        });
        return employees;
    }
}
