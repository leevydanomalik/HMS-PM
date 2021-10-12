package com.bitozen.hms.pm.event.termination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TerminationDeleteEvent {
    
    private String tmnID;
    
    private String updatedBy;
    
}
