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
public class EmploymentLetterStateAndEmploymentLetterStatusChangeEvent {
    
    private String updatedBy;
    private Date updatedDate;
    
    private String elID;
    private EmploymentLetterState elState;
    private EmploymentLetterStatus elStatus;
    private Boolean isFinalApproval;
    
}
