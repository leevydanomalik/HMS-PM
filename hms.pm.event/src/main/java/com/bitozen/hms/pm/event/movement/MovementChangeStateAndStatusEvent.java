package com.bitozen.hms.pm.event.movement;

import com.bitozen.hms.pm.common.MVState;
import com.bitozen.hms.pm.common.MVStatus;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MovementChangeStateAndStatusEvent {
    
    private String mvID;
    private MVStatus mvStatus;
    private MVState mvState;
    private Boolean isFinalApprove;
    
    private String updatedBy;
    private Date updatedDate;
    
}
