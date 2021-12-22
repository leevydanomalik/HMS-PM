package com.bitozen.hms.pm.common.dto.command.movement;

import com.bitozen.hms.pm.common.MVSKState;
import com.bitozen.hms.pm.common.MVSKStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MVSKCreateCommandDTO implements Serializable {

    private String mvID;
    private String mvDetailID;

    private String skID;
    private String skRefID;
    private String skDocNumber;
    private MVSKStatus skStatus;
    private MVSKState skState;
    private String skType;
    private String requestor;
    private Boolean isRevoke;
    private Boolean isFinalApprove;
    private List<String> skCopies;
    private List<String> skConsiderDesc;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date requestDate;

    @JsonIgnore
    public MVSKCreateCommandDTO getInstance() {
        return new MVSKCreateCommandDTO(
                "1637909098351",
                "1637909132703",
                "19B930DF",
                "99DDBF7E",
                "A3C361E1",
                MVSKStatus.INITIATE,
                MVSKState.INITIATE,
                "MOVSKTYP-006",
                "1611128134004",
                Boolean.TRUE,
                Boolean.TRUE,
                new ArrayList<>(),
                new ArrayList<>(),
                new Date()
        );
    }
}
