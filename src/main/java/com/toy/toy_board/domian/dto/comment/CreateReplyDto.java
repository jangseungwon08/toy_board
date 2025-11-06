package com.toy.toy_board.domian.dto.comment;


import com.toy.toy_board.domian.entity.Board;
import com.toy.toy_board.domian.entity.Comment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReplyDto {
    private Long boardId;
    private Long commentId;
    private String replyBody;

    public Comment toEntity(String replyBody, String userId, String userNickName, Board board){
        return Comment.builder()
                .commentWriterId(userId)
                .commentWriterNickName(userNickName)
                .commentBody(replyBody)
                .board_comment(board)
                .build();
    }
}
