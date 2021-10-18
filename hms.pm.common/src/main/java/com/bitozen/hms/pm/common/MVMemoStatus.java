package com.bitozen.hms.pm.common;

import java.util.Arrays;

public enum MVMemoStatus {

    INITIATE("INITIATE"),
    WAITING_APPROVAL("WAITING APPROVAL"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED"),
    REVISED("REVISED"),
    CANCELLED("CANCELLED"),
    INACTIVE("INACTIVE");

    private String name;

    private MVMemoStatus(String name) {
        this.name = name;
    }

    public MVMemoStatus findEnum(String name) {
        return Arrays.stream(values())
                .filter(data -> data.name.equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
