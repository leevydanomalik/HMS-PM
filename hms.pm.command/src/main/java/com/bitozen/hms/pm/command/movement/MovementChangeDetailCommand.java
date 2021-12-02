package com.bitozen.hms.pm.command.movement;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.Date;

@Value
public class MovementChangeDetailCommand {

    @TargetAggregateIdentifier
    private String mvID;
    private String employees;

    private String updatedBy;
    private Date updatedDate;
}
