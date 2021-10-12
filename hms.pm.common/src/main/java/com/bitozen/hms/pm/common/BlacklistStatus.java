package com.bitozen.hms.pm.common;

import java.util.Arrays;

public enum BlacklistStatus {

    INITIATE("INITIATE"),
    WAITING_APPROVAL("WAITING APPROVAL"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED"),
    REVISED("REVISED"),
    CANCELLED("CANCELLED"),
    INACTIVE("INACTIVE");

    private String name;

    private BlacklistStatus(String name) {
        this.name = name;
    }

    public BlacklistStatus findEnum(String name) {
        return Arrays.stream(values())
                .filter(data -> data.name.equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
