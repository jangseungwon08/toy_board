package com.toy.toy_board.service;

import com.toy.toy_board.domian.dto.CreateBoardDto;
import com.toy.toy_board.domian.dto.EditBoardDto;
import com.toy.toy_board.domian.repository.BoardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
@SpringBootTest
@DisplayName("보드 서비스로")
public class BoardServiceTest {
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private BoardService boardService;

    @Test
    @DisplayName("보드를 생성할 수 있다.")
    public void boardCreateTest(){
        CreateBoardDto dto = new CreateBoardDto();
        dto.setBoardBody("테스트 본문");
        dto.setBoardTitle("테스트 타이틀");
        String userId = "seungwon0808";
        String userNickName = "숭이";

        Long res = boardService.createBoard(dto, userId, userNickName);
        System.out.println(res);
    }

    @Test
    @DisplayName("보드 제목을 수정할 수 있다.")
    public void editBoardTitleTest(){
        String boardTitle = "테스트 제목 수정입니다.";
        Long boardId = 3L;
        String userId = "seungwon0808";
        EditBoardDto dto = new EditBoardDto();
        dto.setBoardTitle(boardTitle);
        Long res = boardService.editBoard(dto, userId, boardId);
        assertThat(res).isEqualTo(boardId);
    }

    @Test
    @DisplayName("보드 본문을 수정할 수 있다.")
    public void editBoardBodyTest(){
        String boardBody = "테스트 본문 수정입니다.";
        Long boardId = 3L;
        String userId = "seungwon0808";
        EditBoardDto dto = new EditBoardDto();
        dto.setBoardBody(boardBody);
        Long res = boardService.editBoard(dto, userId, boardId);
        assertThat(res).isEqualTo(boardId);
    }
}
