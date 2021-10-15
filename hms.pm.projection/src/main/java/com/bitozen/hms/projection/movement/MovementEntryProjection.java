package com.bitozen.hms.projection.movement;

import com.bitozen.hms.common.dto.CreationalSpecificationDTO;
import com.bitozen.hms.common.dto.GenericAccessTokenDTO;
import com.bitozen.hms.common.dto.MetadataDTO;
import com.bitozen.hms.common.dto.share.BizparOptimizeDTO;
import com.bitozen.hms.common.dto.share.DocumentDTO;
import com.bitozen.hms.common.dto.share.EmployeeOptimizeDTO;
import com.bitozen.hms.pm.common.MVState;
import com.bitozen.hms.pm.common.MVStatus;
import com.bitozen.hms.pm.common.dto.query.movement.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(value = "trx_movement")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class MovementEntryProjection {

    @Id
    private Long id;

    private String mvID;
    private String mvNotes;
    private Date mvSPKDocNumber;
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
    private CreationalSpecificationDTO creational;
    private String recordID;
}
