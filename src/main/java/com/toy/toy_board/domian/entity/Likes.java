package com.toy.toy_board.domian.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLDelete;

@Slf4j
@NoArgsConstructor
@Setter
@Getter
@Table(name = "likes")
@Entity
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Board.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", referencedColumnName = "id", nullable = false)
    private Board board_like;

    @Column(name = "likes_writer_id", nullable = false)
    private String likesWriterId;

    @Builder
    Likes (Board board, String likesWriterId){
        this.board_like = board;
        this.likesWriterId = likesWriterId;
    }
}
