package com.toy.toy_board.service;

import com.toy.toy_board.common.exception.NotFound;
import com.toy.toy_board.domian.dto.board.BoardDetailDto;
import com.toy.toy_board.domian.entity.Board;
import com.toy.toy_board.domian.repository.BoardRepository;
import com.toy.toy_board.service.board.BoardInfoService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
@SpringBootTest
@DisplayName("보드 서비스로")
public class BoardInfoServiceTest {
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    BoardInfoService boardInfoService;

    @Test
    @DisplayName("회원이 아닌 유저도 보드상세 정보를 볼 수 있다.")
    public void getBoardDetailTest(){
        Long boardId = 3L;
        String userId = null;
        String userNickName = null;
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFound("존재하지 않는 게시글입니다."));
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        BoardDetailDto dto = boardInfoService.getBoardDetail(boardId, userId, userNickName,request, response);
        Long DbBoardId = board.getId();

        assertThat(dto).isNotNull();
        assertThat(dto.getViewCount()).isEqualTo(1L);
        Cookie viewCookie = response.getCookie("postView");
        assertThat(viewCookie).isNotNull();
        assertThat(viewCookie.getValue()).contains("[" + DbBoardId + "]");
    }
}
