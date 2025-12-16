package com.toy.toy_board.api.open;

import com.toy.toy_board.common.context.GatewayRequestHeaderUtils;
import com.toy.toy_board.common.dto.ApiResponseDto;
import com.toy.toy_board.service.likes.LikesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/boards/info", produces = MediaType.APPLICATION_JSON_VALUE)
public class LikesController {
    private final LikesService likesService;

    @PostMapping(value = "/{boardId}/likes")
    public ApiResponseDto<Long> likes(@PathVariable Long boardId){
        String userId = GatewayRequestHeaderUtils.getUserIdOrThrowException();
        return ApiResponseDto.createOk(likesService.likes(boardId, userId));
    }
}