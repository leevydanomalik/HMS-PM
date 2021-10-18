package com.bitozen.hms.pm.common;

import java.util.Arrays;

public enum BlacklistState {

    BLACKLIST_INITIATE("BLACKLIST INITIATE"),
    BLACKLIST_SUBMISSION("BLACKLIST SUBMISSION"),
    BLACKLIST_EMPHEAD_APP("BLACKLIST EMPHEAD APPROVAL"),
    BLACKLIST_KPNO_DDSDM_APP("BLACKLIST KPNO DDSDM APPROVAL");

    private String name;

    private BlacklistState(String name) {
        this.name = name;
    }

    public BlacklistState findEnum(String name) {
        return Arrays.stream(values())
                .filter(data -> data.name.equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
