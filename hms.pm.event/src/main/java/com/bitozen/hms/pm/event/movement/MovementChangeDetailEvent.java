package com.bitozen.hms.pm.event.movement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MovementChangeDetailEvent {

    private String mvID;
    private String employees;

    private String updatedBy;
    private Date updatedDate;
}
