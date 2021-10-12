package com.bitozen.hms.web.assembler;

import com.bitozen.hms.pm.common.dto.query.termination.TerminationDTO;
import com.bitozen.hms.projection.termination.TerminationEntryProjection;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TerminationDTOAssembler {

    public TerminationDTO toDTO(TerminationEntryProjection domain) {
        return new TerminationDTO(
                domain.getTmnID(),
                domain.getTmnNotes(),
                domain.getBpjsHTDocNumber(),
                domain.getBpjsPensionDocNumber(),
                domain.getDocCopies(),
                domain.getIsCancelFinalApprove(),
                domain.getIsExecuted(),
                domain.getIsFinalApprove(),
                domain.getMemoDocNumber(),
                domain.getEmployee(),
                domain.getRequestor(),
                domain.getSkDocNumber(),
                domain.getSkdtDocNumber(),
                domain.getTmnDocs(),
                domain.getTmnEffectiveDate(),
                domain.getTmnReqDate(),
                domain.getTmnPphEndDate(),
                domain.getTmnReason(),
                domain.getTmnState(),
                domain.getTmnStatus(),
                domain.getTmnPension(),
                domain.getMetadata(),
                domain.getToken(),
                domain.getCreational().getCreatedBy(),
                domain.getCreational().getCreatedDate(),
                domain.getCreational().getModifiedBy(),
                domain.getCreational().getModifiedDate(),
                domain.getRecordID()
        );

    }

    public List<TerminationDTO> toDTOs(List<TerminationEntryProjection> list) {
        List<TerminationDTO> datas = new ArrayList<>();
        list.stream().forEach((o) -> {
            datas.add(toDTO(o));
        });
        return datas;
    }

}
