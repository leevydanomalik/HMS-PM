package com.bitozen.hms.pm.event.blacklist;

import com.bitozen.hms.pm.common.BlacklistState;
import com.bitozen.hms.pm.common.BlacklistStatus;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *
 * @author Dumayangsari
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BlacklistStateAndStatusChangeEvent {
    
    private String updatedBy;
    private Date updatedDate;
    
    private String blacklistID;
    private BlacklistState blacklistState;
    private BlacklistStatus blacklistStatus;
    private Boolean isFinalApprove;
    
}
