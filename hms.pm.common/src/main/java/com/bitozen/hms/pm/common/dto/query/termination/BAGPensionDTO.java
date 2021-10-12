package com.bitozen.hms.pm.common.dto.query.termination;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BAGPensionDTO implements Serializable{
    
    private String achievement;
    private String proposingReason;
    private String proposingType;
    
    
}
