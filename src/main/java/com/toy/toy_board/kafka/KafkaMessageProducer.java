package com.toy.toy_board.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaMessageProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(String topic, Object message){
        kafkaTemplate.send(topic, message)
                .whenComplete((res, ex) -> {
                    if(ex == null){
                        log.info("메세지 전송 성공: message = {} with offset = {}"
                                ,message ,res.getRecordMetadata().offset());
                    }
                    else{
                        log.info("메세지 전송 실패: Exception = {}"
                        , ex.getMessage());
                    }
                });
    }
}
