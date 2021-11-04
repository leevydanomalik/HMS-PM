package com.bitozen.hms.pm.event.handler.employmentletter;

import com.bitozen.hms.common.dto.CreationalSpecificationDTO;
import com.bitozen.hms.common.dto.GenericAccessTokenDTO;
import com.bitozen.hms.common.dto.MetadataDTO;
import com.bitozen.hms.common.dto.share.BizparOptimizeDTO;
import com.bitozen.hms.common.dto.share.EmployeeOptimizeDTO;
import com.bitozen.hms.pm.common.EmploymentLetterStatus;
import com.bitozen.hms.pm.common.dto.query.employmentletter.VisaSpecificationDTO;
import com.bitozen.hms.pm.event.employmentletter.EmploymentLetterChangeEvent;
import com.bitozen.hms.pm.event.employmentletter.EmploymentLetterCreateEvent;
import com.bitozen.hms.pm.event.employmentletter.EmploymentLetterDeleteEvent;
import com.bitozen.hms.pm.event.employmentletter.EmploymentLetterStateAndEmploymentLetterStatusChangeEvent;
import com.bitozen.hms.pm.repository.employmentletter.EmploymentLetterRepository;
import com.bitozen.hms.projection.employmentletter.EmploymentLetterEntryProjection;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Jeremia
 */
@Component
@Slf4j
public class EmploymentLetterEventListener {

    @Autowired
    private ObjectMapper mapper;
    
    @Autowired
    private EmploymentLetterRepository repository;

    @SneakyThrows
    @EventHandler
    public void on(EmploymentLetterCreateEvent event) {
        repository.save(new EmploymentLetterEntryProjection(
                repository.count() == 0 ? 1 : repository.findFirstByOrderByIdDesc().get().getId() + 1,
                event.getElID(),
                event.getElDocNumber(),
                event.getElDocURL(),
                event.getElState(),
                event.getElStatus(),
                event.getIsFinalApproval(),
                event.getReason(),
                event.getReqDate(),
                mapper.readValue(event.getReqType(), BizparOptimizeDTO.class),
                mapper.readValue(event.getRequestor(), EmployeeOptimizeDTO.class),
                mapper.readValue(event.getVisaSpec(), VisaSpecificationDTO.class),
                mapper.readValue(event.getMetadata(), MetadataDTO.class),
                mapper.readValue(event.getToken(), GenericAccessTokenDTO.class),
                new CreationalSpecificationDTO(event.getCreatedBy(),
                        event.getCreatedDate(),
                        null,
                        null),
                event.getRecordID()
        ));

    }

    @SneakyThrows
    @EventHandler
    public void on(EmploymentLetterChangeEvent event) {
        Optional<EmploymentLetterEntryProjection> data = repository.findOneByElIDAndElStatusNot(event.getElID(), EmploymentLetterStatus.INACTIVE);
        data.get().setElDocNumber(event.getElDocNumber());
        data.get().setElDocURL(event.getElDocURL());
        data.get().setElState(event.getElState());
        data.get().setElStatus(event.getElStatus());
        data.get().setIsFinalApproval(event.getIsFinalApproval());
        data.get().setReason(event.getReason());
        data.get().setReqDate(event.getReqDate());
        data.get().setReqType(event.getReqType() == null ? null : mapper.readValue(event.getReqType(), BizparOptimizeDTO.class));
        data.get().setRequestor(event.getRequestor() == null ? null : mapper.readValue(event.getRequestor(), EmployeeOptimizeDTO.class));
        data.get().setVisaSpec(event.getVisaSpec() == null ? null : mapper.readValue(event.getVisaSpec(), VisaSpecificationDTO.class));
        data.get().setMetadata(event.getMetadata() == null ? null : mapper.readValue(event.getMetadata(), MetadataDTO.class));
        data.get().setToken(event.getToken() == null ? null : mapper.readValue(event.getToken(), GenericAccessTokenDTO.class));
        data.get().getCreational().setModifiedBy(event.getUpdatedBy());
        data.get().getCreational().setModifiedDate(event.getUpdatedDate());
        repository.save(data.get());
    }
    
    @SneakyThrows
    @EventHandler
    public void on(EmploymentLetterStateAndEmploymentLetterStatusChangeEvent event) {
        Optional<EmploymentLetterEntryProjection> data = repository.findOneByElIDAndElStatusNot(event.getElID(), EmploymentLetterStatus.INACTIVE);
        data.get().setElState(event.getElState());
        data.get().setElStatus(event.getElStatus());
        data.get().setIsFinalApproval(event.getIsFinalApproval());
        data.get().getCreational().setModifiedBy(event.getUpdatedBy());
        data.get().getCreational().setModifiedDate(event.getUpdatedDate());
        repository.save(data.get());
    }
    
    @SneakyThrows
    @EventHandler
    public void on(EmploymentLetterDeleteEvent event) {
        Optional<EmploymentLetterEntryProjection> data = repository.findOneByElIDAndElStatusNot(event.getElID(), EmploymentLetterStatus.INACTIVE);
        data.get().setElStatus(EmploymentLetterStatus.INACTIVE);
        data.get().getCreational().setModifiedBy(event.getUpdatedBy());
        repository.save(data.get());
    }

}
