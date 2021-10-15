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
public class MovementDeleteCommandDTO implements Serializable {

    private String mvID;

    private String updatedBy;
}
