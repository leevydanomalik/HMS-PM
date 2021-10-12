package com.bitozen.hms.web.handler.termination;

import com.bitozen.hms.pm.common.ProlongedIllnessStatus;
import com.bitozen.hms.pm.common.dto.query.termination.ProlongedIllnessRegistryDTO;
import com.bitozen.hms.pm.repository.termination.ProlongedIllnessRegistryRepository;
import com.bitozen.hms.projection.termination.ProlongedIllnessRegistryEntryProjection;
import com.bitozen.hms.web.assembler.ProlongedIllnessRegistryDTOAssembler;
import com.bitozen.hms.web.handler.termination.query.CountAllForWebQuery;
import com.bitozen.hms.web.handler.termination.query.GetAllForWebQuery;
import com.bitozen.hms.web.handler.termination.query.GetByIDQuery;
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
 * @author Dumayangsari
 */
@Component
public class ProlongedIllnessRegistryQueryHandler {
    @Autowired
    private ProlongedIllnessRegistryRepository repository;

    @Autowired
    private ProlongedIllnessRegistryDTOAssembler assembler;

    @QueryHandler
    public ProlongedIllnessRegistryDTO getProlongedIllnessRegistryByID(GetByIDQuery query){
        Optional<ProlongedIllnessRegistryEntryProjection> result = repository.findOneByPiIDAndPiStatus(query.getPiID(), ProlongedIllnessStatus.ACTIVE);
        if (result.isPresent()){
            return assembler.toDTO(result.get());
        }
        return null;
    }

    @QueryHandler
    public List<ProlongedIllnessRegistryDTO> getAllProlongedIllnessRegistryForWeb(GetAllForWebQuery query){
        Pageable pageable = PageRequest.of(query.getRequest().getOffset(), query.getRequest().getLimit(), Sort.by("creational.createdDate").descending());
        String param = String.valueOf(query.getRequest().getParams().get("param"));
        Page<ProlongedIllnessRegistryEntryProjection> results = repository.findAllForWeb(String.valueOf(".*").concat(param).concat(".*"), ProlongedIllnessStatus.ACTIVE.toString(), pageable);
        if(results.hasContent()){
            return assembler.toDTOs(results.getContent());
        }
        return Collections.EMPTY_LIST;
    }

    @QueryHandler
    public Integer countProlongedIllnessRegistryForWeb(CountAllForWebQuery query) {
        String param = String.valueOf(query.getRequest().getParams().get("param"));
        Integer count = Integer.valueOf(String.valueOf(repository.countAllForWeb(String.valueOf(".*").concat(param).concat(".*"), ProlongedIllnessStatus.ACTIVE.toString())));
        return count;
    }
}
