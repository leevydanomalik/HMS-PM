package com.bitozen.hms.pm.command.termination;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 *
 * @author Dumayangsari
 */
@Value
public class ProlongedIllnessRegistryDeleteCommand {
     
    @TargetAggregateIdentifier
    private String piID;

    private String updatedBy;
    
}
