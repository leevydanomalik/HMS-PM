package com.bitozen.hms.pm.common.dto.command.movement;

import com.bitozen.hms.common.dto.GenericAccessTokenDTO;
import com.bitozen.hms.common.dto.MetadataCreateDTO;
import com.bitozen.hms.common.dto.share.DocumentCreateDTO;
import com.bitozen.hms.pm.common.MVState;
import com.bitozen.hms.pm.common.MVStatus;
import com.bitozen.hms.pm.common.dto.query.movement.MVPayrollDTO;
import com.bitozen.hms.pm.common.dto.query.movement.MVPositionDTO;
import com.bitozen.hms.pm.common.dto.query.movement.RecRequestRefDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MovementChangeCommandDTO implements Serializable {

    private String mvID;
    private String mvNotes;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date mvSPKDocNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date mvEffectiveDate;
    private Boolean isFinalApprove;
    private MVStatus mvStatus;
    private MVState mvState;
    private String mvCase;
    private String mvType;
    private String requestor;
    private AssignmentCreateDTO assignment;
    private List<MVEmployeeCreateDTO> employees;
    private List<DocumentCreateDTO> mvDocs;
    private MVBenefitCreateDTO mvBenefitBefore;
    private MVBenefitCreateDTO mvBenefitAfter;
    private MVFacilityCreateDTO mvFacilityBefore;
    private MVFacilityCreateDTO mvFacilityAfter;
    private MVPayrollDTO mvPayroll;
    private MVPositionCreateDTO mvPosition;
    private String refRecRequest;
    private MetadataCreateDTO metadata;
    private GenericAccessTokenDTO token;

    private String updatedBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "GMT+7")
    private Date updatedDate;
}
