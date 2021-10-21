package com.bitozen.hms.pm.command.employmentletter;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 *
 * @author Jeremia
 */
@Value
public class EmploymentLetterDeleteCommand {

    @TargetAggregateIdentifier

    private String elID;

    private String updatedBy;

}
