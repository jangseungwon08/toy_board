package com.toy.toy_board.service;

import com.toy.toy_board.common.aws.S3Builder;
import com.toy.toy_board.common.aws.S3Service;
import com.toy.toy_board.domian.repository.BoardRepository;
import com.toy.toy_board.domian.repository.LikesRepository;
import com.toy.toy_board.service.likes.LikesService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
@SpringBootTest
@DisplayName("라이크 서비스로")
public class LikesServiceTest {
    @Autowired
    LikesRepository likesRepository;
    @Autowired
    LikesService likesService;
    @MockitoBean
    S3Service s3Service;
    @MockitoBean
    S3Builder s3Builder;

    @Test
    @DisplayName("좋아요를 누를 수 있다.")
    public void LikesTest(){
//        given
        Long boardId = 1L;
        String userId = "dong1234";

//        when
        Long res  = likesService.likes(boardId,userId);

//        then
        Long count = likesRepository.likesCount(boardId);
        assertThat(count).isEqualTo(1L);
        System.out.println(count);
    }
    @Test
    @DisplayName("좋아요를 취소할 수 있다.")
    public void disLikeTest(){
        //        given
        Long boardId = 1L;
        String userId = "dong1234";

//        when
        Long res2 = likesService.likes(boardId,userId);
//        then
        assertThat(res2).isEqualTo(1L);

    }
}
