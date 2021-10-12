package com.bitozen.hms.web.assembler;

import com.bitozen.hms.pm.common.dto.query.blacklist.BlacklistDTO;
import com.bitozen.hms.projection.blacklist.BlacklistEntryProjection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class BlacklistDTOAssembler {

    public BlacklistDTO toDTO(BlacklistEntryProjection domain) {
        return new BlacklistDTO(
                domain.getBlacklistID(),
                domain.getBlacklistSPKNumber(),
                domain.getBlacklistStartDate(),
                domain.getBlacklistEndDate(),
                domain.getBlacklistNotes(),
                domain.getBlacklistType(),
                domain.getIsPermanent(),
                domain.getEmployee(),
                domain.getRequestor(),
                domain.getBlacklistDocURL(),
                domain.getIsFinalApprove(),
                domain.getBlacklistStatus(),
                domain.getBlacklistState(),
                domain.getCompRegulationChapter(),
                domain.getCompRegulationChapterDesc(),
                domain.getMetadata(),
                domain.getToken(),
                domain.getCreational().getCreatedBy(),
                domain.getCreational().getCreatedDate(),
                domain.getCreational().getModifiedBy(),
                domain.getCreational().getModifiedDate(),
                domain.getRecordID()
        );
    }

    public List<BlacklistDTO> toDTOs(List<BlacklistEntryProjection> list) {
        List<BlacklistDTO> datas = new ArrayList<>();
        list.stream().forEach((o) -> {
            datas.add(toDTO(o));
        });
        return datas;
    }
}
