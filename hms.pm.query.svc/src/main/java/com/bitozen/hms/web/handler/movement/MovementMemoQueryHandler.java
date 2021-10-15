package com.bitozen.hms.web.handler.movement;

import com.bitozen.hms.pm.common.MVMemoStatus;
import com.bitozen.hms.pm.common.MVStatus;
import com.bitozen.hms.pm.common.dto.query.movement.MovementMemoDTOProjection;
import com.bitozen.hms.pm.common.dto.query.movement.MovementSKDTOProjection;
import com.bitozen.hms.pm.common.util.Count;
import com.bitozen.hms.pm.repository.movement.MovementRepository;
import com.bitozen.hms.web.assembler.MovementDTOAssembler;
import com.bitozen.hms.web.handler.movement.query.*;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MovementMemoQueryHandler {

    @Autowired
    private MovementRepository repository;

    @QueryHandler
    public List<MovementMemoDTOProjection> getMovementMemoByID(GetMovementMemoByIDQuery query){
        List<MovementMemoDTOProjection> result = repository.findMemoByMemoID(query.getMemoID(), MVMemoStatus.INACTIVE.toString());
        if(!result.isEmpty()) {
            return result;
        }
        return new ArrayList<>();
    }

    @QueryHandler
    public List<MovementMemoDTOProjection> getAllMovementMemoForWeb(GetAllMovementMemoForWebQuery query){
        Pageable pageable = PageRequest.of(query.getRequest().getOffset(), query.getRequest().getLimit(), Sort.by("creational.createdDate").descending());
        String param = String.valueOf(query.getRequest().getParams().get("param"));
        List<MovementMemoDTOProjection> result = repository.findMemoForWeb(param, MVMemoStatus.INACTIVE.toString(), pageable);
        if(!result.isEmpty()) {
            return result;
        }
        return new ArrayList<>();
    }

    @QueryHandler
    public Long countMovementMemoForWeb(CountAllMovementMemoForWebQuery query) {
        try{
            String param = String.valueOf(query.getRequest().getParams().get("param"));
            Count total = repository.countMemoForWeb(param, MVMemoStatus.INACTIVE.toString());
            if(total != null){
                return total.getTotalCount();
            } else {
                return 0l;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0l;
    }
}
