package com.bitozen.hms.pm.common.dto.query.movement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MVBenefitDTO implements Serializable {

    private String benefitID;
    private String benefitName;
    private List<MVBenefitDetailDTO> benefitDetails;
}
