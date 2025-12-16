package com.toy.toy_board.kafka.consumer.users;

import com.toy.toy_board.kafka.consumer.users.event.ChangeUserNickNameEvent;
import com.toy.toy_board.kafka.consumer.users.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaChangeUserMessageConsumer {
    private final UserInfoService userInfoService;

    @KafkaListener(topics = ChangeUserNickNameEvent.TOPIC,
    properties = {JsonDeserializer.VALUE_DEFAULT_TYPE + ":com.toy.toy_board.kafka.consumer.users.event.ChangeUserNickNameEvent"})
    public void handleChangeUserNickName(ChangeUserNickNameEvent event, Acknowledgment ack){
        try {
            log.info("받은 정보: {}, {}", event.getUserNickName(), event.getUserId());
            userInfoService.changeNickName(event);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("이벤트 처리 중 에러 발생:", e);
            // 선택사항 A: 에러가 나도 일단 커밋해버린다 (다음 메시지로 넘어가기 위해)
            ack.acknowledge();
            // 선택사항 B: 커밋 안 함 -> 카프카가 잠시 후 재전송 (일시적 DB 장애 등을 대비할 때)
        }
    }
}
