package com.bitozen.hms.pm.common.dto.command.movement;

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
public class MVBenefitDetailCreateDTO implements Serializable {

    private String detailID;
    private String detailType;
    private String detailSubtype;
    private Double detailValue;
}
