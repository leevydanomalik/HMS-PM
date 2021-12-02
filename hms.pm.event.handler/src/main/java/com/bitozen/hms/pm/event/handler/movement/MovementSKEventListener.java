package com.bitozen.hms.pm.event.handler.movement;

import com.bitozen.hms.pm.common.MVSKStatus;
import com.bitozen.hms.pm.common.MVStatus;
import com.bitozen.hms.pm.common.dto.query.movement.*;
import com.bitozen.hms.pm.event.movement.*;
import com.bitozen.hms.pm.repository.movement.MovementRepository;
import com.bitozen.hms.projection.movement.MovementEntryProjection;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Component
@Slf4j
public class MovementSKEventListener {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MovementRepository repository;

    @SneakyThrows
    @EventHandler
    public void on(MovementSKCreateEvent event) {
        Optional<MovementEntryProjection> data = repository.findOneByMvIDAndMvStatusNot(event.getMvID(), MVStatus.INACTIVE);
        MVEmployeeDTO employee = mapper.readValue(event.getEmployees(),MVEmployeeDTO.class);
        if(data.isPresent()) {
            MovementEntryProjection movement = data.get();
            List<MVEmployeeDTO> employees = data.get().getEmployees();
            employees.stream().forEach(detail -> {
                if(detail.getMvDetailID().equalsIgnoreCase(employee.getMvDetailID())) {
                    detail.getSks().addAll(employee.getSks());
                }
            });
            movement.setEmployees(employees);
            repository.save(movement);
        }
    }

    @SneakyThrows
    @EventHandler
    public void on(MovementSKChangeEvent event) {
        Optional<MovementEntryProjection> data = repository.findOneByMvIDAndMvStatusNot(event.getMvID(), MVStatus.INACTIVE);
        MVEmployeeDTO employee = mapper.readValue(event.getEmployees(),MVEmployeeDTO.class);
        MVSKDTO skdata = employee.getSks().get(0);
        if(data.isPresent()) {
            MovementEntryProjection movement = data.get();
            List<MVEmployeeDTO> employees = data.get().getEmployees();
            employees.stream().forEach(detail -> {
                if(detail.getMvDetailID().equalsIgnoreCase(employee.getMvDetailID())) {
                    List<MVSKDTO> sks = detail.getSks();
                    sks.stream().forEach(sk -> {
                        if(sk.getSkID().equalsIgnoreCase(skdata.getSkID())) {
                            sk.setSkRefID(skdata.getSkRefID());
                            sk.setSkDocNumber(skdata.getSkDocNumber());
                            sk.setSkStatus(skdata.getSkStatus());
                            sk.setSkState(skdata.getSkState());
                            sk.setSkType(skdata.getSkType());
                            sk.setRequestor(skdata.getRequestor());
                            sk.setIsRevoke(skdata.getIsRevoke());
                            sk.setIsFinalApprove(skdata.getIsFinalApprove());
                            sk.setSkCopies(skdata.getSkCopies());
                            sk.setSkConsiderDesc(skdata.getSkConsiderDesc());
                            sk.setRequestDate(skdata.getRequestDate());
                        }
                    });
                    detail.setSks(sks);
                }
            });
            movement.setEmployees(employees);
            repository.save(movement);
        }
    }
    
    @SneakyThrows
    @EventHandler
    public void on(MovementSKChangeStateAndStatusEvent event) {
        Optional<MovementEntryProjection> data = repository.findOneByMvIDAndMvStatusNot(event.getMvID(), MVStatus.INACTIVE);
        MVEmployeeDTO employee = mapper.readValue(event.getEmployees(),MVEmployeeDTO.class);
        MVSKDTO skdata = employee.getSks().get(0);
        if(data.isPresent()) {
            MovementEntryProjection movement = data.get();
            List<MVEmployeeDTO> employees = data.get().getEmployees();
            employees.stream().forEach(detail -> {
                if(detail.getMvDetailID().equalsIgnoreCase(employee.getMvDetailID())) {
                    List<MVSKDTO> sks = detail.getSks();
                    sks.stream().forEach(sk -> {
                        if(sk.getSkID().equalsIgnoreCase(skdata.getSkID())) {
                            sk.setSkStatus(skdata.getSkStatus());
                            sk.setSkState(skdata.getSkState());
                            sk.setIsRevoke(skdata.getIsRevoke());
                            sk.setIsFinalApprove(skdata.getIsFinalApprove());
                            sk.setRequestDate(skdata.getRequestDate());
                        }
                    });
                    detail.setSks(sks);
                }
            });
            movement.setEmployees(employees);
            repository.save(movement);
        }
    }

    @SneakyThrows
    @EventHandler
    public void on(MovementSKDeleteEvent event) {
        Optional<MovementEntryProjection> data = repository.findOneByMvIDAndMvStatusNot(event.getMvID(), MVStatus.INACTIVE);
        MVEmployeeDTO employee = mapper.readValue(event.getEmployees(),MVEmployeeDTO.class);
        MVSKDTO skdata = employee.getSks().get(0);
        Predicate<MVSKDTO> target = item -> item.getSkID().equalsIgnoreCase(skdata.getSkID());
        if(data.isPresent()) {
            MovementEntryProjection movement = data.get();
            List<MVEmployeeDTO> employees = data.get().getEmployees();
            employees.stream().forEach(detail -> {
                List<MVSKDTO> sks = detail.getSks();
                sks.stream().filter(target).forEach(sk -> sk.getSkID());
                sks.removeIf(target);
                detail.setSks(sks);
            });
            movement.setEmployees(employees);
            repository.save(movement);
        }
    }
}
