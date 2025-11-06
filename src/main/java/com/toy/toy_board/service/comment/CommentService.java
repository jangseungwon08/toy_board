package com.toy.toy_board.service.comment;

import com.toy.toy_board.common.exception.BadParameter;
import com.toy.toy_board.common.exception.NotFound;
import com.toy.toy_board.domian.dto.comment.CreateCommentDto;
import com.toy.toy_board.domian.dto.comment.CreateReplyDto;
import com.toy.toy_board.domian.entity.Board;
import com.toy.toy_board.domian.entity.Comment;
import com.toy.toy_board.domian.repository.BoardRepository;
import com.toy.toy_board.domian.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public Long createComment(CreateCommentDto createCommentDto, String userId, String userNickName){
        Board board = boardRepository.findById(createCommentDto.getBoardId())
                .orElseThrow(() -> new NotFound("존재하지 않는 게시글입니다."));
        if(board.isDeleted()) {
            throw new BadParameter("이미 삭제된 게시글입니다.");
        }
        Comment comment = createCommentDto.toEntity(createCommentDto.getCommentBody(), userId, userNickName, board);
//        루트 댓글 저장 로직
        Long maxGroupId = commentRepository.findMaxGroupId(board.getId());
        /*
        루트 댓글은 depth = 0 groupId는 해당 그룹 아이디 +1 번째 그룹 sequence는 무조건 1이 된다.
        왜냐하면 계층형적으로 하나의 그룹 안에 시퀀스를 나타내는 거기 때문에
         */
        comment.setDepth(0L);
        comment.setGroupId(maxGroupId);
        comment.setSequence(1L);
        comment.setChildCount(0L);
        try{
            commentRepository.save(comment);
        }
        catch (Exception e){
            log.info("댓글 저장 실패!!",e);
            throw new RuntimeException("댓글 저장중 치명적 버그 발생");
        }
        return comment.getId();
    }

    @Transactional
    public Long createReplyComment(CreateReplyDto createReplyDto, String userId, String userNickName){
        Comment comment = commentRepository.findById(createReplyDto.getCommentId())
                .orElseThrow(() -> new NotFound("존재하지 않는 댓글입니다."));
        Board board = comment.getBoard_comment();
        if(board == null) throw new NotFound("존재하지 않는 게시글입니다.");

        if(board.isDeleted()) throw new BadParameter("이미 삭제된 게시글입니다.");

        Comment reply = createReplyDto.toEntity(createReplyDto.getReplyBody(), userId, userNickName, board);
//        대댓글 진입점 확인
        Long insertPoint;
//        부모와 같은 깊이의 sequence 중 가장 작은 즉 바로 다음 seq을 가져옴
        Long nexSiblingSeq = commentRepository.findNextSiblingSequence(comment.getGroupId(), comment.getDepth(), comment.getSequence());
//        다음 seq => 즉 다음 형제 댓글이 있으면 => 진입점을 해당 seq으로 변경
        if(nexSiblingSeq != null) insertPoint = nexSiblingSeq;
        else{
//            다음 seq이 없으면 그룹 내 seq을 찾아서 +1로 변경
            insertPoint = commentRepository.findNextSequenceInGroup(comment.getGroupId());
        }
        commentRepository.increaseSequence(comment.getGroupId(), insertPoint);
        reply.setSequence(insertPoint);
        reply.setDepth(comment.getDepth()+1);
        reply.setGroupId(comment.getGroupId());
        reply.setChildCount(0L);
        reply.setParent(comment);
        comment.setChildCount(comment.getChildCount()+1);
        try{
            commentRepository.save(reply);
        }
        catch (Exception e){
            throw new RuntimeException("대댓글 생성 중 치명적 버그 발생!");
        }
        return reply.getId();
    }


}
