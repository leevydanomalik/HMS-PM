package com.bitozen.hms.pm.event.handler.blacklist;

import com.bitozen.hms.common.dto.CreationalSpecificationDTO;
import com.bitozen.hms.common.dto.GenericAccessTokenDTO;
import com.bitozen.hms.common.dto.MetadataDTO;
import com.bitozen.hms.common.dto.share.BizparOptimizeDTO;
import com.bitozen.hms.common.dto.share.EmployeeOptimizeDTO;
import com.bitozen.hms.pm.common.BlacklistStatus;
import com.bitozen.hms.pm.event.blacklist.BlacklistChangeEvent;
import com.bitozen.hms.pm.event.blacklist.BlacklistCreateEvent;
import com.bitozen.hms.pm.event.blacklist.BlacklistDeleteEvent;
import com.bitozen.hms.pm.repository.blacklist.BlacklistRepository;
import com.bitozen.hms.projection.blacklist.BlacklistEntryProjection;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class BlacklistEventListener {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private BlacklistRepository repository;

    @SneakyThrows
    @EventHandler
    public void on(BlacklistCreateEvent event) {
        repository.save(new BlacklistEntryProjection(
                repository.count() == 0 ? 1 : repository.findFirstByOrderByIdDesc().get().getId() + 1,
                event.getBlacklistID(),
                event.getBlacklistSPKNumber(),
                event.getBlacklistStartDate(),
                event.getBlacklistEndDate(),
                event.getBlacklistNotes(),
                mapper.readValue(event.getBlacklistType(), BizparOptimizeDTO.class),
                event.getIsPermanent(),
                mapper.readValue(event.getEmployee(), EmployeeOptimizeDTO.class),
                mapper.readValue(event.getRequestor(), EmployeeOptimizeDTO.class),
                event.getBlacklistDocURL(),
                event.getIsFinalApprove(),
                event.getBlacklistStatus(),
                event.getBlacklistState(),
                event.getCompRegulationChapter(),
                event.getCompRegulationChapterDesc(),
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
    public void on(BlacklistChangeEvent event) {
        Optional<BlacklistEntryProjection> data = repository.findOneByBlacklistIDAndBlacklistStatusNot(event.getBlacklistID(), BlacklistStatus.INACTIVE);
        data.get().setBlacklistSPKNumber(event.getBlacklistSPKNumber());
        data.get().setBlacklistStartDate(event.getBlacklistStartDate());
        data.get().setBlacklistEndDate(event.getBlacklistEndDate());
        data.get().setBlacklistNotes(event.getBlacklistNotes());
        data.get().setBlacklistType(mapper.readValue(event.getBlacklistType(), BizparOptimizeDTO.class));
        data.get().setIsPermanent(event.getIsPermanent());
        data.get().setEmployee(mapper.readValue(event.getEmployee(), EmployeeOptimizeDTO.class));
        data.get().setRequestor(mapper.readValue(event.getRequestor(), EmployeeOptimizeDTO.class));
        data.get().setBlacklistDocURL(event.getBlacklistDocURL());
        data.get().setIsFinalApprove(event.getIsFinalApprove());
        data.get().setBlacklistStatus(event.getBlacklistStatus());
        data.get().setBlacklistState(event.getBlacklistState());
        data.get().setCompRegulationChapter(event.getCompRegulationChapter());
        data.get().setCompRegulationChapterDesc(event.getCompRegulationChapterDesc());
        data.get().setMetadata(mapper.readValue(event.getMetadata(), MetadataDTO.class));
        data.get().setToken(mapper.readValue(event.getToken(), GenericAccessTokenDTO.class));
        data.get().getCreational().setModifiedBy(event.getUpdatedBy());
        data.get().getCreational().setModifiedDate(event.getUpdatedDate());
        repository.save(data.get());
    }

    @SneakyThrows
    @EventHandler
    public void on(BlacklistDeleteEvent event) {
        Optional<BlacklistEntryProjection> data = repository.findOneByBlacklistIDAndBlacklistStatusNot(event.getBlacklistID(), BlacklistStatus.INACTIVE);
        data.get().setBlacklistStatus(BlacklistStatus.INACTIVE);
        data.get().getCreational().setModifiedBy(event.getUpdatedBy());
        repository.save(data.get());
    }
}
