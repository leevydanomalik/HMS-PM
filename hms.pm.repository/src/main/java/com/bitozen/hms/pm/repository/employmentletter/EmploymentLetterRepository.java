package com.bitozen.hms.pm.repository.employmentletter;

import com.bitozen.hms.pm.common.EmploymentLetterStatus;
import com.bitozen.hms.projection.employmentletter.EmploymentLetterEntryProjection;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jeremia                                                                                                                                                                                                                                        
 */
@Repository
@Transactional
public interface EmploymentLetterRepository extends MongoRepository<EmploymentLetterEntryProjection, Long>{
    
    Optional<EmploymentLetterEntryProjection> findFirstByOrderByIdDesc();

    Optional<EmploymentLetterEntryProjection> findOneByElIDAndElStatusNot(String elID, EmploymentLetterStatus elStatus);

    @Query(value = "{$and: [{$or: [{ 'elID' : {$regex: ?0,$options: 'i'}}," +
            "{'elDocNumber' : {$regex: ?0,$options: 'i'}}]}," +
            "{'metadata.es.esID' : {$in : ?1}}," +
            "{'elStatus' : {$ne:'INITIATE'}}]}")
    Page<EmploymentLetterEntryProjection> findAllForWeb(String param, List<String> esIDs, Pageable pageable);

    @Query(value = "{$and: [{$or: [{ 'elID' : {$regex: ?0,$options: 'i'}}," +
            "{'elDocNumber' : {$regex: ?0,$options: 'i'}}]}," +
            "{'metadata.es.esID' : {$in : ?1}}," +
            "{'elStatus' : {$ne:'INITIATE'}}]}", count=true)
    long countAllForWeb(String param, List<String> esIDs);
    
}
