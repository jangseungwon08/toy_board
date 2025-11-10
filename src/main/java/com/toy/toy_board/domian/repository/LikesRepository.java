package com.toy.toy_board.domian.repository;

import com.toy.toy_board.domian.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {

    @Query(value = "SELECT COUNT(board_id) FROM likes WHERE board_id = :board_id", nativeQuery = true)
    Long likesCount(@Param("board_id") Long boardId);

    @Query(value = "SELECT * FROM likes WHERE board_id = :board_id AND likes_writer_id = :likes_writer_id", nativeQuery = true)
    Optional<Likes> findLikesStatus(@Param("board_id") Long board_id, @Param("likes_writer_id") String likesWriterId);
}
