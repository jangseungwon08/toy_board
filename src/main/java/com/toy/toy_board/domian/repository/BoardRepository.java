package com.toy.toy_board.domian.repository;


import com.toy.toy_board.domian.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query(value = "SELECT * FROM board WHERE is_deleted= false",
            countQuery = "SELECT count(*) FROM board WHERE is_deleted = false",
            nativeQuery = true)
    Page<Board> findAllOverViewBoard(Pageable pageable);

}
