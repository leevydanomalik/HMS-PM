package com.bitozen.hms.pm.common.dto.command.movement;

import com.bitozen.hms.common.dto.CreationalSpecificationDTO;
import com.bitozen.hms.common.dto.GenericAccessTokenDTO;
import com.bitozen.hms.common.dto.MetadataCreateDTO;
import com.bitozen.hms.common.dto.MetadataDTO;
import com.bitozen.hms.common.dto.share.DocumentCreateDTO;
import com.bitozen.hms.common.dto.share.DocumentDTO;
import com.bitozen.hms.pm.common.MVState;
import com.bitozen.hms.pm.common.MVStatus;
import com.bitozen.hms.pm.common.dto.query.movement.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MovementCreateCommandDTO implements Serializable {

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
    private String refRecRequest; //GET RecruitmentRequest by ID
    private MetadataCreateDTO metadata;
    private GenericAccessTokenDTO token;
    private String createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "GMT+7")
    private Date createdDate;
    private String recordID;

    @JsonIgnore
    public MovementCreateCommandDTO getInstance() {
        return new MovementCreateCommandDTO(
                "1637909098351",
                "CES",
                new Date(),
                new Date(),
                Boolean.TRUE,
                MVStatus.INITIATE,
                MVState.MV_INITIATE,
                "MOVTYP-005-MOVCAT-002-KPNOKPNO_SC22",
                "MOVTYP-006",
                "1611128134004",
                new AssignmentCreateDTO().getInstance(),
                new ArrayList<>(),
                new ArrayList<>(),
                new MVBenefitCreateDTO().getInstance(),
                new MVBenefitCreateDTO().getInstance(),
                new MVFacilityCreateDTO().getInstance(),
                new MVFacilityCreateDTO().getInstance(),
                new MVPayrollDTO().getInstance(),
                new MVPositionCreateDTO().getInstance(),
                "1637287822067",
                new MetadataCreateDTO().getInstance(),
                new GenericAccessTokenDTO(),
                "ADMIN",
                new Date(),
                UUID.randomUUID().toString()
        );
    }
}
