package com.bitozen.hms.pm.command.movement;

import com.bitozen.hms.pm.common.MVState;
import com.bitozen.hms.pm.common.MVStatus;
import java.util.Date;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
public class MovementChangeStateAndStatusCommand {
    
    @TargetAggregateIdentifier
    private String mvID;
    private MVStatus mvStatus;
    private MVState mvState;
    private Boolean isFinalApprove;
    
    private String updatedBy;
    private Date updatedDate;
    
}
