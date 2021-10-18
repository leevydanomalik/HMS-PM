package com.bitozen.hms.pm.common.dto.command.movement;

import com.bitozen.hms.pm.common.dto.query.movement.MVFacilityDetailDTO;
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
public class MVFacilityCreateDTO implements Serializable {

    private String facilityID;
    private String facilityName;
    private List<MVFacilityDetailCreateDTO> facilityDetails;

    @JsonIgnore
    public MVFacilityCreateDTO getInstance() {
        return new MVFacilityCreateDTO(
                UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                "FACILITY NAME",
                new ArrayList<>()
        );
    }
}
