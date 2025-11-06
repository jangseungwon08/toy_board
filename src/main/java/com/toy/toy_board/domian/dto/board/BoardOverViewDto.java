package com.toy.toy_board.domian.dto.board;


import com.toy.toy_board.domian.entity.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class BoardOverViewDto {
    private Long boardId;
    private String boardTitle;
    private String boardWriterNickName;
    private Long viewCount;
    private LocalDateTime createdAt;

    public static BoardOverViewDto fromEntity(Board board){
        return BoardOverViewDto.builder()
                .boardId(board.getId())
                .boardTitle(board.getBoardTitle())
                .boardWriterNickName(board.getBoardWriterNickName())
                .viewCount(board.getViewCount())
                .createdAt(board.getCreatedAt())
                .build();
    }
}
