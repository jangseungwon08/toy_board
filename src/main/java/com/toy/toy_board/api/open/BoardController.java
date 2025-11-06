package com.toy.toy_board.api.open;


import com.toy.toy_board.common.context.GatewayRequestHeaderUtils;
import com.toy.toy_board.common.dto.ApiResponseDto;
import com.toy.toy_board.domian.dto.board.CreateBoardDto;
import com.toy.toy_board.domian.dto.board.EditBoardDto;
import com.toy.toy_board.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/boards", produces = MediaType.APPLICATION_JSON_VALUE)
public class BoardController {
    private final BoardService boardService;

    @PostMapping
    public ApiResponseDto<Long> createBoards(@RequestPart(value = "createBoardDto") @Validated CreateBoardDto createBoardDto,
                                             @RequestPart(value = "img", required = false)MultipartFile multipartFile) {
        String userId = GatewayRequestHeaderUtils.getUserIdOrThrowException();
        String nickName = GatewayRequestHeaderUtils.getUserNickNameOrThrowException();
        return ApiResponseDto.createOk(boardService.createBoard(createBoardDto, userId, nickName, multipartFile));
    }

    @PatchMapping(value = "/{boardId}")
    public ApiResponseDto<Long> editBoards(
            @PathVariable Long boardId,
            @RequestPart(value = "editBoardDto") @Validated EditBoardDto editBoardDto,
            @RequestPart(value = "img", required = false)MultipartFile multipartFile){
        String userId = GatewayRequestHeaderUtils.getUserIdOrThrowException();
        return ApiResponseDto.createOk(boardService.editBoard(editBoardDto, userId, boardId));
    }

    @DeleteMapping(value = "/{boardId}")
    public ApiResponseDto<Long> deleteBoards(@PathVariable Long boardId){
        String userId = GatewayRequestHeaderUtils.getUserIdOrThrowException();
        return ApiResponseDto.createOk(boardService.deleteBoard(boardId, userId));
    }

}
