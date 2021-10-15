package com.bitozen.hms.pm.event.movement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MovementSKDeleteEvent {

    private String mvID;
    private String employees;
}
