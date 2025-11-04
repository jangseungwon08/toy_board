package com.toy.toy_board.domian.dto;

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

    public Board toEntity(String boardTitle, String boardBody, String boardWriterId, String boardWriterNickName, String imgUrl){
        return Board.builder()
                .boardTitle(boardTitle)
                .boardBody(boardBody)
                .boardWriterId(boardWriterId)
                .boardWriterNickName(boardWriterNickName)
                .imgUrl(imgUrl)
                .build();
    }
}
