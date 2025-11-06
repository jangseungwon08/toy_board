package com.toy.toy_board.domian.entity;


import jakarta.persistence.*;
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
@SQLDelete(sql = "UPDATE likes SET is_deleted = true WHERE id = ?")
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Board.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", referencedColumnName = "id", nullable = false)
    private Board board_like;

    @Column(name = "like_writer_id", nullable = false)
    private String likeWriterId;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

}
