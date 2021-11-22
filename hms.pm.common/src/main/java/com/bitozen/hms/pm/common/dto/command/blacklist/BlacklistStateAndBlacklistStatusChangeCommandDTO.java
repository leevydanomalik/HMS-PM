package com.bitozen.hms.pm.common.dto.command.blacklist;

import com.bitozen.hms.pm.common.BlacklistState;
import com.bitozen.hms.pm.common.BlacklistStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author Dumayangsari
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BlacklistStateAndBlacklistStatusChangeCommandDTO implements Serializable{
    
    private String blacklistID;
    private BlacklistStatus blacklistStatus;
    private BlacklistState blacklistState;
    private Boolean isFinalApprove;
    
    private String updatedBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "GMT+7")
    private Date updatedDate;
}
