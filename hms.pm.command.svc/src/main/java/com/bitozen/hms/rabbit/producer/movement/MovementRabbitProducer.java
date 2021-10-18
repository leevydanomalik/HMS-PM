package com.bitozen.hms.rabbit.producer.movement;

import com.bitozen.hms.common.dto.RabbitFileDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovementRabbitProducer {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void movementDocumentUploadProducer(RabbitFileDTO dto) {
        rabbitTemplate.convertAndSend("x-hms-pm", "movement-upload-image", dto);
    }

    public void movementSKDocumentUploadProducer(RabbitFileDTO dto) {
        rabbitTemplate.convertAndSend("x-hms-pm", "movement-sk-upload-image", dto);
    }

    public void movementMemoDocumentUploadProducer(RabbitFileDTO dto) {
        rabbitTemplate.convertAndSend("x-hms-pm", "movement-memo-upload-image", dto);
    }
}
