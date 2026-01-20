package com.toy.toy_board.kafka.producer.notification.event;


import com.toy.toy_board.domian.entity.Board;
import com.toy.toy_board.domian.entity.Comment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class CommentPosted {
    public static final String TOPIC = "comment-posted";
    private Long boardId;
    private String boardTitle;
    private String receiverId;
    private String commentWriterNickName;
    private String commentBody;
    private Long parentCommentId;
    private LocalDateTime createdAt;
    private Long commentId;

    public static CommentPosted fromEntityComment(Board board, Comment comment){
        CommentPosted event = new CommentPosted();
        event.setBoardId(board.getId());
        event.setBoardTitle(board.getBoardTitle());
        event.setReceiverId(board.getBoardWriterId());
        event.setCommentWriterNickName(comment.getCommentWriterNickName());
        event.setCommentBody(comment.getCommentBody());
        event.setCreatedAt(comment.getCreatedAt());
        event.setParentCommentId(null);
        event.setCommentId(comment.getId());
        return event;
    }
    public static CommentPosted fromEntityCommentReply(Board board, Comment reply, Comment parent){
        CommentPosted event = new CommentPosted();
        event.setBoardId(board.getId());
        event.setBoardTitle(board.getBoardTitle());
        event.setReceiverId(parent.getCommentWriterId());
        event.setCommentWriterNickName(reply.getCommentWriterNickName());
        event.setCommentBody(reply.getCommentBody());
        event.setParentCommentId(reply.getParent().getId());
        event.setCommentId(reply.getId());
        event.setCreatedAt(reply.getCreatedAt());
        return event;
    }
}
