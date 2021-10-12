package com.bitozen.hms.pm.event.handler.termination;

import com.bitozen.hms.common.dto.CreationalSpecificationDTO;
import com.bitozen.hms.common.dto.GenericAccessTokenDTO;
import com.bitozen.hms.common.dto.MetadataDTO;
import com.bitozen.hms.common.dto.share.BizparOptimizeDTO;
import com.bitozen.hms.common.dto.share.EmployeeOptimizeDTO;
import com.bitozen.hms.pm.common.ProlongedIllnessStatus;
import com.bitozen.hms.pm.event.termination.ProlongedIllnessRegistryChangeEvent;
import com.bitozen.hms.pm.event.termination.ProlongedIllnessRegistryCreateEvent;
import com.bitozen.hms.pm.event.termination.ProlongedIllnessRegistryDeleteEvent;
import com.bitozen.hms.projection.termination.ProlongedIllnessRegistryEntryProjection;
import com.bitozen.hms.pm.repository.termination.ProlongedIllnessRegistryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Dumayangsari
 */
@Component
@Slf4j
public class ProlongedIllnessRegistryEventListener {
    
    @Autowired
    private ObjectMapper mapper;
    
    @Autowired
    private ProlongedIllnessRegistryRepository repository;
    
    @SneakyThrows
    @EventHandler
    public void on(ProlongedIllnessRegistryCreateEvent event){
        repository.save(new ProlongedIllnessRegistryEntryProjection(
                repository.count() == 0 ? 1 : repository.findFirstByOrderByIdDesc().get().getId() + 1,
                event.getPiID(),
                event.getStartDate(),
                event.getEndDate(),
                event.getDocURL(),
                mapper.readValue(event.getEmployee(), EmployeeOptimizeDTO.class),
                mapper.readValue(event.getRequestor(), EmployeeOptimizeDTO.class),
                mapper.readValue(event.getIlnessType(), BizparOptimizeDTO.class),
                event.getPiStatus(),
                event.getReason(),
                mapper.readValue(event.getMetadata(), MetadataDTO.class),
                mapper.readValue(event.getToken(), GenericAccessTokenDTO.class),
                event.getRecordID(),
                new CreationalSpecificationDTO(event.getCreatedBy(),
                event.getCreatedDate(),
                null,
                null)
        ));
    }
    
    @SneakyThrows
    @EventHandler
    public void on (ProlongedIllnessRegistryChangeEvent event){
        Optional<ProlongedIllnessRegistryEntryProjection> data = repository.findOneByPiIDAndPiStatus(event.getPiID(), ProlongedIllnessStatus.ACTIVE);
        data.get().setPiID(event.getPiID());
        data.get().setStartDate(event.getStartDate());
        data.get().setEndDate(event.getEndDate());
        data.get().setDocURL(event.getDocURL());
        data.get().setEmployee(mapper.readValue(event.getEmployee(), EmployeeOptimizeDTO.class));
        data.get().setRequestor(mapper.readValue(event.getRequestor(), EmployeeOptimizeDTO.class));
        data.get().setIlnessType(mapper.readValue(event.getIlnessType(), BizparOptimizeDTO.class));
        data.get().setPiStatus(event.getPiStatus());
        data.get().setReason(event.getReason());
        data.get().setMetadata(mapper.readValue(event.getMetadata(), MetadataDTO.class));
        data.get().setToken(mapper.readValue(event.getToken(), GenericAccessTokenDTO.class));
        data.get().getCreational().setModifiedBy(event.getUpdatedBy());
        data.get().getCreational().setModifiedDate(event.getUpdatedDate());
        repository.save(data.get());
    }
    
    @SneakyThrows
    @EventHandler
    public void on(ProlongedIllnessRegistryDeleteEvent event){
        Optional<ProlongedIllnessRegistryEntryProjection> data = repository.findOneByPiIDAndPiStatus(event.getPiID(), ProlongedIllnessStatus.ACTIVE);
        data.get().setPiStatus(ProlongedIllnessStatus.INACTIVE);
        data.get().getCreational().setModifiedBy(event.getUpdatedBy());
        repository.save(data.get());
    }
}
