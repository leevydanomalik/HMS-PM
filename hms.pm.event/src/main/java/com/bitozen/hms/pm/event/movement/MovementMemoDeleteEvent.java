package com.bitozen.hms.pm.event.movement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MovementMemoDeleteEvent {

    private String mvID;
    private String employees;
}
