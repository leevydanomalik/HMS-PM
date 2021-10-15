package com.bitozen.hms.pm.common.dto.query.movement;

import com.bitozen.hms.common.dto.share.BizparOptimizeDTO;
import com.bitozen.hms.common.dto.share.ESOptimizeDTO;
import com.bitozen.hms.common.dto.share.PositionOptimizeDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MVPositionDTO implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date effectiveStartDateAfter;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date effectiveEndDateAfter;
    private ESOptimizeDTO esBefore;
    private ESOptimizeDTO esAfter;
    private PositionOptimizeDTO positionBefore;
    private PositionOptimizeDTO positionAfter;
    private BizparOptimizeDTO levelBefore;
    private BizparOptimizeDTO levelAfter;
    private BizparOptimizeDTO gradeBefore;
    private BizparOptimizeDTO gradeAfter;
}
