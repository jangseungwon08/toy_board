package com.toy.toy_board.domian.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLDelete;

import java.util.List;

@Slf4j
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "board")
//1. JPA가 DB 조회를 할 때 기본 생성자를 생성 한 뒤에 DB에서 가져온 데이터를 각 필드에 채워넣음
//2. 지연 로딩을 사용할 때 JPA는 실제 엔티티 객체 대신 껍데기 프록시 객체를 생성한다. 이 프록시 객체는 원본 엔티티를 상속 받아서 만들어짐
@Getter
@SQLDelete(sql = "UPDATE board SET is_deleted = true WHERE id = ?")
public class Board extends BaseTimeEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "board_title",nullable = false, length = 100)
    private String boardTitle;

    @Column(name = "board_body", nullable = false)
    private String boardBody;

    @Column(name = "board_writer_id", nullable = false)
    private String boardWriterId;

    @Column(name = "board_writer_nickName", nullable = false)
    private String boardWriterNickName;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @Column(name ="view_count", nullable = false)
    private Long viewCount;

    @Column(name = "imgUrl", nullable = true)
    private String imgUrl;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "board_comment", cascade = CascadeType.ALL)
    private List<Comment> comment;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "board_like", cascade = CascadeType.ALL)
    private List<Likes> likes;

    @Builder
    public Board(String boardTitle, String boardBody, String boardWriterId, String boardWriterNickName, boolean isDeleted, String imgUrl){
        this.boardTitle = boardTitle;
        this.boardBody = boardBody;
        this.boardWriterId = boardWriterId;
        this.boardWriterNickName = boardWriterNickName;
        this.viewCount = 0L;
        this.isDeleted = isDeleted;
        this.imgUrl = imgUrl;
    }

    public void updateBoardTitle(String boardTitle){
        this.boardTitle = boardTitle;
    }
    public void updateBoardBody(String boardBody){
        this.boardBody = boardBody;
    }

    public void increaseViewCount(){
        this.viewCount += 1;
    }

    public void changeWriterNickName(String boardWriterNickName) {this.boardWriterNickName = boardWriterNickName;}
}
