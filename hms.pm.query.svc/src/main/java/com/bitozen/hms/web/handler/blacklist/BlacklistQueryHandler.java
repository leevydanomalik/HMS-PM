package com.bitozen.hms.web.handler.blacklist;

import com.bitozen.hms.pm.common.BlacklistStatus;
import com.bitozen.hms.pm.common.dto.query.blacklist.BlacklistDTO;
import com.bitozen.hms.pm.repository.blacklist.BlacklistRepository;
import com.bitozen.hms.projection.blacklist.BlacklistEntryProjection;
import com.bitozen.hms.web.assembler.BlacklistDTOAssembler;
import com.bitozen.hms.web.handler.blacklist.query.CountAllForWebQuery;
import com.bitozen.hms.web.handler.blacklist.query.GetAllForWebQuery;
import com.bitozen.hms.web.handler.blacklist.query.GetByIDQuery;
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
public class BlacklistQueryHandler {

    @Autowired
    private BlacklistRepository repository;

    @Autowired
    private BlacklistDTOAssembler assembler;

    @QueryHandler
    public BlacklistDTO getBlacklistByID(GetByIDQuery query){
        Optional<BlacklistEntryProjection> result = repository.findOneByBlacklistIDAndBlacklistStatusNot(query.getBlacklistID(), BlacklistStatus.INACTIVE);
        if (result.isPresent()){
            return assembler.toDTO(result.get());
        }
        return null;
    }

    @QueryHandler
    public List<BlacklistDTO> getAllBlacklistForWeb(GetAllForWebQuery query){
        Pageable pageable = PageRequest.of(query.getRequest().getOffset(), query.getRequest().getLimit(), Sort.by("creational.createdDate").descending());
        String param = String.valueOf(query.getRequest().getParams().get("param"));
        Page<BlacklistEntryProjection> results = repository.findAllForWeb(String.valueOf(".*").concat(param).concat(".*"), BlacklistStatus.INACTIVE.toString(), pageable);
        if(results.hasContent()){
            return assembler.toDTOs(results.getContent());
        }
        return Collections.EMPTY_LIST;
    }

    @QueryHandler
    public Integer countBlacklistForWeb(CountAllForWebQuery query) {
        String param = String.valueOf(query.getRequest().getParams().get("param"));
        Integer count = Integer.valueOf(String.valueOf(repository.countAllForWeb(String.valueOf(".*").concat(param).concat(".*"), BlacklistStatus.INACTIVE.toString())));
        return count;
    }
}
