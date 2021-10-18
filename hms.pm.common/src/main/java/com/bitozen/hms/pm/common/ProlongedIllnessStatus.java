package com.bitozen.hms.pm.common;

import java.util.Arrays;

/**
 *
 * @author Dumayangsari
 */
public enum ProlongedIllnessStatus {
    
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE");
    
    private String name;
    
    private ProlongedIllnessStatus(String name){
        this.name = name;
    }
    
    public ProlongedIllnessStatus findEnum(String name){
        return Arrays.stream(values())
                .filter(data -> data.name.equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
