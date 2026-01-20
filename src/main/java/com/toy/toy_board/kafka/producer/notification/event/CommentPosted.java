package com.toy.toy_board.kafka.producer.notification.event;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateComment {
    public static final String TOPIC = "comment"
}
