package com.bitozen.hms.pm.common.dto.query.movement;

import com.bitozen.hms.common.dto.GenericAccessTokenDTO;
import com.bitozen.hms.common.dto.MetadataDTO;
import com.bitozen.hms.common.dto.share.BizparOptimizeDTO;
import com.bitozen.hms.common.dto.share.DocumentDTO;
import com.bitozen.hms.common.dto.share.EmployeeOptimizeDTO;
import com.bitozen.hms.pm.common.MVState;
import com.bitozen.hms.pm.common.MVStatus;
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
public class MovementDTO implements Serializable {

    private String mvID;
    private String mvNotes;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date mvSPKDocNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date mvEffectiveDate;
    private Boolean isFinalApprove;
    private MVStatus mvStatus;
    private MVState mvState;
    private BizparOptimizeDTO mvCase;
    private BizparOptimizeDTO mvType;
    private EmployeeOptimizeDTO requestor;
    private AssignmentDTO assignment;
    private List<MVEmployeeDTO> employees;
    private List<DocumentDTO> mvDocs;
    private MVBenefitDTO mvBenefitBefore;
    private MVBenefitDTO mvBenefitAfter;
    private MVFacilityDTO mvFacilityBefore;
    private MVFacilityDTO mvFacilityAfter;
    private MVPayrollDTO mvPayroll;
    private MVPositionDTO mvPosition;
    private RecRequestRefDTO refRecRequest;
    private MetadataDTO metadata;
    private GenericAccessTokenDTO token;

    private String createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "GMT+7")
    private Date createdDate;
    private String updatedBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "GMT+7")
    private Date updatedDate;
    private String recordID;
}
