package com.bitozen.hms.pm.common.dto.query.employmentletter;

import com.bitozen.hms.common.dto.GenericAccessTokenDTO;
import com.bitozen.hms.common.dto.MetadataDTO;
import com.bitozen.hms.common.dto.share.BizparOptimizeDTO;
import com.bitozen.hms.common.dto.share.EmployeeOptimizeDTO;
import com.bitozen.hms.pm.common.EmploymentLetterState;
import com.bitozen.hms.pm.common.EmploymentLetterStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author Jeremia
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmploymentLetterDTO implements Serializable{
    
    private String elID;
    private String elDocNumber;
    private String elDocURL;
    private EmploymentLetterState elState;
    private EmploymentLetterStatus elStatus;
    private Boolean isFinalApproval;
    private String reason;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date reqDate;
    private BizparOptimizeDTO reqType;
    private EmployeeOptimizeDTO requestor;
    private VisaSpecificationDTO visaSpec;
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
