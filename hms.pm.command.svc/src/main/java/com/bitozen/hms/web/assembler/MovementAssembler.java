package com.bitozen.hms.web.assembler;

import com.bitozen.hms.common.dto.GenericResponseDTO;
import com.bitozen.hms.common.dto.share.ESOptimizeDTO;
import com.bitozen.hms.common.dto.share.PositionOptimizeDTO;
import com.bitozen.hms.common.status.ResponseStatus;
import com.bitozen.hms.pm.common.dto.command.movement.*;
import com.bitozen.hms.pm.common.dto.query.movement.*;
import com.bitozen.hms.recruitment.common.dto.query.RecruitmentRequestDTO;
import com.bitozen.hms.web.helper.BizparHelper;
import com.bitozen.hms.web.helper.ESHelper;
import com.bitozen.hms.web.helper.EmployeeHelper;
import com.bitozen.hms.web.helper.RecruitmentHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class MovementAssembler {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ESHelper esHelper;

    @Autowired
    private BizparHelper bizHelper;

    @Autowired
    private EmployeeHelper empHelper;

    @Autowired
    private RecruitmentHelper recHelper;

    public AssignmentDTO toAssignmentDTO(AssignmentCreateDTO dto) {
        return new AssignmentDTO(
                dto.getAchievement(),
                dto.getAssignmentStartDate(),
                dto.getAssignmentEndDate(),
                esHelper.findEsByID(dto.getAssignmentPlace()),
                bizHelper.convertBizpar(dto.getAssignmentType()),
                dto.getReasonOfProposing()
        );
    }

    public MVEmployeeDTO toMVEmployeeDTO(MVEmployeeCreateDTO dto) {
        return new MVEmployeeDTO(
                dto.getMvDetailID(),
                empHelper.findEmployeeOptimizeByKey(dto.getEmployee()),
                toSKDTOs(dto.getSks()),
                toMemoDTOs(dto.getMemos()),
                dto.getPkwts()
        );
    }

    public List<MVEmployeeDTO> toMVEmployeeDTOs(List<MVEmployeeCreateDTO> dtos) {
        List<MVEmployeeDTO> datas = new ArrayList<>();
        dtos.stream().forEach((o) -> {
            datas.add(toMVEmployeeDTO(o));
        });
        return datas;
    }

    public MVSKDTO toSKDTO(MVSKCreateDTO dto) {
        return new MVSKDTO(
                dto.getSkID(),
                dto.getSkRefID(),
                dto.getSkDocNumber(),
                dto.getSkStatus(),
                dto.getSkState(),
                bizHelper.convertBizpar(dto.getSkType()),
                empHelper.findEmployeeOptimizeByKey(dto.getRequestor()),
                dto.getIsRevoke(),
                dto.getIsFinalApprove(),
                dto.getSkCopies(),
                dto.getSkConsiderDesc(),
                dto.getRequestDate()
        );
    }

    public List<MVSKDTO> toSKDTOs(List<MVSKCreateDTO> dtos) {
        List<MVSKDTO> datas = new ArrayList<>();
        dtos.stream().forEach((o) -> {
            datas.add(toSKDTO(o));
        });
        return datas;
    }

    public MVMemoDTO toMemoDTO(MVMemoCreateDTO dto) {
        return new MVMemoDTO(
                dto.getMemoID(),
                dto.getMemoRefID(),
                dto.getMemoDocNumber(),
                dto.getIsRevoke(),
                dto.getIsFinalApprove(),
                dto.getMemoStatus(),
                dto.getMemoState(),
                bizHelper.convertBizpar(dto.getMemoType()),
                empHelper.findEmployeeOptimizeByKey(dto.getRequestor()),
                dto.getRequestDate()
        );
    }

    public List<MVMemoDTO> toMemoDTOs(List<MVMemoCreateDTO> dtos) {
        List<MVMemoDTO> datas = new ArrayList<>();
        dtos.stream().forEach((o) -> {
            datas.add(toMemoDTO(o));
        });
        return datas;
    }

    public MVBenefitDTO toBenefitDTO(MVBenefitCreateDTO dto) {
        return new MVBenefitDTO(
                dto.getBenefitID(),
                dto.getBenefitName(),
                toBenefitDetailDTOs(dto.getBenefitDetails())
        );
    }

    public MVBenefitDetailDTO toBenefitDetailDTO(MVBenefitDetailCreateDTO dto) {
        return new MVBenefitDetailDTO(
                dto.getDetailID(),
                bizHelper.convertBizpar(dto.getDetailType()),
                bizHelper.convertBizpar(dto.getDetailSubtype()),
                dto.getDetailValue()
        );
    }

    public List<MVBenefitDetailDTO> toBenefitDetailDTOs(List<MVBenefitDetailCreateDTO> dtos) {
        List<MVBenefitDetailDTO> datas = new ArrayList<>();
        dtos.stream().forEach((o) -> {
            datas.add(toBenefitDetailDTO(o));
        });
        return datas;
    }

    public MVFacilityDTO toFacilityDTO(MVFacilityCreateDTO dto) {
        return new MVFacilityDTO(
                dto.getFacilityID(),
                dto.getFacilityName(),
                toFacilityDetailDTOs(dto.getFacilityDetails())
        );
    }

    public MVFacilityDetailDTO toFacilityDetailDTO(MVFacilityDetailCreateDTO dto) {
        return new MVFacilityDetailDTO(
                dto.getDetailID(),
                dto.getDetailQty(),
                bizHelper.convertBizpar(dto.getDetailType()),
                bizHelper.convertBizpar(dto.getDetailSubtype())
        );
    }

    public List<MVFacilityDetailDTO> toFacilityDetailDTOs(List<MVFacilityDetailCreateDTO> dtos) {
        List<MVFacilityDetailDTO> datas = new ArrayList<>();
        dtos.stream().forEach((o) -> {
            datas.add(toFacilityDetailDTO(o));
        });
        return datas;
    }

    public MVPositionDTO toPositionDTO(MVPositionCreateDTO dto) {
        return new MVPositionDTO(
                dto.getEffectiveStartDateAfter(),
                dto.getEffectiveEndDateAfter(),
                esHelper.findEsByID(dto.getEsBefore()),
                esHelper.findEsByID(dto.getEsAfter()),
                esHelper.findESByOUID(dto.getEsBefore(), dto.getPositionBefore()),
                esHelper.findESByOUID(dto.getEsAfter(), dto.getPositionAfter()),
                bizHelper.convertBizpar(dto.getLevelBefore()),
                bizHelper.convertBizpar(dto.getLevelAfter()),
                bizHelper.convertBizpar(dto.getGradeBefore()),
                bizHelper.convertBizpar(dto.getGradeBefore())
        );
    }

    public RecRequestRefDTO toRecRequestDTO(String refRecRequest) {
        GenericResponseDTO<RecruitmentRequestDTO> recReq = recHelper.findRecruitmentRequestByID(refRecRequest);
        if(recReq.getStatus().equals(ResponseStatus.S) && recReq.getCode().equals("201")) {
            RecruitmentRequestDTO data = objectMapper.convertValue(recReq.getData(), RecruitmentRequestDTO.class);
            return new RecRequestRefDTO(
                    data.getRecruitmentRequestID(),
                    data.getMpp().getMppID(),
                    esHelper.findEsByID(data.getMpp().getEsid()),
                    data.getRecruitmentCategory().getValue(),
                    data.getRecruitmentCategory().getKey(),
                    new PositionOptimizeDTO(data.getMpp().getOuid(), data.getMpp().getOuKey(), data.getMpp().getOuName()),
                    data.getRecruitmentRequestType().getValue(),
                    data.getRecruitmentRequestType().getKey(),
                    data.getRecruitmentType().getValue(),
                    data.getRecruitmentType().getKey()
            );
        } else {
            return null;
        }

    }

    public MVEmployeeDTO toSKDTOPostRequest(MVSKCreateCommandDTO dto) {
        MVEmployeeDTO empDTO = new MVEmployeeDTO();
        empDTO.setMvDetailID(dto.getMvDetailID());
        empDTO.setSks(new ArrayList<>(Arrays.asList(
                new MVSKDTO(
                        dto.getSkID(),
                        dto.getSkRefID(),
                        dto.getSkDocNumber(),
                        dto.getSkStatus(),
                        dto.getSkState(),
                        bizHelper.convertBizpar(dto.getSkType()),
                        empHelper.findEmployeeOptimizeByKey(dto.getRequestor()),
                        dto.getIsRevoke(),
                        dto.getIsFinalApprove(),
                        dto.getSkCopies(),
                        dto.getSkConsiderDesc(),
                        dto.getRequestDate()
        ))));
        return empDTO;
    }

    public MVEmployeeDTO toSKDTODeleteRequest(MVSKDeleteCommandDTO dto) {
        MVEmployeeDTO empDTO = new MVEmployeeDTO();
        empDTO.setMvDetailID(dto.getMvDetailID());
        empDTO.setSks(new ArrayList<>(Arrays.asList(
                new MVSKDTO(
                        dto.getSkID(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        dto.getRequestDate()
                ))));
        return empDTO;
    }

    public MVEmployeeDTO toMemoDTOPostRequest(MVMemoCreateCommandDTO dto) {
        MVEmployeeDTO empDTO = new MVEmployeeDTO();
        empDTO.setMvDetailID(dto.getMvDetailID());
        empDTO.setMemos(new ArrayList<>(Arrays.asList(
                new MVMemoDTO(
                        dto.getMemoID(),
                        dto.getMemoRefID(),
                        dto.getMemoDocNumber(),
                        dto.getIsRevoke(),
                        dto.getIsFinalApprove(),
                        dto.getMemoStatus(),
                        dto.getMemoState(),
                        bizHelper.convertBizpar(dto.getMemoType()),
                        empHelper.findEmployeeOptimizeByKey(dto.getRequestor()),
                        dto.getRequestDate()
                ))));
        return empDTO;
    }

    public MVEmployeeDTO toMemoDTODeleteRequest(MVMemoDeleteCommandDTO dto) {
        MVEmployeeDTO empDTO = new MVEmployeeDTO();
        empDTO.setMvDetailID(dto.getMvDetailID());
        empDTO.setMemos(new ArrayList<>(Arrays.asList(
                new MVMemoDTO(
                        dto.getMemoID(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        dto.getRequestDate()
                ))));
        return empDTO;
    }
}
