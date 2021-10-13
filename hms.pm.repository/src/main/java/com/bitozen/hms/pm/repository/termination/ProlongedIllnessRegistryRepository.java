package com.bitozen.hms.pm.repository.termination;

import com.bitozen.hms.projection.termination.ProlongedIllnessRegistryEntryProjection;
import com.bitozen.hms.pm.common.ProlongedIllnessStatus;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Dumayangsari
 */
@Repository
@Transactional
public interface ProlongedIllnessRegistryRepository extends MongoRepository<ProlongedIllnessRegistryEntryProjection, Long>{
    
    Optional<ProlongedIllnessRegistryEntryProjection> findFirstByOrderByIdDesc();
    
    Optional<ProlongedIllnessRegistryEntryProjection> findOneByPiIDAndPiStatus(String piID, ProlongedIllnessStatus piStatus);
    
    @Query(value = "{$or: [{ 'piID' : {$regex: ?0,$options: 'i'}}," +
            " {'metadata.es.esID' : {$regex: ?0,$options: 'i'}}]}")
    Page<ProlongedIllnessRegistryEntryProjection> findAllForWeb(String param, String piStatus, Pageable pageable);

    @Query(value = "{$or: [{ 'piID' : {$regex: ?0,$options: 'i'}}," +
            " {'metadata.es.esID' : {$regex: ?0,$options: 'i'}}]}", count=true)
    long countAllForWeb(String param, String piStatus);
}
