package com.toy.toy_board.api.open.boardController;

import com.toy.toy_board.common.context.GatewayRequestHeaderUtils;
import com.toy.toy_board.common.dto.ApiResponseDto;
import com.toy.toy_board.domian.dto.BoardDetailDto;
import com.toy.toy_board.service.BoardInfoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/boards/info", produces = MediaType.APPLICATION_JSON_VALUE)
public class BoardInfoController {
    private final BoardInfoService boardInfoService;

    @GetMapping(value = "/{boardId}")
    public ApiResponseDto<BoardDetailDto> boardDetail(@PathVariable Long boardId, HttpServletRequest request, HttpServletResponse response){
        String userId = GatewayRequestHeaderUtils.getUserId();
        String userNickName = GatewayRequestHeaderUtils.getUserNickName();
        return ApiResponseDto.createOk(boardInfoService.getBoardDetail(boardId, userId, userNickName, request, response));
    }
}
