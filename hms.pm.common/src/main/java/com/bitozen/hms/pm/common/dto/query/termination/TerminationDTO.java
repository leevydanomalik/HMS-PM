package com.bitozen.hms.pm.common.dto.query.termination;

import com.bitozen.hms.common.dto.GenericAccessTokenDTO;
import com.bitozen.hms.common.dto.MetadataDTO;
import com.bitozen.hms.common.dto.share.BizparOptimizeDTO;
import com.bitozen.hms.common.dto.share.EmployeeOptimizeDTO;
import com.bitozen.hms.pm.common.TerminationState;
import com.bitozen.hms.pm.common.TerminationStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class TerminationDTO extends BAGTMNSpec implements Serializable{
    
    private String tmnID;
    private String tmnNotes;
    private String bpjsHTDocNumber;
    private String bpjsPensionDocNumber;
    private List<String> docCopies;
    private Boolean isCancelFinalApprove;
    private Boolean isExecuted;
    private Boolean isFinalApprove;
    private String memoDocNumber;
    private EmployeeOptimizeDTO employee;
    private EmployeeOptimizeDTO requestor;
    private String skDocNumber;
    private String skdtDocNumber;
    private List<TerminationDocumentDTO> tmnDocs;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date tmnEffectiveDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date tmnReqDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date tmnPphEndDate;
    private BizparOptimizeDTO tmnReason;
    private TerminationState tmnState;
    private TerminationStatus tmnStatus;
    private MetadataDTO metadata;
    private GenericAccessTokenDTO token;
    
    private String createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "GMT+7")
    private Date createdDate;
    private String updatedBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "GMT+7")
    private Date updatedDate;
    private String recordID;

    public TerminationDTO(String tmnID, String tmnNotes, String bpjsHTDocNumber, String bpjsPensionDocNumber, List<String> docCopies, Boolean isCancelFinalApprove, Boolean isExecuted, Boolean isFinalApprove, String memoDocNumber, EmployeeOptimizeDTO employee, EmployeeOptimizeDTO requestor, String skDocNumber, String skdtDocNumber, List<TerminationDocumentDTO> tmnDocs, Date tmnEffectiveDate, Date tmnReqDate, Date tmnPphEndDate, BizparOptimizeDTO tmnReason, TerminationState tmnState, TerminationStatus tmnStatus, MetadataDTO metadata, GenericAccessTokenDTO token, String createdBy, Date createdDate, String updatedBy, Date updatedDate, String recordID, BAGPensionDTO bagPensionSpec, BAGProlongedIllnessDTO bagProlongedIllnessSpec) {
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
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.updatedBy = updatedBy;
        this.updatedDate = updatedDate;
        this.recordID = recordID;
    }
    
    
    
}
