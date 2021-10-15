package com.bitozen.hms.pm.common.dto.command.movement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MVBenefitCreateDTO implements Serializable {

    private String benefitID;
    private String benefitName;
    private List<MVBenefitDetailCreateDTO> benefitDetails;

    @JsonIgnore
    public MVBenefitCreateDTO getInstance() {
        return new MVBenefitCreateDTO(
                UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                "BENEFIT NAME",
                new ArrayList<>()
        );
    }
}
