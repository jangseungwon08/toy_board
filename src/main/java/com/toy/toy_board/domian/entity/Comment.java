package com.toy.toy_board.domian.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@Table(name = "comment")
@Entity
public class Comment extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comment_writer_id", nullable = false)
    private String commentWriterId;

    @Column(name = "comment_writer_nick_name",nullable = false)
    private String commentWriterNickName;

    @ManyToOne(targetEntity = Board.class, fetch = FetchType.LAZY)
//    referencedColumnName => 대상 테이블의 칼럼 명
    @JoinColumn(name = "board_id", referencedColumnName = "id", nullable = false)
    private Board board_comment;


//    자식 클래스
    @ManyToOne(targetEntity = Comment.class, fetch = FetchType.LAZY)
//    모든 댓글이 부모를 가져야되는건 아니니까 nullable true로
    @JoinColumn(name = "parent_id", referencedColumnName = "id", nullable = true)
    private Comment parent;

// 부모 클래스
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> children;

}
