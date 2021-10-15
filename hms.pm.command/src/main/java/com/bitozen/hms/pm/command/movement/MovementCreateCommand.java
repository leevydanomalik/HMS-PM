package com.bitozen.hms.pm.command.movement;

import com.bitozen.hms.pm.common.MVState;
import com.bitozen.hms.pm.common.MVStatus;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.Date;
import java.util.List;

@Value
public class MovementCreateCommand {

    @TargetAggregateIdentifier
    private String mvID;
    private String mvNotes;
    private Date mvSPKDocNumber;
    private Date mvEffectiveDate;
    private Boolean isFinalApprove;
    private MVStatus mvStatus;
    private MVState mvState;
    private String mvCase;
    private String mvType;
    private String requestor;
    private String assignment;
    private String employees;
    private String mvDocs;
    private String mvBenefitBefore;
    private String mvBenefitAfter;
    private String mvFacilityBefore;
    private String mvFacilityAfter;
    private String mvPayroll;
    private String mvPosition;
    private String refRecRequest;
    private String metadata;
    private String token;

    private String createdBy;
    private Date createdDate;
    private String recordID;
}
