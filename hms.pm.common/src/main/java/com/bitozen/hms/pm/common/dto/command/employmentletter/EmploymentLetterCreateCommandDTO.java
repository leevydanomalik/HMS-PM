package com.bitozen.hms.pm.common.dto.command.employmentletter;

import com.bitozen.hms.common.dto.GenericAccessTokenDTO;
import com.bitozen.hms.common.dto.MetadataCreateDTO;
import com.bitozen.hms.pm.common.EmploymentLetterState;
import com.bitozen.hms.pm.common.EmploymentLetterStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
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
public class EmploymentLetterCreateCommandDTO implements Serializable {

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

    private String createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "GMT+7")
    private Date createdDate;
    private String recordID;

    @JsonIgnore
    public EmploymentLetterCreateCommandDTO getInstance() {
        return new EmploymentLetterCreateCommandDTO(
                UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                "elDocNumber",
                "elDocURL",
                EmploymentLetterState.EL_INITIATE,
                EmploymentLetterStatus.INITIATE,
                Boolean.TRUE,
                "reason",
                new Date(),
                "KEY001",
                "64BDEEB8",
                new VisaSpecificationCreateDTO().getInstance(),
                new MetadataCreateDTO().getInstance(),
                new GenericAccessTokenDTO(),
                "ADMIN",
                new Date(),
                UUID.randomUUID().toString()
        );
    }

}
