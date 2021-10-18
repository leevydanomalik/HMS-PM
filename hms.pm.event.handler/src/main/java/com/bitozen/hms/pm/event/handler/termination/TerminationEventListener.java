package com.bitozen.hms.pm.event.handler.termination;

import com.bitozen.hms.common.dto.CreationalSpecificationDTO;
import com.bitozen.hms.common.dto.GenericAccessTokenDTO;
import com.bitozen.hms.common.dto.MetadataDTO;
import com.bitozen.hms.common.dto.share.BizparOptimizeDTO;
import com.bitozen.hms.common.dto.share.EmployeeOptimizeDTO;
import com.bitozen.hms.pm.common.TerminationStatus;
import com.bitozen.hms.pm.common.dto.query.termination.BAGPensionDTO;
import com.bitozen.hms.pm.common.dto.query.termination.BAGProlongedIllnessDTO;
import com.bitozen.hms.pm.common.dto.query.termination.TerminationDocumentDTO;
import com.bitozen.hms.pm.event.termination.TerminationChangeEvent;
import com.bitozen.hms.pm.event.termination.TerminationCreateEvent;
import com.bitozen.hms.pm.event.termination.TerminationDeleteEvent;
import com.bitozen.hms.pm.event.termination.TerminationStateAndStatusChangeEvent;
import com.bitozen.hms.pm.repository.termination.TerminationRepository;
import com.bitozen.hms.projection.termination.TerminationEntryProjection;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TerminationEventListener {
    
    @Autowired
    private ObjectMapper mapper;
    
    @Autowired
    private TerminationRepository repository;
    
    @SneakyThrows
    @EventHandler
    public void on(TerminationCreateEvent event) {
        repository.save(new TerminationEntryProjection(
                repository.count() == 0 ? 1 : repository.findFirstByOrderByIdDesc().get().getId() + 1,
                event.getTmnID(),
                event.getTmnNotes(),
                event.getBpjsHTDocNumber(),
                event.getBpjsPensionDocNumber(),
                event.getDocCopies() == null ? null : mapper.readValue(event.getDocCopies(), new TypeReference<List<String>>(){}),
                event.getIsCancelFinalApprove(),
                event.getIsExecuted(),
                event.getIsFinalApprove(),
                event.getMemoDocNumber(),
                event.getEmployee() == null ? null : mapper.readValue(event.getEmployee(), EmployeeOptimizeDTO.class),
                event.getRequestor() == null ? null : mapper.readValue(event.getRequestor(), EmployeeOptimizeDTO.class),
                event.getSkDocNumber(),
                event.getSkdtDocNumber(),
                event.getTmnDocs() == null ? null : mapper.readValue(event.getTmnDocs(), new TypeReference<List<TerminationDocumentDTO>>(){}),
                event.getTmnEffectiveDate(),
                event.getTmnReqDate(),
                event.getTmnPphEndDate(),
                event.getTmnReason() == null ? null : mapper.readValue(event.getTmnReason(), BizparOptimizeDTO.class),
                event.getTmnState(),
                event.getTmnStatus(),
                event.getMetadata() == null ? null : mapper.readValue(event.getMetadata(), MetadataDTO.class),
                event.getToken() == null ? null : mapper.readValue(event.getToken(), GenericAccessTokenDTO.class),
                new CreationalSpecificationDTO(event.getCreatedBy(),
                        event.getCreatedDate(),
                        null,
                        null),
                event.getRecordID(),
                event.getBagPensionSpec() == null ? null : mapper.readValue(event.getBagPensionSpec(), BAGPensionDTO.class),
                event.getBagProlongedIllnessSpec() == null ? null : mapper.readValue(event.getBagProlongedIllnessSpec(), BAGProlongedIllnessDTO.class)
        ));

    }
    
    @SneakyThrows
    @EventHandler
    public void on(TerminationChangeEvent event){
        Optional<TerminationEntryProjection> data = repository.findOneByTmnIDAndTmnStatusNot(event.getTmnID(), TerminationStatus.INACTIVE);
        data.get().setTmnNotes(event.getTmnNotes());
        data.get().setBpjsHTDocNumber(event.getBpjsHTDocNumber());
        data.get().setBpjsPensionDocNumber(event.getBpjsPensionDocNumber());
        data.get().setDocCopies(event.getDocCopies() == null ? null : mapper.readValue(event.getDocCopies(), new TypeReference<List<String>>(){}));
        data.get().setIsCancelFinalApprove(event.getIsCancelFinalApprove());
        data.get().setIsExecuted(event.getIsExecuted());
        data.get().setIsFinalApprove(event.getIsFinalApprove());
        data.get().setMemoDocNumber(event.getMemoDocNumber());
        data.get().setEmployee(event.getEmployee() == null ? null : mapper.readValue(event.getEmployee(), EmployeeOptimizeDTO.class));
        data.get().setRequestor(event.getRequestor() == null ? null : mapper.readValue(event.getRequestor(), EmployeeOptimizeDTO.class));
        data.get().setSkDocNumber(event.getSkDocNumber());
        data.get().setSkdtDocNumber(event.getSkdtDocNumber());
        data.get().setTmnDocs(event.getTmnDocs() == null ? null : mapper.readValue(event.getTmnDocs(), new TypeReference<List<TerminationDocumentDTO>>(){}));
        data.get().setTmnEffectiveDate(event.getTmnEffectiveDate());
        data.get().setTmnReqDate(event.getTmnReqDate());
        data.get().setTmnPphEndDate(event.getTmnPphEndDate());
        data.get().setTmnReason(event.getTmnReason() == null ? null : mapper.readValue(event.getTmnReason(), BizparOptimizeDTO.class));
        data.get().setTmnState(event.getTmnState());
        data.get().setTmnStatus(event.getTmnStatus());
        data.get().setMetadata(event.getMetadata() == null ? null : mapper.readValue(event.getMetadata(), MetadataDTO.class));
        data.get().setToken(event.getToken() == null ? null : mapper.readValue(event.getToken(), GenericAccessTokenDTO.class));
        data.get().getCreational().setModifiedBy(event.getUpdatedBy());
        data.get().getCreational().setModifiedDate(event.getUpdatedDate());
        data.get().setBagPensionSpec(event.getBagPensionSpec() == null ? null : mapper.readValue(event.getBagPensionSpec(), BAGPensionDTO.class));
        data.get().setBagProlongedIllnessSpec(event.getBagProlongedIllnessSpec() == null ? null : mapper.readValue(event.getBagProlongedIllnessSpec(), BAGProlongedIllnessDTO.class));
        repository.save(data.get());
    }
    
    @SneakyThrows
    @EventHandler
    public void on(TerminationDeleteEvent event){
        Optional<TerminationEntryProjection> data = repository.findOneByTmnIDAndTmnStatusNot(event.getTmnID(), TerminationStatus.INACTIVE);
        data.get().setTmnStatus(TerminationStatus.INACTIVE);
        data.get().getCreational().setModifiedBy(event.getUpdatedBy());
        repository.save(data.get());
    }
    
    @SneakyThrows
    @EventHandler
    public void on(TerminationStateAndStatusChangeEvent event) {
        try {
            Optional<TerminationEntryProjection> data = repository.findOneByTmnIDAndTmnStatusNot(event.getTmnID(), TerminationStatus.INACTIVE);
            data.get().setTmnState(event.getTmnState());
            data.get().setTmnStatus(event.getTmnStatus());
            data.get().getCreational().setModifiedBy(event.getUpdatedBy());
            data.get().getCreational().setModifiedDate(event.getUpdatedDate());
            repository.save(data.get());
        } catch (Exception e) {
            log.info(e.getMessage());    
        }
    }
}
