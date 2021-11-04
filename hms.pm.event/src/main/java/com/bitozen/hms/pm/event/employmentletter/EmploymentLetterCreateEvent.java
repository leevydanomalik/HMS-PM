package com.bitozen.hms.pm.event.employmentletter;

import com.bitozen.hms.pm.common.EmploymentLetterState;
import com.bitozen.hms.pm.common.EmploymentLetterStatus;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *
 * @author Jeremia
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EmploymentLetterCreateEvent {
    
    private String createdBy;
    private Date createdDate;
    private String recordID;
    
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
    
}
