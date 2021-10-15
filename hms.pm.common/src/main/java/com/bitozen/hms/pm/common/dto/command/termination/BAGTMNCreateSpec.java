package com.bitozen.hms.pm.common.dto.command.termination;

import com.bitozen.hms.pm.common.dto.query.termination.*;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BAGTMNCreateSpec implements Serializable{
    
    private BAGPensionDTO bagPensionSpec;
    private BAGProlongedIllnessCreateDTO bagProlongedIllnessSpec;
    
}
