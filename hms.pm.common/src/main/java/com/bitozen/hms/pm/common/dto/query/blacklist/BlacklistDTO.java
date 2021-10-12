package com.bitozen.hms.pm.common.dto.query.blacklist;

import com.bitozen.hms.common.dto.CreationalSpecificationDTO;
import com.bitozen.hms.common.dto.GenericAccessTokenDTO;
import com.bitozen.hms.common.dto.MetadataDTO;
import com.bitozen.hms.common.dto.share.BizparOptimizeDTO;
import com.bitozen.hms.common.dto.share.EmployeeOptimizeDTO;
import com.bitozen.hms.pm.common.BlacklistState;
import com.bitozen.hms.pm.common.BlacklistStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BlacklistDTO implements Serializable {

    private String blacklistID;
    private String blacklistSPKNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date blacklistStartDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
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

    private String createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "GMT+7")
    private Date createdDate;
    private String updatedBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "GMT+7")
    private Date updatedDate;
    private String recordID;
}
