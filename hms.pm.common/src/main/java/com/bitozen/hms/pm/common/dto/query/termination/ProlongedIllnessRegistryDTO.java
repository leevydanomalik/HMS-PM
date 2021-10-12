package com.bitozen.hms.pm.common.dto.query.termination;

import com.bitozen.hms.common.dto.GenericAccessTokenDTO;
import com.bitozen.hms.common.dto.MetadataDTO;
import com.bitozen.hms.common.dto.share.BizparOptimizeDTO;
import com.bitozen.hms.common.dto.share.EmployeeOptimizeDTO;
import com.bitozen.hms.pm.common.ProlongedIllnessStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author Dumayangsari
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProlongedIllnessRegistryDTO implements Serializable{
    
    private String piID;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
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
    private String createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "GMT+7")
    private Date createdDate;
    private String updatedBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "GMT+7")
    private Date updatedDate;
    
}
