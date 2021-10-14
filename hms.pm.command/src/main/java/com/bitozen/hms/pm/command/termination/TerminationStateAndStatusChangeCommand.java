package com.bitozen.hms.pm.command.termination;

import com.bitozen.hms.pm.common.TerminationState;
import com.bitozen.hms.pm.common.TerminationStatus;
import java.util.Date;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 *
 * @author Dumayangsari
 */
@Value
public class TerminationStateAndStatusChangeCommand {
    @TargetAggregateIdentifier
    private String tmnID;
    private TerminationState tmnState;
    private TerminationStatus tmnStatus;
    
    private String updatedBy;
    private Date updatedDate;
}
