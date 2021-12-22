package com.bitozen.hms.pm.common.dto.command.movement;

import com.bitozen.hms.common.dto.share.BizparOptimizeDTO;
import com.bitozen.hms.common.dto.share.ESOptimizeDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class AssignmentCreateDTO implements Serializable {

    private String achievement;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date assignmentStartDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date assignmentEndDate;
    private String assignmentPlace;
    private String assignmentType;
    private String reasonOfProposing;

    @JsonIgnore
    public AssignmentCreateDTO getInstance() {
        return new AssignmentCreateDTO(
                "CES",
                new Date(),
                new Date(),
                "1635845919333",
                "MOVTYPASS-003",
                "CES"
        );
    }
}
