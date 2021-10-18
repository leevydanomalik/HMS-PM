package com.bitozen.hms.pm.common.dto.command.termination;

import com.bitozen.hms.common.dto.GenericAccessTokenDTO;
import com.bitozen.hms.common.dto.MetadataCreateDTO;
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
public class ProlongedIllnessRegistryChangeCommandDTO implements Serializable{
    
    private String piID;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date endDate;
    private String docURL;
    private String employee;
    private String requestor;
    private String ilnessType;
    private ProlongedIllnessStatus piStatus;
    private String reason;
    private MetadataCreateDTO metadata;
    private GenericAccessTokenDTO token;
    
    private String updatedBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date updatedDate;
    
}
