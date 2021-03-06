package com.bitozen.hms.rabbit.producer.termination;

import com.bitozen.hms.common.dto.RabbitFileDTO;
import com.bitozen.hms.pm.common.dto.command.termination.TerminationCreateCommandDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TerminationProducer {
    
    @Autowired
    RabbitTemplate rabbitTemplate;

    public void uploadDocumentProducer(RabbitFileDTO obj) {
        rabbitTemplate.convertAndSend("x-hms-pm", "termination-upload-document", obj);
    }

    public void terminationMigrationProducer(TerminationCreateCommandDTO obj) {
        rabbitTemplate.convertAndSend("x-hms-pm", "termination-migration", obj);
    }
}
