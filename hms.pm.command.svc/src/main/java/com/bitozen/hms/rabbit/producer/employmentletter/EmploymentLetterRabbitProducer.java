package com.bitozen.hms.rabbit.producer.employmentletter;

import com.bitozen.hms.common.dto.RabbitFileDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jeremia
 */
@Service
public class EmploymentLetterRabbitProducer {
    
    @Autowired
    RabbitTemplate rabbitTemplate;
    
    public void employmentLetterDocumentUploadProducer(RabbitFileDTO dto) {
        rabbitTemplate.convertAndSend("x-hms-pm", "employmentletter-upload-document", dto);
    }
    
}
