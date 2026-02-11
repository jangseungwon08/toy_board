package com.toy.toy_board.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.toy.toy_board.common.aws.S3Service;
import com.toy.toy_board.common.exception.NotFound;
import com.toy.toy_board.domian.dto.board.BoardDetailDto;
import com.toy.toy_board.domian.dto.board.CreateBoardDto;
import com.toy.toy_board.domian.entity.Board;
import com.toy.toy_board.domian.repository.BoardRepository;
import com.toy.toy_board.service.board.BoardInfoService;
import com.toy.toy_board.service.board.BoardService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.mockito.BDDMockito.given;      // given 사용을 위해
import static org.mockito.ArgumentMatchers.any;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "eureka.client.enabled=false",
        "cloud.aws.credentials.access-key=test_access_key",
        "cloud.aws.credentials.secret-key=test_secret_key",
        "cloud.aws.region.static=ap-northeast-2",
        "cloud.aws.s3.bucket=test_bucket"
})
@DisplayName("보드 서비스로")
public class BoardInfoServiceTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    BoardInfoService boardInfoService;

    @Autowired
    BoardService boardService;

    @MockitoBean
    private S3Service s3Service;

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

    @Test
    @Transactional
    @DisplayName("보드 페이징 테스트")
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // H2 사용
    public void pagingBoardTest() throws IOException {
        for(int i = 0; i< 10; i++){
            CreateBoardDto dto = CreateBoardDto.builder()
                    .boardBody("테스트 본문")
                    .boardTitle("테스트 타이틀")
                    .build();
            String userId = "seungwon0808";
            String userNickName = "숭이";
            given(s3Service.uploadFile(any(), any())).willReturn("https://fake-url.com/img.png");
            MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png", new byte[10]);
//        when
            Long res = boardService.createBoard(dto, userId, userNickName, null);
//        then
            System.out.println(res);
        }
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<Board> res = boardRepository.findAll(pageable);
        assertThat(res.getTotalElements()).isEqualTo(10); // 전체 데이터 10개 [1]
        assertThat(res.getSize()).isEqualTo(10);           // 페이지 사이즈 3
        assertThat(res.getNumber()).isEqualTo(0);         // 요청 페이지 0
        assertThat(res.getTotalPages()).isEqualTo(1);     // 전체 페이지 4 (10/3)
        assertThat(res.getContent().size()).isEqualTo(10);
        try{
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(res);
            System.out.println("=================");
            System.out.println(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
