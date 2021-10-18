package com.bitozen.hms.pm.common.dto.command.termination;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author Dumayangsari
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProlongedIllnessRegistryDeleteCommandDTO implements Serializable{
    
    private String piID;

    private String updatedBy;
}
