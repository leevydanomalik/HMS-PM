package com.bitozen.hms.pm.command.blacklist;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
public class BlacklistDeleteCommand {

    @TargetAggregateIdentifier
    private String blacklistID;

    private String updatedBy;
}
