package com.toy.toy_board.api.open;

import com.toy.toy_board.common.context.GatewayRequestHeaderUtils;
import com.toy.toy_board.common.dto.ApiResponseDto;
import com.toy.toy_board.domian.dto.board.BoardDetailDto;
import com.toy.toy_board.domian.dto.board.BoardOverViewDto;
import com.toy.toy_board.service.board.BoardInfoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/boards/info", produces = MediaType.APPLICATION_JSON_VALUE)
public class BoardInfoController {
    private final BoardInfoService boardInfoService;

    @GetMapping
    public ApiResponseDto<Page<BoardOverViewDto>> boardOverView(@RequestParam(defaultValue = "1") int page,
                                                            @RequestParam(defaultValue = "10") int size){
        return ApiResponseDto.readOk(boardInfoService.boardOverView(page, size));
    }

    @GetMapping(value = "/{boardId}")
    public ApiResponseDto<BoardDetailDto> boardDetail(@PathVariable Long boardId, HttpServletRequest request, HttpServletResponse response){
        String userId = GatewayRequestHeaderUtils.getUserId();
        String userNickName = GatewayRequestHeaderUtils.getUserNickName();
        return ApiResponseDto.readOk(boardInfoService.getBoardDetail(boardId, userId, userNickName, request, response));
    }
}
