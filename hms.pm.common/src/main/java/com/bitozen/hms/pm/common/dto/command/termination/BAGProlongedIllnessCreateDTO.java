package com.bitozen.hms.pm.common.dto.command.termination;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BAGProlongedIllnessCreateDTO implements Serializable{
    
    private String plDuration;
    
    @JsonIgnore
    public BAGProlongedIllnessCreateDTO getInstance(){
        return new BAGProlongedIllnessCreateDTO(
                "KEY001"
        );
    }
    
}
