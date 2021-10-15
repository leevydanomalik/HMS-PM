package com.bitozen.hms.pm.common.dto.query.movement;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class MovementMemoDTOProjection implements Serializable {

    private MVMemoDTO movement;
}
