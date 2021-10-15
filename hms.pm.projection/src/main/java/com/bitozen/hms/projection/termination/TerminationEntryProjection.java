package com.bitozen.hms.projection.termination;

import com.bitozen.hms.common.dto.CreationalSpecificationDTO;
import com.bitozen.hms.common.dto.GenericAccessTokenDTO;
import com.bitozen.hms.common.dto.MetadataDTO;
import com.bitozen.hms.common.dto.share.BizparOptimizeDTO;
import com.bitozen.hms.common.dto.share.EmployeeOptimizeDTO;
import com.bitozen.hms.pm.common.TerminationState;
import com.bitozen.hms.pm.common.TerminationStatus;
import com.bitozen.hms.pm.common.dto.query.termination.BAGPensionDTO;
import com.bitozen.hms.pm.common.dto.query.termination.BAGProlongedIllnessDTO;
import com.bitozen.hms.pm.common.dto.query.termination.BAGTMNSpec;
import com.bitozen.hms.pm.common.dto.query.termination.TerminationDocumentDTO;
import java.util.Date;
import java.util.List;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(value = "trx_termination")
@NoArgsConstructor
@Data
@ToString
public class TerminationEntryProjection extends BAGTMNSpec {
    
    @Id
    private Long id;
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
    @Temporal(TemporalType.DATE)
    private Date tmnEffectiveDate;
    @Temporal(TemporalType.DATE)
    private Date tmnReqDate;
    @Temporal(TemporalType.DATE)
    private Date tmnPphEndDate;
    private BizparOptimizeDTO tmnReason;
    private TerminationState tmnState;
    private TerminationStatus tmnStatus;
    private MetadataDTO metadata;
    private GenericAccessTokenDTO token;
    
    private CreationalSpecificationDTO creational;
    private String recordID;
    
    

    public TerminationEntryProjection(Long id, String tmnID, String tmnNotes, String bpjsHTDocNumber, String bpjsPensionDocNumber, List<String> docCopies, Boolean isCancelFinalApprove, Boolean isExecuted, Boolean isFinalApprove, String memoDocNumber, EmployeeOptimizeDTO employee, EmployeeOptimizeDTO requestor, String skDocNumber, String skdtDocNumber, List<TerminationDocumentDTO> tmnDocs, Date tmnEffectiveDate, Date tmnReqDate, Date tmnPphEndDate, BizparOptimizeDTO tmnReason, TerminationState tmnState, TerminationStatus tmnStatus, MetadataDTO metadata, GenericAccessTokenDTO token, CreationalSpecificationDTO creational, String recordID, BAGPensionDTO bagPensionSpec, BAGProlongedIllnessDTO bagProlongedIllnessSpec) {
        super(bagPensionSpec, bagProlongedIllnessSpec);
        this.id = id;
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
        this.creational = creational;
        this.recordID = recordID;
    }
    
    
    
    
    
}
