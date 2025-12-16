package com.toy.toy_board.kafka.consumer.users.service;

import com.toy.toy_board.common.exception.NotFound;
import com.toy.toy_board.domian.entity.Board;
import com.toy.toy_board.domian.repository.BoardRepository;
import com.toy.toy_board.kafka.consumer.users.event.ChangeUserNickNameEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserInfoService {
    private final BoardRepository boardRepository;

    @Transactional
    public void changeNickName(ChangeUserNickNameEvent event){
        List<Board> boards = boardRepository.findAllByBoardWriterId(event.getUserId());
        if(boards.isEmpty()){
            throw new NotFound("존재하지 않는 사용자입니다.");
        }
        for(Board board: boards){
            board.changeWriterNickName(event.getUserNickName());
        }
    }
}
