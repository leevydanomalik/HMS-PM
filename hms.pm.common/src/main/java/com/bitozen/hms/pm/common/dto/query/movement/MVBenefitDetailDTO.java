package com.bitozen.hms.pm.common.dto.query.movement;

import com.bitozen.hms.common.dto.share.BizparOptimizeDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MVBenefitDetailDTO implements Serializable {

    private String detailID;
    private BizparOptimizeDTO detailType;
    private BizparOptimizeDTO detailSubtype;
    private Double detailValue;
}
