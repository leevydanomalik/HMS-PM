package com.bitozen.hms.pm.common.dto.command.termination;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TerminationDeleteCommandDTO implements Serializable{
    
    private String tmnID;
    
    private String updatedBy;
    
}
