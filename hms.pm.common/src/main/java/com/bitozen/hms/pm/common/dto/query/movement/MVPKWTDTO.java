package com.bitozen.hms.pm.common.dto.query.movement;

import com.bitozen.hms.common.status.DataStatus;
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
public class MVPKWTDTO implements Serializable {

    private String pkwtID;
    private DataStatus pkwtStatus;
    private String pkwtDocNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date requestDate;
}
