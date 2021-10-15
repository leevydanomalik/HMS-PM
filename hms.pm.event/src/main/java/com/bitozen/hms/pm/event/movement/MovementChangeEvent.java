package com.bitozen.hms.pm.event.movement;

import com.bitozen.hms.pm.common.MVState;
import com.bitozen.hms.pm.common.MVStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MovementChangeEvent {

    private String updatedBy;
    private Date updatedDate;

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
}
