package com.bitozen.hms.rabbit.producer.blacklist;

import com.bitozen.hms.common.dto.RabbitFileDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlacklistRabbitProducer {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void blacklistDocumentUploadProducer(RabbitFileDTO dto) {
        rabbitTemplate.convertAndSend("x-hms-pm", "blacklist-upload-image", dto);
    }
}
