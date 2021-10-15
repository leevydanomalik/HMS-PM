package com.bitozen.hms.pm.common.dto.query.termination;

import com.bitozen.hms.common.dto.share.BizparOptimizeDTO;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BAGProlongedIllnessDTO implements Serializable {
    
    private BizparOptimizeDTO plDuration;
    
}
