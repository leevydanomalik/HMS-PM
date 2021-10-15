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
                "ES-001",
                "ES-001",
                "OU-001",
                "OU-001",
                "KEY001",
                "KEY001",
                "KEY001",
                "KEY001"
        );
    }
}
