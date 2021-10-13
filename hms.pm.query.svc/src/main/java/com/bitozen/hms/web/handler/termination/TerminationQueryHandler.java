package com.bitozen.hms.web.handler.termination;

import com.bitozen.hms.pm.common.TerminationStatus;
import com.bitozen.hms.pm.common.dto.query.termination.TerminationDTO;
import com.bitozen.hms.pm.repository.termination.TerminationRepository;
import com.bitozen.hms.projection.termination.TerminationEntryProjection;
import com.bitozen.hms.web.assembler.TerminationDTOAssembler;
import com.bitozen.hms.web.handler.termination.query.CountAllTerminationForWebQuery;
import com.bitozen.hms.web.handler.termination.query.GetAllTerminationForWebQuery;
import com.bitozen.hms.web.handler.termination.query.GetTerminationByIDQuery;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class TerminationQueryHandler {
    
    @Autowired
    private TerminationRepository repository;
    
    @Autowired
    private TerminationDTOAssembler assembler;
    
    @QueryHandler
    public TerminationDTO getTerminationByID(GetTerminationByIDQuery query){
        Optional<TerminationEntryProjection> result = repository.findOneByTmnIDAndTmnStatusNot(query.getTmnID(), TerminationStatus.INACTIVE);
        if (result.isPresent()){
            return assembler.toDTO(result.get());
        }
        return null;
    }
    
    @QueryHandler
    public List<TerminationDTO> getAllTerminationForWeb(GetAllTerminationForWebQuery query){
        Pageable pageable = PageRequest.of(query.getRequest().getOffset(), query.getRequest().getLimit(), Sort.by("creational.createdDate").descending());
        String param = String.valueOf(query.getRequest().getParams().get("param"));
        List<String> esIDs = (List<String>) query.getRequest().getParams().get("esIDs");
        Page<TerminationEntryProjection> results = repository.findAllForWeb(String.valueOf(".*").concat(param).concat(".*"),esIDs, pageable);
        if(results.hasContent()){
            return assembler.toDTOs(results.getContent());
        }
        return Collections.EMPTY_LIST;
    }
    
    @QueryHandler
    public Integer countTerminationForWeb(CountAllTerminationForWebQuery query) {
        String param = String.valueOf(query.getRequest().getParams().get("param"));
        List<String> esIDs = (List<String>) query.getRequest().getParams().get("esIDs");
        Integer count = Integer.valueOf(String.valueOf(repository.countAllForWeb(String.valueOf(".*").concat(param).concat(".*"),esIDs)));
        return count;
    }
    
}
