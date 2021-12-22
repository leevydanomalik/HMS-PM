package com.bitozen.hms.pm.common.dto.command.movement;

import com.bitozen.hms.common.dto.share.PositionOptimizeDTO;
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
public class MVPositionCreateDTO implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date effectiveStartDateAfter;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date effectiveEndDateAfter;
    private String esBefore;
    private String esAfter;
    private String positionBefore;
    private String positionAfter;
    private String levelBefore;
    private String levelAfter;
    private String gradeBefore;
    private String gradeAfter;

    @JsonIgnore
    public MVPositionCreateDTO getInstance() {
        return new MVPositionCreateDTO(
                new Date(),
                new Date(),
                "ES-1604890793920",
                "ES-1604890793920",
                "1633571519452",
                "1633577702157",
                "LVL-006",
                "LVL-006",
                "GRADE-001",
                "GRADE-001"
        );
    }
}
