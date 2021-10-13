package com.bitozen.hms.pm.command.termination;

import com.bitozen.hms.pm.common.TerminationState;
import com.bitozen.hms.pm.common.TerminationStatus;
import java.util.Date;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
public class TerminationCreateCommand {
    
    @TargetAggregateIdentifier
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
    
    private String createdBy;
    private Date createdDate;
    private String recordID;
    
}
