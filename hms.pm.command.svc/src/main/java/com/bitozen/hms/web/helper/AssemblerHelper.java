package com.bitozen.hms.web.helper;

import com.bitozen.hms.common.dto.MetadataCreateDTO;
import com.bitozen.hms.common.dto.MetadataDTO;
import com.bitozen.hms.common.dto.share.DocumentCreateDTO;
import com.bitozen.hms.common.dto.share.DocumentDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class AssemblerHelper {

    @Autowired
    private ESHelper esHelper;

    @Autowired
    private BizparHelper bizHelper;

    public MetadataDTO toMetadata(MetadataCreateDTO dto) {
        MetadataDTO meta = new MetadataDTO();
        meta.setEs(esHelper.findEsByID(dto.getEs()));
        meta.setId(dto.getId());
        meta.setTag(dto.getTag());
        meta.setType(bizHelper.convertBizpar(dto.getType()));
        meta.setVersion(dto.getVersion());
        return meta;
    }

    public DocumentDTO convertDocument(DocumentCreateDTO dto){
        DocumentDTO data = new DocumentDTO();
        data.setDocID(dto.getDocID());
        data.setDocName(dto.getDocName());
        data.setDocType(bizHelper.convertBizpar(dto.getDocType()));
        data.setDocURL(dto.getDocURL());
        return data;
    }

    public List<DocumentDTO> convertDocuments(List<DocumentCreateDTO> dtos) {
        List<DocumentDTO> datas = new ArrayList<>();
        dtos.stream().forEach((o) -> {
            datas.add(convertDocument(o));
        });
        return datas;
    }
}
