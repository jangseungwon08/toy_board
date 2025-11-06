package com.toy.toy_board.domian.dto.board;

import com.toy.toy_board.domian.entity.Board;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBoardDto {
    private String boardTitle;
    private String boardBody;
    private Long viewCount;
    private String imgUrl;

    public Board toEntity(String boardWriterId, String boardWriterNickName){
        return Board.builder()
                .boardTitle(this.boardTitle)
                .boardBody(this.boardBody)
                .boardWriterId(boardWriterId)
                .boardWriterNickName(boardWriterNickName)
                .imgUrl(this.imgUrl)
                .build();
    }
}
