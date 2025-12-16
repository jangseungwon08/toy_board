package com.toy.toy_board.kafka.consumer.users.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeUserNickNameEvent {
    public static final String TOPIC = "changeUserNickName";
    private String userNickName;
    private String userId;
}
