package com.bitozen.hms.pm.event.termination;


import com.bitozen.hms.pm.common.ProlongedIllnessStatus;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *
 * @author Dumayangsari
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProlongedIllnessRegistryCreateEvent {
    
    private String recordID;
    private String createdBy;
    private Date createdDate;
    
    private String piID;
    private Date startDate;
    private Date endDate;
    private String docURL;
    private String employee;
    private String requestor;
    private String ilnessType;
    private ProlongedIllnessStatus piStatus;
    private String reason;
    private String metadata;
    private String token;
    
}
