package com.bitozen.hms.pm.command.termination;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
public class TerminationDeleteCommand {
    
    @TargetAggregateIdentifier
    private String tmnID;
    
    private String updatedBy;
}
