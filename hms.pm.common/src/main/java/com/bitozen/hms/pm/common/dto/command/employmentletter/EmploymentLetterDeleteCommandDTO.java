package com.bitozen.hms.pm.common.dto.command.employmentletter;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author Jeremia
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmploymentLetterDeleteCommandDTO implements Serializable {
    
    private String elID;
    
    private String updatedBy;
    
}
