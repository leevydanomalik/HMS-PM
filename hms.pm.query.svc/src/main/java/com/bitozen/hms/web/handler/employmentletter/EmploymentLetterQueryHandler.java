package com.bitozen.hms.web.handler.employmentletter;

import com.bitozen.hms.pm.common.EmploymentLetterStatus;
import com.bitozen.hms.pm.common.dto.query.employmentletter.EmploymentLetterDTO;
import com.bitozen.hms.pm.repository.employmentletter.EmploymentLetterRepository;
import com.bitozen.hms.projection.employmentletter.EmploymentLetterEntryProjection;
import com.bitozen.hms.web.assembler.EmploymentLetterDTOAssembler;
import com.bitozen.hms.web.handler.employmentletter.query.CountAllEmploymentLetterForWebQuery;
import com.bitozen.hms.web.handler.employmentletter.query.GetAllEmploymentLetterForWebQuery;
import com.bitozen.hms.web.handler.employmentletter.query.GetEmploymentLetterByIDQuery;
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

/**
 *
 * @author Jeremia
 */
@Component
public class EmploymentLetterQueryHandler {
    
    @Autowired
    private EmploymentLetterRepository repository;

    @Autowired
    private EmploymentLetterDTOAssembler assembler;
    
    @QueryHandler
    public EmploymentLetterDTO getEmploymentLetterByID(GetEmploymentLetterByIDQuery query){
        Optional<EmploymentLetterEntryProjection> result = repository.findOneByElIDAndElStatusNot(query.getElID(), EmploymentLetterStatus.INACTIVE);
        if (result.isPresent()){
            return assembler.toDTO(result.get());
        }
        return null;
    }

    @QueryHandler
    public List<EmploymentLetterDTO> getAllEmploymentLetterForWeb(GetAllEmploymentLetterForWebQuery query){
        Pageable pageable = PageRequest.of(query.getRequest().getOffset(), query.getRequest().getLimit(), Sort.by("creational.createdDate").descending());
        String param = String.valueOf(query.getRequest().getParams().get("param"));
        List<String> esIDs = (List<String>) query.getRequest().getParams().get("esIDs");
        Page<EmploymentLetterEntryProjection> results = repository.findAllForWeb(String.valueOf(".*").concat(param).concat(".*"), esIDs, pageable);
        if(results.hasContent()){
            return assembler.toDTOs(results.getContent());
        }
        return Collections.EMPTY_LIST;
    }

    @QueryHandler
    public Integer countEmploymentLetterForWeb(CountAllEmploymentLetterForWebQuery query) {
        String param = String.valueOf(query.getRequest().getParams().get("param"));
        List<String> esIDs = (List<String>) query.getRequest().getParams().get("esIDs");
        Integer count = Integer.valueOf(String.valueOf(repository.countAllForWeb(String.valueOf(".*").concat(param).concat(".*"), esIDs)));
        return count;
    }
    
}
