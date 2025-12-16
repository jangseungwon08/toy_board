package com.toy.toy_board.domian.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLDelete;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "comment")
@Entity
@SQLDelete(sql = "UPDATE comment SET is_deleted = true WHERE id = ?")
public class Comment extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comment_writer_id", nullable = false)
    @Setter
    private String commentWriterId;

    @Column(name = "comment_writer_nick_name",nullable = false)
    private String commentWriterNickName;

    @Column(name = "comment_body", nullable = false)
    @Setter
    private String commentBody;

    @Column(name = "is_deleted", nullable = false)
    @Setter
    private boolean isDeleted = false;

    @ManyToOne(targetEntity = Board.class, fetch = FetchType.LAZY)
//    referencedColumnName => 대상 테이블의 칼럼 명
    @JoinColumn(name = "board_id", referencedColumnName = "id", nullable = false)
    private Board board_comment;

//    대댓글 계층 구조를 위한 속성
//    대댓글을 그룹화 하기 위해 사용
//    ex) 댓글 1-1 , 댓글 1-2, 댓글 1-1-1
    @Column(name = "group_id")
    @Setter
    private Long groupId;

    @Setter
    @Column(name = "sequence")
    private Long sequence;

    @Column(name = "depth")
    @Setter
    private Long depth;

    @Column(name = "child_count")
    @Setter
    private Long childCount;

    @ManyToOne(targetEntity = Comment.class, fetch = FetchType.LAZY)
//    첫번째 댓글은 부모를 가져야되는건 아니니까 nullable true로
    @JoinColumn(name = "parent_id", referencedColumnName = "id", nullable = true)
    @Setter
    private Comment parent;

/*
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> children;
*/

}
