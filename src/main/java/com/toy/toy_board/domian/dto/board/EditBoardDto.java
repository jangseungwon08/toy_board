package com.toy.toy_board.domian.dto.board;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class EditBoardDto {
    private String boardTitle;
    private String boardBody;
}
