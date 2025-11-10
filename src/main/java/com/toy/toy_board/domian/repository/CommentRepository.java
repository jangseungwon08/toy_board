package com.toy.toy_board.domian.repository;

import com.toy.toy_board.domian.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "SELECT COALESCE(max(group_id),0) +1 FROM comment WHERE board_id = :board_id", nativeQuery = true)
    Long findMaxGroupId(@Param(value = "board_id") Long boardId);

//    그룹 내 최대 sequence찾기
    @Query(value = "SELECT COALESCE(max(sequence), 0) +1 FROM comment where group_id = : group_id", nativeQuery = true)
    Long findMaxSequence(@Param(value = "group_Id") Long groupId);

    @Query(value = "SELECT * FROM comment WHERE board_id = :board_id ORDER BY group_id, sequence", nativeQuery = true)
    List<Comment> findAllComment(@Param(value = "board_id") Long boardId);

//    부모와 같은 깊이 sequence 중 부모보다는 큰 바로 다음 sequence 가져온다.
    @Query(value = "SELECT min(sequence) FROM comment c WHERE c.group_id = :group_id and c.depth = :parent_depth and c.sequence > :parent_sequence", nativeQuery = true)
    Long findNextSiblingSequence(@Param(value = "group_id")Long group_id, @Param(value = "parent_depth") Long parent_depth, @Param(value = "parent_sequence") Long parent_sequence);

//    업데이트 딜리트 쿼리는 이 어노테이션이 필수
    @Modifying
    @Query(value = "UPDATE comment SET sequence = sequence +1 WHERE group_id = :group_id AND sequence >= :insert_point", nativeQuery = true)
    void increaseSequence(@Param(value = "group_id") Long groupId, @Param(value = "insert_point") Long insertPoint);


}
