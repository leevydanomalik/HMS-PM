package com.bitozen.hms.web.assembler;

import com.bitozen.hms.pm.common.dto.query.termination.ProlongedIllnessRegistryDTO;
import com.bitozen.hms.projection.termination.ProlongedIllnessRegistryEntryProjection;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 *
 * @author Dumayangsari
 */
@Component
@Slf4j
public class ProlongedIllnessRegistryDTOAssembler {
    
    public ProlongedIllnessRegistryDTO toDTO(ProlongedIllnessRegistryEntryProjection domain){
        return new ProlongedIllnessRegistryDTO(
                domain.getPiID(),
                domain.getStartDate(),
                domain.getEndDate(),
                domain.getDocURL(),
                domain.getEmployee(),
                domain.getRequestor(),
                domain.getIlnessType(),
                domain.getPiStatus(),
                domain.getReason(),
                domain.getMetadata(),
                domain.getToken(),
                domain.getRecordID(),
                domain.getCreational().getCreatedBy(),
                domain.getCreational().getCreatedDate(),
                domain.getCreational().getModifiedBy(),
                domain.getCreational().getModifiedDate()
                
        );
    }
    
    public List<ProlongedIllnessRegistryDTO> toDTOs(List<ProlongedIllnessRegistryEntryProjection> list){
        List<ProlongedIllnessRegistryDTO> datas = new ArrayList<>();
        list.stream().forEach((o)->{
            datas.add(toDTO(o));
        });
        return datas;
    }
}
