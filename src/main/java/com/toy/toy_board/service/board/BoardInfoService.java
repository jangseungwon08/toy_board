package com.toy.toy_board.service.board;

import com.toy.toy_board.common.exception.BadParameter;
import com.toy.toy_board.common.exception.NotFound;
import com.toy.toy_board.domian.dto.board.BoardDetailDto;
import com.toy.toy_board.domian.dto.board.BoardOverViewDto;
import com.toy.toy_board.domian.entity.Board;
import com.toy.toy_board.domian.repository.BoardRepository;
import com.toy.toy_board.domian.repository.LikesRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardInfoService {
    private final BoardRepository boardRepository;
    private final LikesRepository likesRepository;

    @Transactional
    public BoardDetailDto getBoardDetail(Long boardId, String userId, String userNickName, HttpServletRequest request, HttpServletResponse response){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFound("존재하지 않는 게시글입니다."));
        if(board.isDeleted()) {
            throw new BadParameter("이미 삭제된 게시글입니다.");
        }
        addViewCount(request, response, boardId);
        Long likesCount = likesRepository.likesCount(board.getId());

        return BoardDetailDto.builder()
                .boardTitle(board.getBoardTitle())
                .boardBody(board.getBoardBody())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .boardWriteId(board.getBoardWriterId())
                .boardWriterNickName(board.getBoardWriterNickName())
                .isOwned(board.getBoardWriterId().equals(userId))
                .viewCount(board.getViewCount())
                .likesCount(likesCount)
                .imgUrl(board.getImgUrl())
                .build();
    }

    @Transactional
    public void addViewCount(HttpServletRequest request, HttpServletResponse response, Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFound("존재하지 않는 게시글입니다."));
        Cookie oldCookie = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("postView")) {
                    oldCookie = cookie;
                }
            }
        }
        if (oldCookie != null) {
            if (!oldCookie.getValue().contains("[" + boardId.toString() + "]")) {
                board.increaseViewCount();
                oldCookie.setValue(oldCookie.getValue() + "_[" + boardId + "]");
                oldCookie.setPath("/");
                response.addCookie(oldCookie);
            }
        } else {
            board.increaseViewCount();
            Cookie newCookie = new Cookie("postView", "[" + boardId + "]");
            newCookie.setPath("/");
            response.addCookie(newCookie);
        }
    }

    @Transactional(readOnly = true)
    public Page<BoardOverViewDto> boardOverView(Pageable pageable){
        Page<Board> boards = boardRepository.findAllOverViewBoard(pageable);
        return boards.map(
                BoardOverViewDto::fromEntity
        );
    }
}
