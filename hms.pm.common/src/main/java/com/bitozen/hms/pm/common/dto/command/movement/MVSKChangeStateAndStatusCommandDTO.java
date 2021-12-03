package com.bitozen.hms.pm.common.dto.command.movement;

import com.bitozen.hms.pm.common.MVSKState;
import com.bitozen.hms.pm.common.MVSKStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MVSKChangeStateAndStatusCommandDTO implements Serializable{
    
    private String mvID;
    private String mvDetailID;

    private String skID;
    private MVSKStatus skStatus;
    private MVSKState skState;
    private Boolean isRevoke;
    private Boolean isFinalApprove;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date requestDate;
    
}
