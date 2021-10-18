package com.bitozen.hms.pm.event.handler.movement;

import com.bitozen.hms.common.dto.CreationalSpecificationDTO;
import com.bitozen.hms.common.dto.GenericAccessTokenDTO;
import com.bitozen.hms.common.dto.MetadataDTO;
import com.bitozen.hms.common.dto.share.BizparOptimizeDTO;
import com.bitozen.hms.common.dto.share.DocumentDTO;
import com.bitozen.hms.common.dto.share.EmployeeOptimizeDTO;
import com.bitozen.hms.pm.common.MVStatus;
import com.bitozen.hms.pm.common.dto.query.movement.*;
import com.bitozen.hms.pm.event.movement.MovementChangeEvent;
import com.bitozen.hms.pm.event.movement.MovementCreateEvent;
import com.bitozen.hms.pm.event.movement.MovementDeleteEvent;
import com.bitozen.hms.pm.repository.movement.MovementRepository;
import com.bitozen.hms.projection.movement.MovementEntryProjection;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class MovementEventListener {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MovementRepository repository;

    @SneakyThrows
    @EventHandler
    public void on(MovementCreateEvent event) {
        repository.save(new MovementEntryProjection(
                repository.count() == 0 ? 1 : repository.findFirstByOrderByIdDesc().get().getId() + 1,
                event.getMvID(),
                event.getMvNotes(),
                event.getMvSPKDocNumber(),
                event.getMvEffectiveDate(),
                event.getIsFinalApprove(),
                event.getMvStatus(),
                event.getMvState(),
                mapper.readValue(event.getMvCase(), BizparOptimizeDTO.class),
                mapper.readValue(event.getMvType(), BizparOptimizeDTO.class),
                mapper.readValue(event.getRequestor(), EmployeeOptimizeDTO.class),
                mapper.readValue(event.getAssignment(), AssignmentDTO.class),
                mapper.readValue(event.getEmployees(), new TypeReference<List<MVEmployeeDTO>>() {}),
                mapper.readValue(event.getMvDocs(), new TypeReference<List<DocumentDTO>>() {}),
                mapper.readValue(event.getMvBenefitBefore(), MVBenefitDTO.class),
                mapper.readValue(event.getMvBenefitAfter(), MVBenefitDTO.class),
                mapper.readValue(event.getMvFacilityBefore(), MVFacilityDTO.class),
                mapper.readValue(event.getMvFacilityAfter(), MVFacilityDTO.class),
                mapper.readValue(event.getMvPayroll(), MVPayrollDTO.class),
                mapper.readValue(event.getMvPosition(), MVPositionDTO.class),
                mapper.readValue(event.getRefRecRequest(), RecRequestRefDTO.class),
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
    public void on(MovementChangeEvent event) {
        Optional<MovementEntryProjection> data = repository.findOneByMvIDAndMvStatusNot(event.getMvID(), MVStatus.INACTIVE);
        data.get().setMvNotes(event.getMvNotes());
        data.get().setMvSPKDocNumber(event.getMvSPKDocNumber());
        data.get().setMvEffectiveDate(event.getMvEffectiveDate());
        data.get().setIsFinalApprove(event.getIsFinalApprove());
        data.get().setMvStatus(event.getMvStatus());
        data.get().setMvState(event.getMvState());
        data.get().setMvCase(mapper.readValue(event.getMvCase(), BizparOptimizeDTO.class));
        data.get().setMvType(mapper.readValue(event.getMvType(), BizparOptimizeDTO.class));
        data.get().setRequestor(mapper.readValue(event.getRequestor(), EmployeeOptimizeDTO.class));
        data.get().setAssignment(mapper.readValue(event.getAssignment(), AssignmentDTO.class));
        data.get().setEmployees(mapper.readValue(event.getEmployees(), new TypeReference<List<MVEmployeeDTO>>() {}));
        data.get().setMvDocs(mapper.readValue(event.getMvDocs(), new TypeReference<List<DocumentDTO>>() {}));
        data.get().setMvBenefitBefore(mapper.readValue(event.getMvBenefitBefore(), MVBenefitDTO.class));
        data.get().setMvBenefitAfter(mapper.readValue(event.getMvBenefitAfter(), MVBenefitDTO.class));
        data.get().setMvFacilityBefore(mapper.readValue(event.getMvFacilityBefore(), MVFacilityDTO.class));
        data.get().setMvFacilityAfter(mapper.readValue(event.getMvFacilityAfter(), MVFacilityDTO.class));
        data.get().setMvPayroll(mapper.readValue(event.getMvPayroll(), MVPayrollDTO.class));
        data.get().setMvPosition(mapper.readValue(event.getMvPosition(), MVPositionDTO.class));
        data.get().setRefRecRequest(mapper.readValue(event.getRefRecRequest(), RecRequestRefDTO.class));
        data.get().setMetadata(mapper.readValue(event.getMetadata(), MetadataDTO.class));
        data.get().setToken(mapper.readValue(event.getToken(), GenericAccessTokenDTO.class));
        data.get().getCreational().setModifiedBy(event.getUpdatedBy());
        data.get().getCreational().setModifiedDate(event.getUpdatedDate());
        repository.save(data.get());
    }

    @SneakyThrows
    @EventHandler
    public void on(MovementDeleteEvent event) {
        Optional<MovementEntryProjection> data = repository.findOneByMvIDAndMvStatusNot(event.getMvID(), MVStatus.INACTIVE);
        data.get().setMvStatus(MVStatus.INACTIVE);
        data.get().getCreational().setModifiedBy(event.getUpdatedBy());
        repository.save(data.get());
    }
}
