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
public interface EmploymentLetterRepository extends MongoRepository<EmploymentLetterEntryProjection, Long> {

    Optional<EmploymentLetterEntryProjection> findFirstByOrderByIdDesc();

    Optional<EmploymentLetterEntryProjection> findOneByElIDAndElStatusNot(String elID, EmploymentLetterStatus elStatus);

    Optional<EmploymentLetterEntryProjection> findOneByElDocNumberAndElStatusNot(String elDocNumber, EmploymentLetterStatus elStatus);

    @Query(value = "{$and: [{$or: [{ 'elID' : {$regex: ?0,$options: 'i'}},"
            + "{'elDocNumber' : {$regex: ?0,$options: 'i'}}]},"          
            + "{'metadata.es.esID' : {$in : ?1}},"
            + "{'elStatus' : {$ne:?2}}]}")
    Page<EmploymentLetterEntryProjection> findAllForWeb(String param, List<String> esIDs, String elStatus, Pageable pageable);

    @Query(value = "{$and: [{$or: [{ 'elID' : {$regex: ?0,$options: 'i'}},"
            + "{'elDocNumber' : {$regex: ?0,$options: 'i'}}]},"
            + "{'metadata.es.esID' : {$in : ?1}},"
            + "{'elStatus' : {$ne:?2}}]}", count = true)
    long countAllForWeb(String param, List<String> esIDs, String elStatus);
    
    @Query(value = "{$and: [{$or: [{ 'elID' : {$regex: ?0,$options: 'i'}},"
            + " {'metadata.es.esID' : {$regex: ?0,$options: 'i'}}]},"
            + "{'elStatus' : {$ne:'INACTIVE'}},"
            + "{'requestor.empID': {$regex: ?1,$options: 'i'}}]}")
    Page<EmploymentLetterEntryProjection> findAllByESSAndParamSearch(String param, String empID, Pageable pageable);
    
    @Query(value = "{$and: [{$or: [{ 'elID' : {$regex: ?0,$options: 'i'}},"
            + " {'metadata.es.esID' : {$regex: ?0,$options: 'i'}}]},"
            + "{'elStatus' : {$ne:'INACTIVE'}},"
            + "{'requestor.empID': {$regex: ?1,$options: 'i'}}]}", count = true)
    long countAllForWebESS(String param, String empID);

}
