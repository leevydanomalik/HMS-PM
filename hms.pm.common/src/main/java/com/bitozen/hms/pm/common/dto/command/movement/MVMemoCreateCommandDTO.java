package com.bitozen.hms.pm.common.dto.command.movement;

import com.bitozen.hms.pm.common.MVMemoState;
import com.bitozen.hms.pm.common.MVMemoStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MVMemoCreateCommandDTO implements Serializable {

    private String mvID;
    private String mvDetailID;

    private String memoID;
    private String memoRefID;
    private String memoDocNumber;
    private Boolean isRevoke;
    private Boolean isFinalApprove;
    private MVMemoStatus memoStatus;
    private MVMemoState memoState;
    private String memoType;
    private String requestor;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date requestDate;

    @JsonIgnore
    public MVMemoCreateCommandDTO getInstance() {
        return new MVMemoCreateCommandDTO(
                "1637909098351",
                "1637909132703",
                "1638953223569",
                "1637909098351",
                "MO/2021/VIABLE/EELS/2022",
                Boolean.TRUE,
                Boolean.TRUE,
                MVMemoStatus.INITIATE,
                MVMemoState.INITIATE,
                "MOVMEMOTYP-001",
                "1611128134009",
                new Date()
        );
    }
}
