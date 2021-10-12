package com.bitozen.hms.projection.blacklist;

import com.bitozen.hms.common.dto.CreationalSpecificationDTO;
import com.bitozen.hms.common.dto.GenericAccessTokenDTO;
import com.bitozen.hms.common.dto.MetadataDTO;
import com.bitozen.hms.common.dto.share.BizparOptimizeDTO;
import com.bitozen.hms.common.dto.share.EmployeeOptimizeDTO;
import com.bitozen.hms.pm.common.BlacklistState;
import com.bitozen.hms.pm.common.BlacklistStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Document(value = "trx_blacklist")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class BlacklistEntryProjection {

    @Id
    private Long id;

    private String blacklistID;
    private String blacklistSPKNumber;
    @Temporal(TemporalType.DATE)
    private Date blacklistStartDate;
    @Temporal(TemporalType.DATE)
    private Date blacklistEndDate;
    private String blacklistNotes;
    private BizparOptimizeDTO blacklistType;
    private Boolean isPermanent;
    private EmployeeOptimizeDTO employee;
    private EmployeeOptimizeDTO requestor;
    private String blacklistDocURL;
    private Boolean isFinalApprove;
    private BlacklistStatus blacklistStatus;
    private BlacklistState blacklistState;
    private String compRegulationChapter;
    private String compRegulationChapterDesc;
    private MetadataDTO metadata;
    private GenericAccessTokenDTO token;

    private CreationalSpecificationDTO creational;
    private String recordID;
}
