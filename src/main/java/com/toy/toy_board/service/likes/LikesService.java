package com.toy.toy_board.service.likes;


import com.toy.toy_board.common.exception.BadParameter;
import com.toy.toy_board.common.exception.NotFound;
import com.toy.toy_board.domian.entity.Board;
import com.toy.toy_board.domian.entity.Likes;
import com.toy.toy_board.domian.repository.BoardRepository;
import com.toy.toy_board.domian.repository.LikesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikesService {
    private final LikesRepository likesRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public Long likes(Long boardId, String userId){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFound("존재하지 않는 게시글입니다."));
        if(board.isDeleted()){
            throw new BadParameter("이미 삭제된 게시글입니다.");
        }
        Optional<Likes> likes = likesRepository.findLikesStatus(board.getId(), userId);
        if(likes.isPresent()){
            likesRepository.delete(likes.get());
        }
        else{
            Likes newLikes = Likes.builder()
                    .board(board)
                    .likesWriterId(userId)
                    .build();
            likesRepository.save(newLikes);
        }
        return likesRepository.likesCount(board.getId());
    }
}
