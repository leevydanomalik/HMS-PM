package com.bitozen.hms.pm.command.movement;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
public class MovementMemoChangeStateAndStatusCommand {

    @TargetAggregateIdentifier
    private String mvID;
    private String employees;
}
