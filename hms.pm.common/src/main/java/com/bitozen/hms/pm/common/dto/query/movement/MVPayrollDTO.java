package com.bitozen.hms.pm.common.dto.query.movement;

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
public class MVPayrollDTO implements Serializable {

    private Double basicSalaryBefore;
    private Double basicSalaryAfter;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date effectiveStartDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date effectiveEndDate;

    @JsonIgnore
    public MVPayrollDTO getInstance() {
        return new MVPayrollDTO(
                0.0,
                0.0,
                new Date(),
                new Date()
        );
    }
}
