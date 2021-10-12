package com.bitozen.hms.web.helper;

import com.bitozen.hms.common.dto.MetadataCreateDTO;
import com.bitozen.hms.common.dto.MetadataDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
}
