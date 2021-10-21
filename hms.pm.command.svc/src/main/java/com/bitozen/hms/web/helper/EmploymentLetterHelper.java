package com.bitozen.hms.web.helper;

import com.bitozen.hms.pm.common.dto.command.employmentletter.VisaSpecificationCreateDTO;
import com.bitozen.hms.pm.common.dto.query.employmentletter.VisaSpecificationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 *
 * @author Jeremia
 */
@Component
@Slf4j
public class EmploymentLetterHelper {

    public VisaSpecificationDTO toVisaSpecification(VisaSpecificationCreateDTO dto) {
        VisaSpecificationDTO visa = new VisaSpecificationDTO();
        visa.setDepartureDate(dto.getDepartureDate());
        visa.setDestination(dto.getDestination());
        visa.setFamilyInfos(dto.getFamilyInfos());

        return visa;
    }

}
