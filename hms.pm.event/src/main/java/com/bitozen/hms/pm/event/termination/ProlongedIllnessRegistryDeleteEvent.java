package com.bitozen.hms.pm.event.termination;

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
public class ProlongedIllnessRegistryDeleteEvent {
    
    private String piID;

    private String updatedBy;
}
