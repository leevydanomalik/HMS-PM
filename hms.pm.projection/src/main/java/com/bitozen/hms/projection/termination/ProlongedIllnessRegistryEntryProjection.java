package com.bitozen.hms.projection.termination;

import com.bitozen.hms.common.dto.CreationalSpecificationDTO;
import com.bitozen.hms.common.dto.GenericAccessTokenDTO;
import com.bitozen.hms.common.dto.MetadataDTO;
import com.bitozen.hms.common.dto.share.BizparOptimizeDTO;
import com.bitozen.hms.common.dto.share.EmployeeOptimizeDTO;
import com.bitozen.hms.pm.common.ProlongedIllnessStatus;
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
 * @author Dumayangsari
 */
@Document(value = "trx_prolongedillness")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class ProlongedIllnessRegistryEntryProjection {
    
    @Id
    private Long id;
    
    private String piID;
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Temporal(TemporalType.DATE)
    private Date endDate;
    private String docURL;
    private EmployeeOptimizeDTO employee;
    private EmployeeOptimizeDTO requestor;
    private BizparOptimizeDTO ilnessType;
    private ProlongedIllnessStatus piStatus;
    private String reason;
    private MetadataDTO metadata;
    private GenericAccessTokenDTO token;
    
    private String recordID;
    private CreationalSpecificationDTO creational;
    
}
