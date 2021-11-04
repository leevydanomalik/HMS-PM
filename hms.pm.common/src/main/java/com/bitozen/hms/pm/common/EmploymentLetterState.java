package com.bitozen.hms.pm.common;

import java.util.Arrays;

/**
 *
 * @author Jeremia
 */
public enum EmploymentLetterState {

    EL_INITIATE("EL_INITIATE"),
    EL_SUBMISSION("EL_SUBMISSION"),
    EL_WAITING_APPROVAL("EL_WAITING_APPROVAL"),
    EL_EMPHEAD_APP("EL_EMPHEAD_APP"),
    EL_STAFF_BPO_APP("EL_STAFF_BPO_APP"),
    EL_EO_SDM_APP("EL_EO_SDM_APP"),
    EL_DD_SDM_APP("EL_DD_SDM_APP"),
    EL_PRESDIR_APP("EL_PRESDIR_APP");

    private String name;

    private EmploymentLetterState(String name) {
        this.name = name;
    }

    public EmploymentLetterState findEnum(String name) {
        return Arrays.stream(values())
                .filter(data -> data.name.equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

}
