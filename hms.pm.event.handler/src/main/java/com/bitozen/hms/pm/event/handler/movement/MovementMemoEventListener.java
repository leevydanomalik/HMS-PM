package com.bitozen.hms.pm.event.handler.movement;

import com.bitozen.hms.pm.common.MVMemoStatus;
import com.bitozen.hms.pm.common.MVSKStatus;
import com.bitozen.hms.pm.common.MVStatus;
import com.bitozen.hms.pm.common.dto.query.movement.MVEmployeeDTO;
import com.bitozen.hms.pm.common.dto.query.movement.MVMemoDTO;
import com.bitozen.hms.pm.common.dto.query.movement.MVSKDTO;
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
public class MovementMemoEventListener {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MovementRepository repository;

    @SneakyThrows
    @EventHandler
    public void on(MovementMemoCreateEvent event) {
        Optional<MovementEntryProjection> data = repository.findOneByMvIDAndMvStatusNot(event.getMvID(), MVStatus.INACTIVE);
        MVEmployeeDTO employee = mapper.readValue(event.getEmployees(),MVEmployeeDTO.class);
        if(data.isPresent()) {
            MovementEntryProjection movement = data.get();
            List<MVEmployeeDTO> employees = data.get().getEmployees();
            employees.stream().forEach(detail -> {
                if(detail.getMvDetailID().equalsIgnoreCase(employee.getMvDetailID())) {
                    detail.getMemos().addAll(employee.getMemos());
                }
            });
            movement.setEmployees(employees);
            repository.save(movement);
        }
    }

    @SneakyThrows
    @EventHandler
    public void on(MovementMemoChangeEvent event) {
        Optional<MovementEntryProjection> data = repository.findOneByMvIDAndMvStatusNot(event.getMvID(), MVStatus.INACTIVE);
        MVEmployeeDTO employee = mapper.readValue(event.getEmployees(),MVEmployeeDTO.class);
        MVMemoDTO memoData = employee.getMemos().get(0);
        if(data.isPresent()) {
            MovementEntryProjection movement = data.get();
            List<MVEmployeeDTO> employees = data.get().getEmployees();
            employees.stream().forEach(detail -> {
                if(detail.getMvDetailID().equalsIgnoreCase(employee.getMvDetailID())) {
                    List<MVMemoDTO> memos = detail.getMemos();
                    memos.stream().forEach(memo -> {
                        if(memo.getMemoID().equalsIgnoreCase(memoData.getMemoID())) {
                            memo.setMemoRefID(memoData.getMemoRefID());
                            memo.setMemoDocNumber(memoData.getMemoDocNumber());
                            memo.setIsRevoke(memoData.getIsRevoke());
                            memo.setIsFinalApprove(memoData.getIsFinalApprove());
                            memo.setMemoStatus(memoData.getMemoStatus());
                            memo.setMemoState(memoData.getMemoState());
                            memo.setMemoType(memoData.getMemoType());
                            memo.setRequestor(memoData.getRequestor());
                            memo.setRequestDate(memoData.getRequestDate());
                        }
                    });
                    detail.setMemos(memos);
                }
            });
            movement.setEmployees(employees);
            repository.save(movement);
        }
    }

    @SneakyThrows
    @EventHandler
    public void on(MovementMemoDeleteEvent event) {
        Optional<MovementEntryProjection> data = repository.findOneByMvIDAndMvStatusNot(event.getMvID(), MVStatus.INACTIVE);
        MVEmployeeDTO employee = mapper.readValue(event.getEmployees(),MVEmployeeDTO.class);
        MVMemoDTO memoData = employee.getMemos().get(0);
        Predicate<MVMemoDTO> target = item -> item.getMemoID().equalsIgnoreCase(memoData.getMemoID());
        if(data.isPresent()) {
            MovementEntryProjection movement = data.get();
            List<MVEmployeeDTO> employees = data.get().getEmployees();
            employees.stream().forEach(detail -> {
                if(detail.getMvDetailID().equalsIgnoreCase(employee.getMvDetailID())) {
                    List<MVMemoDTO> memos = detail.getMemos();
                    memos.stream().filter(target).forEach(memo -> memo.getMemoID());
                    memos.removeIf(target);
                    detail.setMemos(memos);
                }
            });
            movement.setEmployees(employees);
            repository.save(movement);
        }
    }
}
