package com.bitozen.hms.pm.common.dto.command.employmentletter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author Jeremia
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VisaSpecificationCreateDTO implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date departureDate;
    private String destination;
    private List<String> familyInfos;
    
    @JsonIgnore
    public VisaSpecificationCreateDTO getInstance() {
        return new VisaSpecificationCreateDTO(
                
                new Date(),
                "Rep. Congo",
                new ArrayList<>()
                
        );
    }

}
