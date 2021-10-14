package com.bitozen.hms.pm.command.termination;


import com.bitozen.hms.pm.common.ProlongedIllnessStatus;
import java.util.Date;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 *
 * @author Dumayangsari
 */
@Value
public class ProlongedIllnessRegistryCreateCommand {
    
    @TargetAggregateIdentifier
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
    
    private String recordID;
    private String createdBy;
    private Date createdDate;
}
