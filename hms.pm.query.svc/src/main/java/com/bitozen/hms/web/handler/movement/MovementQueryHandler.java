package com.bitozen.hms.web.handler.movement;

import com.bitozen.hms.pm.common.MVStatus;
import com.bitozen.hms.pm.common.dto.query.movement.MovementDTO;
import com.bitozen.hms.pm.repository.movement.MovementRepository;
import com.bitozen.hms.projection.movement.MovementEntryProjection;
import com.bitozen.hms.web.assembler.MovementDTOAssembler;
import com.bitozen.hms.web.handler.movement.query.CountAllMovementForWebQuery;
import com.bitozen.hms.web.handler.movement.query.GetAllMovementForWebQuery;
import com.bitozen.hms.web.handler.movement.query.GetMovementByIDQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class MovementQueryHandler {

    @Autowired
    private MovementRepository repository;

    @Autowired
    private MovementDTOAssembler assembler;

    @QueryHandler
    public MovementDTO getMovementByID(GetMovementByIDQuery query){
        Optional<MovementEntryProjection> result = repository.findOneByMvIDAndMvStatusNot(query.getMvID(), MVStatus.INACTIVE);
        if (result.isPresent()){
            return assembler.toDTO(result.get());
        }
        return null;
    }

    @QueryHandler
    public List<MovementDTO> getAllMovementForWeb(GetAllMovementForWebQuery query){
        Pageable pageable = PageRequest.of(query.getRequest().getOffset(), query.getRequest().getLimit(), Sort.by("creational.createdDate").descending());
        String param = String.valueOf(query.getRequest().getParams().get("param"));
        List<String> esIDs = (List<String>) query.getRequest().getParams().get("esIDs");
        Page<MovementEntryProjection> results = repository.findAllForWeb(String.valueOf(".*").concat(param).concat(".*"), esIDs, MVStatus.INACTIVE.toString(), pageable);
        if(results.hasContent()){
            return assembler.toDTOs(results.getContent());
        }
        return Collections.EMPTY_LIST;
    }

    @QueryHandler
    public Integer countMovementForWeb(CountAllMovementForWebQuery query) {
        String param = String.valueOf(query.getRequest().getParams().get("param"));
        List<String> esIDs = (List<String>) query.getRequest().getParams().get("esIDs");
        Integer count = Integer.valueOf(String.valueOf(repository.countAllForWeb(String.valueOf(".*").concat(param).concat(".*"), esIDs, MVStatus.INACTIVE.toString())));
        return count;
    }
}
