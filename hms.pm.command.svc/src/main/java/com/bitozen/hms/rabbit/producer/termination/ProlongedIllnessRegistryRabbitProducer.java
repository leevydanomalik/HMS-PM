package com.bitozen.hms.rabbit.producer.termination;

import com.bitozen.hms.common.dto.RabbitFileDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Dumayangsari
 */
@Service
public class ProlongedIllnessRegistryRabbitProducer {
    
    @Autowired
    RabbitTemplate rabbitTemplate;

    public void ProlongedIllnessRegistryDocumentUploadProducer(RabbitFileDTO dto) {
        rabbitTemplate.convertAndSend("x-hms-pm", "prolonged-illness-registry-upload-image", dto);
    }
}
