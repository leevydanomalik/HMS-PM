package com.bitozen.hms.web.helper;

import com.bitozen.hms.pm.common.dto.command.termination.TerminationDocumentCreateDTO;
import com.bitozen.hms.pm.common.dto.query.termination.TerminationDocumentDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TerminationHelper {
    
    @Autowired
    private BizparHelper bizHelper;
    
    public TerminationDocumentDTO convertDocument(TerminationDocumentCreateDTO dto){
        TerminationDocumentDTO data = new TerminationDocumentDTO();
        data.setDocID(dto.getDocID());
        data.setDocName(dto.getDocName());
        data.setDocType(bizHelper.convertBizpar(dto.getDocType()));
        data.setDocURL(dto.getDocURL());
        return data;
    
    }
    
    public List<TerminationDocumentDTO> toDocumentDTOs(List<TerminationDocumentCreateDTO> domains) {
        List<TerminationDocumentDTO> datas = new ArrayList<>();
        if (domains != null) {
            domains.stream().forEach((o) -> {
                try {

                    datas.add(convertDocument(o));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        return datas;
    }
    
}
