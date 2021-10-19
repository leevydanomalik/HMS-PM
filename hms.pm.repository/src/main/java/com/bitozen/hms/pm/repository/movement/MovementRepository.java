package com.bitozen.hms.pm.repository.movement;

import com.bitozen.hms.pm.common.MVStatus;
import com.bitozen.hms.pm.common.dto.query.movement.MovementMemoDTOProjection;
import com.bitozen.hms.pm.common.dto.query.movement.MovementSKDTOProjection;
import com.bitozen.hms.pm.common.util.Count;
import com.bitozen.hms.projection.movement.MovementEntryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
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

    @Aggregation({
            "{'$unwind':{'path' : '$employees'}}",
            "{'$unwind':{'path' : '$employees.sks'}}",
            "{'$match' : {'$and':[{'employees.sks.skID':{$regex: ?0,$options: 'i'}}, {'employees.sks.skStatus':{$ne:?1}}]}}",
            "{'$group' :{'_id' : { 'skID' : '$employees.sks.skID', 'skRefID':'$employees.sks.skRefID', 'skDocNumber':'$employees.sks.skDocNumber', 'skStatus':'$employees.sks.skStatus', 'skState':'$employees.sks.skState', 'skType':'$employees.sks.skType', 'requestor':'$employees.sks.requestor', 'isRevoke':'$employees.sks.isRevoke', 'isFinalApprove':'$employees.sks.isFinalApprove', 'skCopies':'$employees.sks.skCopies', 'skConsiderDesc':'$employees.sks.skConsiderDesc', 'requestDate':'$employees.sks.requestDate'}}}",
            "{'$project' : { '_id' : 0, 'movement' : '$_id'}}"
    })
    List<MovementSKDTOProjection> findSKBySkID(String skID, String status);

    @Aggregation({
            "{'$unwind':{'path' : '$employees'}}",
            "{'$unwind':{'path' : '$employees.sks'}}",
            "{'$match' : {'$and':[{'employees.sks.requestor.empID':{$regex: ?0,$options: 'i'}}, {'employees.sks.skStatus':{$ne:?1}}]}}",
            "{'$group' :{'_id' : { 'skID' : '$employees.sks.skID', 'skRefID':'$employees.sks.skRefID', 'skDocNumber':'$employees.sks.skDocNumber', 'skStatus':'$employees.sks.skStatus', 'skState':'$employees.sks.skState', 'skType':'$employees.sks.skType', 'requestor':'$employees.sks.requestor', 'isRevoke':'$employees.sks.isRevoke', 'isFinalApprove':'$employees.sks.isFinalApprove', 'skCopies':'$employees.sks.skCopies', 'skConsiderDesc':'$employees.sks.skConsiderDesc', 'requestDate':'$employees.sks.requestDate'}}}",
            "{'$project' : { '_id' : 0, 'movement' : '$_id'}}"
    })
    List<MovementSKDTOProjection> findSKForWeb(String param, String status, Pageable pageable);

    @Aggregation({
            "{'$unwind':{'path' : '$employees'}}",
            "{'$unwind':{'path' : '$employees.sks'}}",
            "{'$match' : {'$and':[{'employees.sks.requestor.empID':{$regex: ?0,$options: 'i'}}, {'employees.sks.skStatus':{$ne:?1}}]}}",
            "{'$group' :{'_id' : { 'skID' : '$employees.sks.skID', 'skRefID':'$employees.sks.skRefID', 'skDocNumber':'$employees.sks.skDocNumber', 'skStatus':'$employees.sks.skStatus', 'skState':'$employees.sks.skState', 'skType':'$employees.sks.skType', 'requestor':'$employees.sks.requestor', 'isRevoke':'$employees.sks.isRevoke', 'isFinalApprove':'$employees.sks.isFinalApprove', 'skCopies':'$employees.sks.skCopies', 'skConsiderDesc':'$employees.sks.skConsiderDesc', 'requestDate':'$employees.sks.requestDate'}}}",
            "{'$project' : { '_id' : 0, 'movement' : '$_id'}}",
            "{'$count': 'totalCount'}"
    })
    Count countSKForWeb(String skID, String status);

    @Aggregation({
            "{'$unwind':{'path' : '$employees'}}",
            "{'$unwind':{'path' : '$employees.memos'}}",
            "{'$match' : {'$and':[{'employees.memos.memoID':{$regex: ?0,$options: 'i'}}, {'employees.memos.memoStatus':{$ne:?1}}]}}",
            "{'$group' :{'_id' : { 'memoID' : '$employees.memos.memoID', 'memoRefID':'$employees.memos.memoRefID', 'memoDocNumber':'$employees.memos.memoDocNumber', 'isRevoke':'$employees.memos.isRevoke', 'isFinalApprove':'$employees.memos.isFinalApprove', 'memoStatus':'$employees.memos.memoStatus', 'memoState':'$employees.memos.memoState', 'memoType':'$employees.memos.memoType', 'requestor':'$employees.memos.requestor', 'requestDate':'$employees.memos.requestDate'}}}",
            "{'$project' : { '_id' : 0, 'movement' : '$_id'}}"
    })
    List<MovementMemoDTOProjection> findMemoByMemoID(String skID, String status);

    @Aggregation({
            "{'$unwind':{'path' : '$employees'}}",
            "{'$unwind':{'path' : '$employees.memos'}}",
            "{'$match' : {'$and':[{'employees.memos.requestor.empID':{$regex: ?0,$options: 'i'}}, {'employees.memos.memoStatus':{$ne:?1}}]}}",
            "{'$group' :{'_id' : { 'memoID' : '$employees.memos.memoID', 'memoRefID':'$employees.memos.memoRefID', 'memoDocNumber':'$employees.memos.memoDocNumber', 'isRevoke':'$employees.memos.isRevoke', 'isFinalApprove':'$employees.memos.isFinalApprove', 'memoStatus':'$employees.memos.memoStatus', 'memoState':'$employees.memos.memoState', 'memoType':'$employees.memos.memoType', 'requestor':'$employees.memos.requestor', 'requestDate':'$employees.memos.requestDate'}}}",
            "{'$project' : { '_id' : 0, 'movement' : '$_id'}}"
    })
    List<MovementMemoDTOProjection> findMemoForWeb(String param, String status, Pageable pageable);

    @Aggregation({
            "{'$unwind':{'path' : '$employees'}}",
            "{'$unwind':{'path' : '$employees.memos'}}",
            "{'$match' : {'$and':[{'employees.memos.requestor.empID':{$regex: ?0,$options: 'i'}}, {'employees.memos.memoStatus':{$ne:?1}}]}}",
            "{'$group' :{'_id' : { 'memoID' : '$employees.memos.memoID', 'memoRefID':'$employees.memos.memoRefID', 'memoDocNumber':'$employees.memos.memoDocNumber', 'isRevoke':'$employees.memos.isRevoke', 'isFinalApprove':'$employees.memos.isFinalApprove', 'memoStatus':'$employees.memos.memoStatus', 'memoState':'$employees.memos.memoState', 'memoType':'$employees.memos.memoType', 'requestor':'$employees.memos.requestor', 'requestDate':'$employees.memos.requestDate'}}}",
            "{'$project' : { '_id' : 0, 'movement' : '$_id'}}",
            "{'$count': 'totalCount'}"
    })
    Count countMemoForWeb(String skID, String status);


}
