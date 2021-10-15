package com.bitozen.hms.pm.common.dto.command.termination;

import com.bitozen.hms.common.dto.GenericAccessTokenDTO;
import com.bitozen.hms.common.dto.MetadataCreateDTO;
import com.bitozen.hms.pm.common.TerminationState;
import com.bitozen.hms.pm.common.TerminationStatus;
import com.bitozen.hms.pm.common.dto.query.termination.BAGPensionDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class TerminationChangeCommandDTO extends BAGTMNCreateSpec implements Serializable{
    
    private String tmnID;
    private String tmnNotes;
    private String bpjsHTDocNumber;
    private String bpjsPensionDocNumber;
    private List<String> docCopies;
    private Boolean isCancelFinalApprove;
    private Boolean isExecuted;
    private Boolean isFinalApprove;
    private String memoDocNumber;
    private String employee;
    private String requestor;
    private String skDocNumber;
    private String skdtDocNumber;
    private List<TerminationDocumentCreateDTO> tmnDocs;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date tmnEffectiveDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date tmnReqDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date tmnPphEndDate;
    private String tmnReason;
    private TerminationState tmnState;
    private TerminationStatus tmnStatus;
    private MetadataCreateDTO metadata;
    private GenericAccessTokenDTO token;
    
    private String updatedBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "GMT+7")
    private Date updatedDate;

    
    public TerminationChangeCommandDTO(String tmnID, String tmnNotes, String bpjsHTDocNumber, String bpjsPensionDocNumber, List<String> docCopies, Boolean isCancelFinalApprove, Boolean isExecuted, Boolean isFinalApprove, String memoDocNumber, String employee, String requestor, String skDocNumber, String skdtDocNumber, List<TerminationDocumentCreateDTO> tmnDocs, Date tmnEffectiveDate, Date tmnReqDate, Date tmnPphEndDate, String tmnReason, TerminationState tmnState, TerminationStatus tmnStatus, MetadataCreateDTO metadata, GenericAccessTokenDTO token, String updatedBy, Date updatedDate, BAGPensionDTO bagPensionSpec, BAGProlongedIllnessCreateDTO bagProlongedIllnessSpec) {
        super(bagPensionSpec, bagProlongedIllnessSpec);
        this.tmnID = tmnID;
        this.tmnNotes = tmnNotes;
        this.bpjsHTDocNumber = bpjsHTDocNumber;
        this.bpjsPensionDocNumber = bpjsPensionDocNumber;
        this.docCopies = docCopies;
        this.isCancelFinalApprove = isCancelFinalApprove;
        this.isExecuted = isExecuted;
        this.isFinalApprove = isFinalApprove;
        this.memoDocNumber = memoDocNumber;
        this.employee = employee;
        this.requestor = requestor;
        this.skDocNumber = skDocNumber;
        this.skdtDocNumber = skdtDocNumber;
        this.tmnDocs = tmnDocs;
        this.tmnEffectiveDate = tmnEffectiveDate;
        this.tmnReqDate = tmnReqDate;
        this.tmnPphEndDate = tmnPphEndDate;
        this.tmnReason = tmnReason;
        this.tmnState = tmnState;
        this.tmnStatus = tmnStatus;
        this.metadata = metadata;
        this.token = token;
        this.updatedBy = updatedBy;
        this.updatedDate = updatedDate;
    }
    
    
    
}
