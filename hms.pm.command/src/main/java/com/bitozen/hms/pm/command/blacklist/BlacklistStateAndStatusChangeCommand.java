package com.bitozen.hms.pm.command.blacklist;

import com.bitozen.hms.pm.common.BlacklistState;
import com.bitozen.hms.pm.common.BlacklistStatus;
import java.util.Date;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 *
 * @author Dumayangsari
 */
@Value
public class BlacklistStateAndStatusChangeCommand {
    
    @TargetAggregateIdentifier
    private String blacklistID;
    private BlacklistState blacklistState;
    private BlacklistStatus blacklistStatus;
    private Boolean isFinalApprove;
    
    private String updatedBy;
    private Date updatedDate;
}
