package com.rsuniverse.jobify_user.services;

import com.rsuniverse.jobify_user.models.pojos.KafkaEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, KafkaEvent> kafkaTemplate;

    public void sendMessage(String topic, KafkaEvent event) {
        kafkaTemplate.send(topic, event);
    }

}
