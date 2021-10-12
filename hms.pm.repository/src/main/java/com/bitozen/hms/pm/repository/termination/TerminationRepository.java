package com.bitozen.hms.pm.repository.termination;

import com.bitozen.hms.pm.common.TerminationStatus;
import com.bitozen.hms.projection.termination.TerminationEntryProjection;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface TerminationRepository extends MongoRepository<TerminationEntryProjection, Long> {

    Optional<TerminationEntryProjection> findFirstByOrderByIdDesc();

    Optional<TerminationEntryProjection> findOneByTmnIDAndTmnStatusNot(String tmnID, TerminationStatus tmnStatus);

    @Query(value = "{$and: [{$or: [{ 'tmnID' : {$regex: ?0,$options: 'i'}},"
            + " {'metadata.es.esID' : {$regex: ?0,$options: 'i'}}]},"
            + "{'tmnStatus' : {$ne:'INACTIVE'}}]}")
    Page<TerminationEntryProjection> findAllForWeb(String param, Pageable pageable);

    @Query(value = "{$and: [{$or: [{ 'tmnID' : {$regex: ?0,$options: 'i'}},"
            + " {'metadata.es.esID' : {$regex: ?0,$options: 'i'}}]},"
            + "{'tmnStatus' : {$ne:'INACTIVE'}}]}", count = true)
    long countAllForWeb(String param);
}
