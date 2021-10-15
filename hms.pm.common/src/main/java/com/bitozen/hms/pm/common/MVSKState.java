package com.bitozen.hms.pm.common;

import java.util.Arrays;

public enum MVSKState {

    INITIATE("INITIATE"),
    SK_EO_DIKLAT_APPROVAL("SK EO DIKLAT APPROVAL"),
    SK_DDSDM_APPROVAL("SK DDSDM APPROVAL"),
    SUBMISSION("SUBMISSION"),
    SK_PRESDIR_APPROvAL("SK PRESDIR APPROVAL"),
    SK_STAFFBPO_APPROvAL("SK STAFFBPO APPROVAL"),
    SK_ASSDIR_APPROvAL("SK ASSDIR APPROVAL");

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
