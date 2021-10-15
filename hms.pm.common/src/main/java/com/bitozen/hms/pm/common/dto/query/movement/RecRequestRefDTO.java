package com.bitozen.hms.pm.common.dto.query.movement;

import com.bitozen.hms.common.dto.share.ESOptimizeDTO;
import com.bitozen.hms.common.dto.share.PositionOptimizeDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RecRequestRefDTO implements Serializable {

    private String recRequestID;
    private String recRefMPPID;
    private ESOptimizeDTO recRefMPPES;
    private String recRequestCategory;
    private String recRequestCategoryKey;
    private PositionOptimizeDTO recRequestPosition;
    private String recRequestType;
    private String recRequestTypeKey;
    private String recType;
    private String recTypeKey;
}
