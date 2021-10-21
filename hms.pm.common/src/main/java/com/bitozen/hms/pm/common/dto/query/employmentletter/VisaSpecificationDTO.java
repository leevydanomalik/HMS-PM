package com.bitozen.hms.pm.common.dto.query.employmentletter;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
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
public class VisaSpecificationDTO implements Serializable{
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date departureDate;
    private String destination;
    private List<String> familyInfos;
    
}
