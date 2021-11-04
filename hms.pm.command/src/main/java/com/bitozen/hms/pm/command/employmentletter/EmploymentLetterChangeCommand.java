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
public class EmploymentLetterChangeCommand {
    
    @TargetAggregateIdentifier
    private String elID;
    private String elDocNumber;
    private String elDocURL;
    private EmploymentLetterState elState;
    private EmploymentLetterStatus elStatus;
    private Boolean isFinalApproval;
    private String reason;
    private Date reqDate;
    private String reqType;
    private String requestor;
    private String visaSpec;
    private String metadata;
    private String token;
    
    private String updatedBy;
    private Date updatedDate;
    
}
