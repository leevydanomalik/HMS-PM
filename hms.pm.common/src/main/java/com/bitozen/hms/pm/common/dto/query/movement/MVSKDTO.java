package com.bitozen.hms.pm.common.dto.query.movement;

import com.bitozen.hms.common.dto.share.BizparOptimizeDTO;
import com.bitozen.hms.common.dto.share.EmployeeOptimizeDTO;
import com.bitozen.hms.pm.common.MVSKState;
import com.bitozen.hms.pm.common.MVSKStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MVSKDTO implements Serializable {

    private String skID;
    private String skRefID;
    private String skDocNumber;
    private MVSKStatus skStatus;
    private MVSKState skState;
    private BizparOptimizeDTO skType;
    private EmployeeOptimizeDTO requestor;
    private Boolean isRevoke;
    private Boolean isFinalApprove;
    private List<String> skCopies;
    private List<String> skConsiderDesc;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date requestDate;
}
