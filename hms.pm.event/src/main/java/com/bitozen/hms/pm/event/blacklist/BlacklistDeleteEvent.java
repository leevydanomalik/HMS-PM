package com.bitozen.hms.pm.event.blacklist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BlacklistDeleteEvent {

    private String blacklistID;
    private String updatedBy;
}
