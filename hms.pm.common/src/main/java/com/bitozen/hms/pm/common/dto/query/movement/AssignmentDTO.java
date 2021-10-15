package com.bitozen.hms.pm.common.dto.query.movement;

import com.bitozen.hms.common.dto.share.BizparOptimizeDTO;
import com.bitozen.hms.common.dto.share.ESOptimizeDTO;
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
public class AssignmentDTO implements Serializable {

    private String achievement;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date assignmentStartDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date assignmentEndDate;
    private ESOptimizeDTO assignmentPlace;
    private BizparOptimizeDTO assignmentType;
    private String reasonOfProposing;
}
