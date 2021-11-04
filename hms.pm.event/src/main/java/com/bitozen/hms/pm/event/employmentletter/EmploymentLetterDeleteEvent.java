package com.bitozen.hms.pm.event.employmentletter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *
 * @author Jeremia
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EmploymentLetterDeleteEvent {

    private String elID;
    private String updatedBy;

}
