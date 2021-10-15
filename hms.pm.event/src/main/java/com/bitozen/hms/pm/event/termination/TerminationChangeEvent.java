package com.bitozen.hms.pm.event.termination;

import com.bitozen.hms.pm.common.TerminationState;
import com.bitozen.hms.pm.common.TerminationStatus;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TerminationChangeEvent {
    
    private String tmnID;
    private String tmnNotes;
    private String bpjsHTDocNumber;
    private String bpjsPensionDocNumber;
    private String docCopies;
    private Boolean isCancelFinalApprove;
    private Boolean isExecuted;
    private Boolean isFinalApprove;
    private String memoDocNumber;
    private String employee;
    private String requestor;
    private String skDocNumber;
    private String skdtDocNumber;
    private String tmnDocs;
    private Date tmnEffectiveDate;
    private Date tmnReqDate;
    private Date tmnPphEndDate;
    private String tmnReason;
    private TerminationState tmnState;
    private TerminationStatus tmnStatus;
    private String metadata;
    private String token;
    private String bagPensionSpec;
    private String bagProlongedIllnessSpec;
    
    private String updatedBy;
    private Date updatedDate;
    
}
