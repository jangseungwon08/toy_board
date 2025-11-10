package com.toy.toy_board.service;

import com.toy.toy_board.common.aws.S3Builder;
import com.toy.toy_board.common.aws.S3Service;
import com.toy.toy_board.common.exception.NotFound;
import com.toy.toy_board.domian.dto.comment.CreateCommentDto;
import com.toy.toy_board.domian.dto.comment.CreateReplyDto;
import com.toy.toy_board.domian.dto.comment.UpdateCommentDto;
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
        CreateCommentDto dto = CreateCommentDto.builder()
                .commentBody("ㄹㅇㅋㅋ")
                .boardId(board.getId())
                .build();

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
        CreateReplyDto dto = CreateReplyDto.builder()
                .commentId(1L)
                .boardId(board.getId())
                .replyBody("진짜  ㄹㅇㅋㅋ")
                .build();

//        when
        Long res = commentService.createReplyComment(dto, userId, userNickName);
//        then
        Comment comment = commentRepository.findById(1L).orElseThrow(() -> new NotFound("x"));
        Comment reply = commentRepository.findById(res).orElseThrow(() -> new NotFound("X"));
        assertThat(comment.getChildCount()).isEqualTo(1L);
        assertThat(reply.getDepth()).isEqualTo(1L);
        assertThat(reply.getSequence()).isEqualTo(3L);
        assertThat(reply.getGroupId()).isEqualTo(1L);
        System.out.println(reply.getDepth());
        System.out.println(reply.getSequence());
        System.out.println(reply.getChildCount());
        System.out.println(comment.getChildCount());
    }
    @Test
    @DisplayName("대대댓글 테스트")
    public void replyreplyTest() {
        //        given
        Board board = boardRepository.findById(1L).orElseThrow(() -> new NotFound("존재하지 않는 게시글입니다."));
        String userId = "seungwon0808";
        String userNickName = "숭이";
        CreateReplyDto dto = CreateReplyDto.builder()
                .commentId(2L)
                .boardId(board.getId())
                .replyBody("진짜  ㄹㅇㅋㅋ")
                .build();
//        when
        Long res = commentService.createReplyComment(dto, userId, userNickName);
//        then
        Comment comment = commentRepository.findById(res)
                .orElseThrow(() -> new NotFound("x"));
        assertThat(comment.getDepth()).isEqualTo(2L);
        assertThat(comment.getSequence()).isEqualTo(3L);
    }

    @Test
    @DisplayName("댓글 그룹 및 시퀀스 테스트")
    public void sequenceTest(){
//        given
        Board board = boardRepository.findById(1L).orElseThrow(() -> new NotFound("존재하지 않는 게시글입니다."));
        String userId= "seungwon0808";
        String userNickName = "숭이";
        CreateCommentDto dto = CreateCommentDto.builder()
                .commentBody("걍 ㄹㅇㅋㅋ만 외쳐라 ㅋㅋ")
                .boardId(board.getId())
                .build();
        //        when
        Long res = commentService.createComment(dto, userId, userNickName);
//        then
        Comment comment = commentRepository.findById(res).orElseThrow(() -> new NotFound("x"));
        assertThat(res).isEqualTo(comment.getId());
        assertThat(0L).isEqualTo(comment.getDepth());
        assertThat(1L).isEqualTo(comment.getSequence());
        assertThat(3L).isEqualTo(comment.getGroupId());
        System.out.println(comment.getSequence());
    }

    @Test
    @DisplayName("커멘트를 수정할 수 있다.")
    public void updateCommentTest(){
//        given
        Board board = boardRepository.findById(1L).orElseThrow(() -> new NotFound("존재하지 않는 게시글입니다."));
        String userId= "seungwon0808";
        String userNickName = "숭이";
        String updateComment = "ㄹㅇㅋㅋ만 외쳐라 ㅋㅋ 수정본";

        UpdateCommentDto dto =
                UpdateCommentDto.builder()
                        .commentBody(updateComment)
                        .build();

//        when
        Long res = commentService.updateComment(dto, 1L, userId);
//        then
        Comment comment = commentRepository.findById(res)
                .orElseThrow(() -> new NotFound("x"));

        assertThat(comment.getCommentBody()).isEqualTo("ㄹㅇㅋㅋ만 외쳐라 ㅋㅋ 수정본");
        assertThat(comment.getGroupId()).isEqualTo(1L);
        assertThat(comment.getSequence()).isEqualTo(1L);
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    public void deleteCommentTest(){
//        given
        Board board = boardRepository.findById(1L).orElseThrow(() -> new NotFound("존재하지 않는 게시글입니다."));
        String userId= "seungwon0808";
        String userNickName = "숭이";
        Long commentId = 1L;
//        when
        Long res = commentService.deleteComment(commentId, userId);
//        then
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFound("x"));
        Comment reply = commentRepository.findById(2L)
                        .orElseThrow(() -> new NotFound("X"));
        assertThat(comment.isDeleted()).isEqualTo(true);
        assertThat(comment.isDeleted()).isEqualTo(true);
    }
}
