package com.bitozen.hms.web.assembler;

import com.bitozen.hms.pm.common.dto.query.blacklist.BlacklistDTO;
import com.bitozen.hms.pm.common.dto.query.movement.MovementDTO;
import com.bitozen.hms.projection.movement.MovementEntryProjection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class MovementDTOAssembler {

    public MovementDTO toDTO(MovementEntryProjection domain) {
        return new MovementDTO(
                domain.getMvID(),
                domain.getMvNotes(),
                domain.getMvSPKDocNumber(),
                domain.getMvEffectiveDate(),
                domain.getIsFinalApprove(),
                domain.getMvStatus(),
                domain.getMvState(),
                domain.getMvCase(),
                domain.getMvType(),
                domain.getRequestor(),
                domain.getAssignment(),
                domain.getEmployees(),
                domain.getMvDocs(),
                domain.getMvBenefitBefore(),
                domain.getMvBenefitAfter(),
                domain.getMvFacilityBefore(),
                domain.getMvFacilityAfter(),
                domain.getMvPayroll(),
                domain.getMvPosition(),
                domain.getRefRecRequest(),
                domain.getMetadata(),
                domain.getToken(),
                domain.getCreational().getCreatedBy(),
                domain.getCreational().getCreatedDate(),
                domain.getCreational().getModifiedBy(),
                domain.getCreational().getModifiedDate(),
                domain.getRecordID()
        );
    }

    public List<MovementDTO> toDTOs(List<MovementEntryProjection> list) {
        List<MovementDTO> datas = new ArrayList<>();
        list.stream().forEach((o) -> {
            datas.add(toDTO(o));
        });
        return datas;
    }
}
