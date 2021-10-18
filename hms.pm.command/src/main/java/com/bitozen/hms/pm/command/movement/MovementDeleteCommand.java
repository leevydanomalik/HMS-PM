package com.bitozen.hms.pm.command.movement;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
public class MovementDeleteCommand {

    @TargetAggregateIdentifier
    private String mvID;

    private String updatedBy;
}
