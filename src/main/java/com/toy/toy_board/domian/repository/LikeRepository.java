package com.toy.toy_board.domian.repository;

import com.toy.toy_board.domian.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Likes, Long> {

    @Query(value = "SELECT COUNT(board_id) FROM likes WHERE board_id = :board_id", nativeQuery = true)
    int likeCount(@Param("board_id") Long board_id);
}
