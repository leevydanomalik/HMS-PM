package com.bitozen.hms.pm.common;

import java.util.Arrays;

public enum MVMemoState {

    INITIATE("INITIATE"),
    MEMO_EO_DIKLAT_APP("MEMO EO DIKLAT APPROVAL"),
    MEMO_DDSDM_APP("MEMO DDSDM APPROVAL"),
    MEMO_STAFFBPO_APP("MEMO STAFFBPO APPROVAL"),
    SUBMISSION("SUBMISSION");

    private String name;

    private MVMemoState(String name) {
        this.name = name;
    }

    public MVMemoState findEnum(String name) {
        return Arrays.stream(values())
                .filter(data -> data.name.equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
