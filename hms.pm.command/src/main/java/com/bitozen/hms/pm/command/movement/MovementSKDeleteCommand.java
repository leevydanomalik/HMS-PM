package com.bitozen.hms.pm.command.movement;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.Date;

@Value
public class MovementSKDeleteCommand {

    @TargetAggregateIdentifier
    private String mvID;
    private String employees;
}
