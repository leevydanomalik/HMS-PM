package com.bitozen.hms.pm.common.dto.command.movement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MVFacilityDetailCreateDTO implements Serializable {

    private String detailID;
    private Integer detailQty;
    private String detailType;
    private String detailSubtype;
}
