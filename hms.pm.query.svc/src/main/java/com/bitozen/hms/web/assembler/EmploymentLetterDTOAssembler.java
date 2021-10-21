package com.bitozen.hms.web.assembler;

import com.bitozen.hms.pm.common.dto.query.employmentletter.EmploymentLetterDTO;
import com.bitozen.hms.projection.employmentletter.EmploymentLetterEntryProjection;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 *
 * @author Jeremia
 */
@Component
@Slf4j
public class EmploymentLetterDTOAssembler {

    public EmploymentLetterDTO toDTO(EmploymentLetterEntryProjection domain) {
        return new EmploymentLetterDTO(
                domain.getElID(),
                domain.getElDocNumber(),
                domain.getElDocURL(),
                domain.getElState(),
                domain.getElStatus(),
                domain.getIsFinalApproval(),
                domain.getReason(),
                domain.getReqDate(),
                domain.getReqType(),
                domain.getRequestor(),
                domain.getVisaSpec(),
                domain.getMetadata(),
                domain.getToken(),
                domain.getCreational().getCreatedBy(),
                domain.getCreational().getCreatedDate(),
                domain.getCreational().getModifiedBy(),
                domain.getCreational().getModifiedDate(),
                domain.getRecordID()
        );
    }

    public List<EmploymentLetterDTO> toDTOs(List<EmploymentLetterEntryProjection> list) {
        List<EmploymentLetterDTO> datas = new ArrayList<>();
        list.stream().forEach((o) -> {
            datas.add(toDTO(o));
        });
        return datas;
    }
}
