package com.toy.toy_board.domian.dto.comment;

import com.toy.toy_board.domian.entity.Board;
import com.toy.toy_board.domian.entity.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateCommentDto {
    private Long boardId;
    private String commentBody;

    public Comment toEntity(String commentBody, String commentWriterId, String commentWriterNickName, Long groupId, Board board){
        return Comment.builder()
                .commentWriterId(commentWriterId)
                .commentWriterNickName(commentWriterNickName)
                .commentBody(commentBody)
                .board_comment(board)
                .depth(0L)
                .groupId(groupId)
                .sequence(1L)
                .childCount(0L)
                .build();
    }
}
