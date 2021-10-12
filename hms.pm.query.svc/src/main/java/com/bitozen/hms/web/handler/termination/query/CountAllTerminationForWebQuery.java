package com.bitozen.hms.web.handler.termination.query;

import com.bitozen.hms.common.dto.GetListRequestDTO;
import lombok.Value;

@Value
public class CountAllTerminationForWebQuery {
    private GetListRequestDTO request;
}
