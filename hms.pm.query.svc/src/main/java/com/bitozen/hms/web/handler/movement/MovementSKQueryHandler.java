package com.bitozen.hms.web.handler.movement;

import com.bitozen.hms.pm.common.MVSKStatus;
import com.bitozen.hms.pm.common.MVStatus;
import com.bitozen.hms.pm.common.dto.query.movement.MVSKDTO;
import com.bitozen.hms.pm.common.dto.query.movement.MovementSKDTOProjection;
import com.bitozen.hms.pm.common.util.Count;
import com.bitozen.hms.pm.repository.movement.MovementRepository;
import com.bitozen.hms.projection.movement.MovementEntryProjection;
import com.bitozen.hms.web.assembler.MovementDTOAssembler;
import com.bitozen.hms.web.handler.movement.query.CountAllMovementSKForWebQuery;
import com.bitozen.hms.web.handler.movement.query.GetAllMovementSKForWebQuery;
import com.bitozen.hms.web.handler.movement.query.GetMovementSKByIDQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class MovementSKQueryHandler {

    @Autowired
    private MovementRepository repository;

    @QueryHandler
    public List<MovementSKDTOProjection> getMovementSKByID(GetMovementSKByIDQuery query){
        List<MovementSKDTOProjection> result = repository.findSKBySkID(query.getSkID(), MVStatus.INACTIVE.toString());
        if(!result.isEmpty()) {
            return result;
        }
        return new ArrayList<>();
    }

    @QueryHandler
    public List<MovementSKDTOProjection> getAllMovementSKForWeb(GetAllMovementSKForWebQuery query){
        Pageable pageable = PageRequest.of(query.getRequest().getOffset(), query.getRequest().getLimit(), Sort.by("creational.createdDate").descending());
        String param = String.valueOf(query.getRequest().getParams().get("param"));
        List<MovementSKDTOProjection> result = repository.findSKForWeb(param, MVSKStatus.INACTIVE.toString(), pageable);
        if(!result.isEmpty()) {
            return result;
        }
        return new ArrayList<>();
    }

    @QueryHandler
    public Long countMovementSKForWeb(CountAllMovementSKForWebQuery query) {
        try{
            String param = String.valueOf(query.getRequest().getParams().get("param"));
            Count total = repository.countSKForWeb(param, MVSKStatus.INACTIVE.toString());
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
