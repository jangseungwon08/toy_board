package com.toy.toy_board.domian.dto;

import com.toy.toy_board.domian.entity.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class BoardDetailDto {
    private String boardTitle;
    private String boardBody;
    private Long viewCount;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private String boardWriteId;
    private String boardWriterNickName;
    private String imgUrl;
    private int likeCount;
    private boolean isLiked;
    private boolean isOwned;

    public static BoardDetailDto fromEntity(Board board, boolean isLiked, boolean isOwned){
        return BoardDetailDto.builder()
                .boardTitle(board.getBoardTitle())
                .boardBody(board.getBoardBody())
                .viewCount(board.getViewCount())
                .updatedAt(board.getUpdatedAt())
                .createdAt(board.getCreatedAt())
                .boardWriteId(board.getBoardWriterId())
                .boardWriterNickName(board.getBoardWriterNickName())
                .isLiked(isLiked)
                .isOwned(isOwned)
                .imgUrl(board.getImgUrl())
                .build();
    }
}
