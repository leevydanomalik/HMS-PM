package com.bitozen.hms.pm.common;

import java.util.Arrays;

/**
 *
 * @author Jeremia
 */
public enum EmploymentLetterStatus {

    INITIATE("INITIATE"),
    WAITING_APPROVAL("WAITING_APPROVAL"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED"),
    REVISED("REVISED"),
    INACTIVE("INACTIVE");

    private String name;

    private EmploymentLetterStatus(String name) {
        this.name = name;
    }

    public EmploymentLetterStatus findEnum(String name) {
        return Arrays.stream(values())
                .filter(data -> data.name.equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

}
