package com.bitozen.hms.pm.repository.movement;

import com.bitozen.hms.pm.common.MVStatus;
import com.bitozen.hms.projection.movement.MovementEntryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface MovementRepository extends MongoRepository<MovementEntryProjection, Long> {

    Optional<MovementEntryProjection> findFirstByOrderByIdDesc();

    Optional<MovementEntryProjection> findOneByMvIDAndMvStatusNot(String mvID, MVStatus status);

    @Query(value = "{$and: [{$or: [{ 'mvID' : {$regex: ?0,$options: 'i'}}," +
            "{'mvNotes' : {$regex: ?0,$options: 'i'}}]}," +
            "{'metadata.es.esID' : {$in : ?1}}," +
            "{'mvStatus' : {$ne:?2}}]}")
    Page<MovementEntryProjection> findAllForWeb(String param, List<String> esIDs, String status, Pageable pageable);

    @Query(value = "{$and: [{$or: [{ 'mvID' : {$regex: ?0,$options: 'i'}}," +
            "{'mvNotes' : {$regex: ?0,$options: 'i'}}]}," +
            "{'metadata.es.esID' : {$in : ?1}}," +
            "{'mvStatus' : {$ne:?2}}]}", count=true)
    long countAllForWeb(String param, List<String> esIDs, String status);
}
