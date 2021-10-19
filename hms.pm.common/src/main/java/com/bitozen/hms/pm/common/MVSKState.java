package com.bitozen.hms.pm.common;

import java.util.Arrays;

public enum MVSKState {

    INITIATE("INITIATE"),
    SK_EO_DIKLAT_APP("SK EO DIKLAT APPROVAL"),
    SK_DDSDM_APP("SK DDSDM APPROVAL"),
    SUBMISSION("SUBMISSION"),
    SK_PRESDIR_APP("SK PRESDIR APPROVAL"),
    SK_STAFFBPO_APP("SK STAFFBPO APPROVAL"),
    SK_ASSDIR_APP("SK ASSDIR APPROVAL");

    private String name;

    private MVSKState(String name) {
        this.name = name;
    }

    public MVSKState findEnum(String name) {
        return Arrays.stream(values())
                .filter(data -> data.name.equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
