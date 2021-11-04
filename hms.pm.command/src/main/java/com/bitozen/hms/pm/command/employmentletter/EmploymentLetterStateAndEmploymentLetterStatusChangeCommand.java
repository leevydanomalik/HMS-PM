package com.bitozen.hms.pm.command.employmentletter;

import com.bitozen.hms.pm.common.EmploymentLetterState;
import com.bitozen.hms.pm.common.EmploymentLetterStatus;
import java.util.Date;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 *
 * @author Jeremia
 */
@Value
public class EmploymentLetterStateAndEmploymentLetterStatusChangeCommand {
    
    @TargetAggregateIdentifier
    private String elID;
    private EmploymentLetterState elState;
    private EmploymentLetterStatus elStatus;
    private Boolean isFinalApproval;
    
    private String updatedBy;
    private Date updatedDate;
    
}
