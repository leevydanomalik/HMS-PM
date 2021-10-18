package com.bitozen.hms.pm.common.dto.command.termination;

import com.bitozen.hms.pm.common.TerminationState;
import com.bitozen.hms.pm.common.TerminationStatus;
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
public class TerminationStateAndStatusChangeCommandDTO implements Serializable{
    private String tmnID;
    private TerminationState tmnState;
    private TerminationStatus tmnStatus;
    
    private String updatedBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "GMT+7")
    private Date updatedDate;
}
