package com.bitozen.hms.projection.employmentletter;

import com.bitozen.hms.common.dto.CreationalSpecificationDTO;
import com.bitozen.hms.common.dto.GenericAccessTokenDTO;
import com.bitozen.hms.common.dto.MetadataDTO;
import com.bitozen.hms.pm.common.EmploymentLetterState;
import com.bitozen.hms.pm.common.EmploymentLetterStatus;
import com.bitozen.hms.common.dto.share.BizparOptimizeDTO;
import com.bitozen.hms.common.dto.share.EmployeeOptimizeDTO;
import com.bitozen.hms.pm.common.dto.query.employmentletter.VisaSpecificationDTO;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author Jeremia
 */
@Document(value = "trx_employmentletter")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class EmploymentLetterEntryProjection {
    
    @Id
    private Long id;
    
    private String elID;
    private String elDocNumber;
    private String elDocURL;
    private EmploymentLetterState elState;
    private EmploymentLetterStatus elStatus;
    private Boolean isFinalApproval;
    private String reason;
    @Temporal(TemporalType.DATE)
    private Date reqDate;
    private BizparOptimizeDTO reqType;
    private EmployeeOptimizeDTO requestor;
    private VisaSpecificationDTO visaSpec;
    private MetadataDTO metadata;
    private GenericAccessTokenDTO token;
    
    private CreationalSpecificationDTO creational;
    private String recordID;
    
    
    
}
