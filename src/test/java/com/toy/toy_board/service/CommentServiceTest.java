package com.toy.toy_board.service;

import com.toy.toy_board.common.aws.S3Builder;
import com.toy.toy_board.common.aws.S3Service;
import com.toy.toy_board.common.exception.NotFound;
import com.toy.toy_board.domian.dto.comment.CreateCommentDto;
import com.toy.toy_board.domian.dto.comment.CreateReplyDto;
import com.toy.toy_board.domian.entity.Board;
import com.toy.toy_board.domian.entity.Comment;
import com.toy.toy_board.domian.repository.BoardRepository;
import com.toy.toy_board.domian.repository.CommentRepository;
import com.toy.toy_board.service.comment.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
@SpringBootTest
@DisplayName("커맨트 서비스로 ")
public class CommentServiceTest {

    @Autowired
    BoardRepository boardRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    CommentService commentService;
    @MockitoBean
    S3Service s3Service;
    @MockitoBean
    S3Builder s3Builder;

    @Test
    public void CommentTest(){
//        given
        Board board = boardRepository.findById(1L).orElseThrow(() -> new NotFound("존재하지 않는 게시글입니다."));
//        보드와 직접 연결된 comment
        String userId= "seungwon0808";
        String userNickName = "숭이";
        CreateCommentDto dto = new CreateCommentDto();
        dto.setCommentBody("ㄹㅇㅋㅋ");
        dto.setBoardId(board.getId());

//        when
        Long res = commentService.createComment(dto, userId, userNickName);
//        then
        Comment comment = commentRepository.findById(res).orElseThrow(() -> new NotFound("x"));
        assertThat(res).isEqualTo(comment.getId());
        assertThat(0L).isEqualTo(comment.getDepth());
        assertThat(1L).isEqualTo(comment.getSequence());
        assertThat(1L).isEqualTo(comment.getGroupId());
    }

    @Test
    @DisplayName("대댓글로 계층적 구현을 할 수 있다.")
    public void ReplyTest(){
//        given
        Board board = boardRepository.findById(1L).orElseThrow(() -> new NotFound("존재하지 않는 게시글입니다."));
        String userId= "seungwon0808";
        String userNickName = "숭이";
        CreateReplyDto dto = new CreateReplyDto();
        dto.setCommentId(1L);
        dto.setBoardId(board.getId());
        dto.setReplyBody("진짜 ㄹㅇㅋㅋ");

//        when
        Long res = commentService.createReplyComment(dto, userId, userNickName);
//        then
        Comment comment = commentRepository.findById(1L).orElseThrow(() -> new NotFound("x"));
        Comment reply = commentRepository.findById(res).orElseThrow(() -> new NotFound("X"));
        assertThat(comment.getChildCount()).isEqualTo(1L);
        assertThat(reply.getDepth()).isEqualTo(1L);
        assertThat(reply.getSequence()).isEqualTo(2L);
        assertThat(reply.getGroupId()).isEqualTo(1L);
        System.out.println(reply.getDepth());
        System.out.println(reply.getSequence());
        System.out.println(reply.getChildCount());
        System.out.println(comment.getChildCount());
    }

    @Test
    @DisplayName("댓글 그룹 및 시퀀스 테스트")
    public void sequenceTest(){
//        given
        Board board = boardRepository.findById(1L).orElseThrow(() -> new NotFound("존재하지 않는 게시글입니다."));
        String userId= "seungwon0808";
        String userNickName = "숭이";
        CreateCommentDto dto = new CreateCommentDto();
        dto.setCommentBody("걍 ㄹㅇㅋㅋ만 외쳐라 ㅋㅋ");
        dto.setBoardId(board.getId());
        //        when
        Long res = commentService.createComment(dto, userId, userNickName);
//        then
        Comment comment = commentRepository.findById(res).orElseThrow(() -> new NotFound("x"));
        assertThat(res).isEqualTo(comment.getId());
        assertThat(0L).isEqualTo(comment.getDepth());
        assertThat(1L).isEqualTo(comment.getSequence());
        assertThat(2L).isEqualTo(comment.getGroupId());
        System.out.println(comment.getSequence());
    }
}
