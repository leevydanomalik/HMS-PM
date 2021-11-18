package com.bitozen.hms.pm.event.termination;

import com.bitozen.hms.pm.common.TerminationState;
import com.bitozen.hms.pm.common.TerminationStatus;
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
public class TerminationStateAndStatusChangeEvent {
    private String updatedBy;
    private Date updatedDate;
    
    private String tmnID;
    private TerminationState tmnState;
    private TerminationStatus tmnStatus;
    private Boolean isFinalApprove;
}
