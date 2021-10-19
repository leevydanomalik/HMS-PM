package com.bitozen.hms.pm.common.dto.command.movement;

import com.bitozen.hms.pm.common.MVMemoState;
import com.bitozen.hms.pm.common.MVMemoStatus;
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
public class MVMemoChangeCommandDTO implements Serializable {

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
}
