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
                UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                MVSKStatus.INITIATE,
                MVSKState.INITIATE,
                "KEY001",
                "EMPNEW666",
                Boolean.TRUE,
                Boolean.TRUE,
                new ArrayList<>(),
                new ArrayList<>(),
                new Date()
        );
    }
}
