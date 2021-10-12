package com.bitozen.hms.pm.repository.blacklist;

import com.bitozen.hms.pm.common.BlacklistStatus;
import com.bitozen.hms.projection.blacklist.BlacklistEntryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface BlacklistRepository extends MongoRepository<BlacklistEntryProjection, Long> {

    Optional<BlacklistEntryProjection> findFirstByOrderByIdDesc();

    Optional<BlacklistEntryProjection> findOneByBlacklistIDAndBlacklistStatusNot(String blacklistID, BlacklistStatus blacklistStatus);

    @Query(value = "{$and: [{$or: [{ 'blacklistID' : {$regex: ?0,$options: 'i'}}," +
            " {'metadata.es.esID' : {$regex: ?0,$options: 'i'}}]}," +
            "{'blacklistStatus' : {$ne:?1}}]}")
    Page<BlacklistEntryProjection> findAllForWeb(String param, String blacklistStatus, Pageable pageable);

    @Query(value = "{$and: [{$or: [{ 'blacklistID' : {$regex: ?0,$options: 'i'}}," +
            " {'metadata.es.esID' : {$regex: ?0,$options: 'i'}}]}," +
            "{'blacklistStatus' : {$ne:?1}}]}")
    long countAllForWeb(String param, String blacklistStatus);
}
