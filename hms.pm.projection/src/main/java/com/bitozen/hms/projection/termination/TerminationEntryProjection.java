package com.bitozen.hms.projection.termination;

import com.bitozen.hms.common.dto.CreationalSpecificationDTO;
import com.bitozen.hms.common.dto.GenericAccessTokenDTO;
import com.bitozen.hms.common.dto.MetadataDTO;
import com.bitozen.hms.common.dto.share.BizparOptimizeDTO;
import com.bitozen.hms.common.dto.share.EmployeeOptimizeDTO;
import com.bitozen.hms.pm.common.TerminationState;
import com.bitozen.hms.pm.common.TerminationStatus;
import com.bitozen.hms.pm.common.dto.query.termination.BAGPensionDTO;
import com.bitozen.hms.pm.common.dto.query.termination.TerminationDocumentDTO;
import java.util.Date;
import java.util.List;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(value = "trx_termination")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class TerminationEntryProjection {
    
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
    private BAGPensionDTO tmnPension;
    private MetadataDTO metadata;
    private GenericAccessTokenDTO token;
    
    private CreationalSpecificationDTO creational;
    private String recordID;
    
    
    
}
