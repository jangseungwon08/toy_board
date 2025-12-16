package com.toy.toy_board.api.open;


import com.toy.toy_board.common.context.GatewayRequestHeaderUtils;
import com.toy.toy_board.common.dto.ApiResponseDto;
import com.toy.toy_board.domian.dto.comment.CreateCommentDto;
import com.toy.toy_board.domian.dto.comment.CreateReplyDto;
import com.toy.toy_board.domian.dto.comment.UpdateCommentDto;
import com.toy.toy_board.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/boards/comments", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ApiResponseDto<Long> createComments(@RequestBody CreateCommentDto createCommentDto){
        String userId = GatewayRequestHeaderUtils.getUserIdOrThrowException();
        String userNickName = GatewayRequestHeaderUtils.getUserNickNameOrThrowException();
        return ApiResponseDto.createOk(commentService.createComment(createCommentDto, userId, userNickName));
    }

    @PatchMapping(value = "/{commentId}")
    public ApiResponseDto<Long> updateComments(@PathVariable Long commentId, @RequestBody UpdateCommentDto updateCommentDto){
        String userId = GatewayRequestHeaderUtils.getUserIdOrThrowException();
        return ApiResponseDto.createOk(commentService.updateComment(updateCommentDto,commentId, userId));
    }

    @DeleteMapping(value = "/{commentId}")
    public ApiResponseDto<Long> deleteComments(@PathVariable Long commentId){
        String userId = GatewayRequestHeaderUtils.getUserIdOrThrowException();
        return ApiResponseDto.deleteOk(commentService.deleteComment(commentId, userId));
    }

    @PostMapping(value = "/reply")
    public ApiResponseDto<Long> createReply(@RequestBody CreateReplyDto createReplyDto){
        String userId = GatewayRequestHeaderUtils.getUserIdOrThrowException();
        String userNickName = GatewayRequestHeaderUtils.getUserNickNameOrThrowException();
        return ApiResponseDto.createOk(commentService.createReplyComment(createReplyDto, userId, userNickName));
    }
}
