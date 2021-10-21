package com.bitozen.hms.pm.common.dto.command.employmentletter;

import com.bitozen.hms.common.dto.GenericAccessTokenDTO;
import com.bitozen.hms.common.dto.MetadataCreateDTO;
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
public class EmploymentLetterChangeCommandDTO implements Serializable {
    
    private String elID;
    private String elDocNumber;
    private String elDocURL;
    private EmploymentLetterState elState;
    private EmploymentLetterStatus elStatus;
    private Boolean isFinalApproval;
    private String reason;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date reqDate;
    private String reqType;
    private String requestor;
    private VisaSpecificationCreateDTO visaSpec;
    private MetadataCreateDTO metadata;
    private GenericAccessTokenDTO token;
    
    private String updatedBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "GMT+7")
    private Date updatedDate;

    
}
