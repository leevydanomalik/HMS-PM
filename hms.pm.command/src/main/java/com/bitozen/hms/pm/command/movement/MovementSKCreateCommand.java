package com.bitozen.hms.pm.command.movement;

import com.bitozen.hms.pm.common.MVSKState;
import com.bitozen.hms.pm.common.MVSKStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.Date;
import java.util.List;

@Value
public class MovementSKCreateCommand {

    @TargetAggregateIdentifier
    private String mvID;
    private String employees;
}
