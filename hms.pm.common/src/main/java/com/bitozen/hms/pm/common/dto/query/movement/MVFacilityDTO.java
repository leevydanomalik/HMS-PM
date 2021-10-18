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
public class MVFacilityDTO implements Serializable {

    private String facilityID;
    private String facilityName;
    private List<MVFacilityDetailDTO> facilityDetails;
}
