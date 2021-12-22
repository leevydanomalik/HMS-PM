package com.bitozen.hms.pm.common.dto.command.termination;

import com.bitozen.hms.common.dto.GenericAccessTokenDTO;
import com.bitozen.hms.common.dto.MetadataCreateDTO;
import com.bitozen.hms.pm.common.ProlongedIllnessStatus;
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
 * @author Dumayangsari
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProlongedIllnessRegistryCreateCommandDTO implements Serializable{
    
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
    
    private String recordID;
    private String createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date createdDate;
    
    @JsonIgnore
    public  ProlongedIllnessRegistryCreateCommandDTO getInstance(){
        return new ProlongedIllnessRegistryCreateCommandDTO(
                UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                new Date(),
                new Date(),
                "PROLONGED_DOC_1638356798075_lorem-ipsum.pdf",
                "EMP-990909001",
                "EMP-990909001",
                "TSBTYP-002",
                ProlongedIllnessStatus.ACTIVE,
                "Secara tiba2",
                new MetadataCreateDTO().getInstance(),
                new GenericAccessTokenDTO(),
                UUID.randomUUID().toString(),
                "ADMIN",
                new Date()              
        );
    }
    
}
