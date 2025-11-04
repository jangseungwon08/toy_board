package com.toy.toy_board.service;

import com.toy.toy_board.common.aws.S3Service;
import com.toy.toy_board.common.exception.BadParameter;
import com.toy.toy_board.common.exception.NotFound;
import com.toy.toy_board.domian.dto.CreateBoardDto;
import com.toy.toy_board.domian.dto.EditBoardDto;
import com.toy.toy_board.domian.entity.Board;
import com.toy.toy_board.domian.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final S3Service s3Service;

    @Transactional
    public Long createBoard(CreateBoardDto createBoardDto, String userId, String nickName, MultipartFile multipartFile){
        String imgUrl = "";
        try{
            imgUrl = s3Service.uploadFile(multipartFile, "");
        } catch(IOException e){
            log.error("S3 파일 업로드 중 심각한 에러 발생", e); // 에러 메시지도 구체적으로
            throw new RuntimeException("파일 업로드에 실패했습니다.", e);
        }
        Board board = createBoardDto
                .toEntity(createBoardDto.getBoardTitle(), createBoardDto.getBoardBody(), userId, nickName, imgUrl);
        try{
            boardRepository.save(board);
        }
        catch (Exception e){
            log.error("게시글 저장에 실패했습니다. userId: {}, nickName: {}", userId, nickName, e);
            throw new RuntimeException("게시글 저장에 실패했습니다.");
        }
        return board.getId();
    }

    @Transactional
    public Long editBoard(EditBoardDto editBoardDto, String userId, Long boardId){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFound("존재하지 않는 게시글입니다."));
        if (!board.getBoardWriterId().equals(userId)){
            throw new BadParameter("작성자만 수정할 수 있습니다.");
        }
        if(editBoardDto.getBoardTitle() != null){
            board.setBoardTitle(editBoardDto.getBoardTitle());
        }
        if(editBoardDto.getBoardBody() != null){
            board.setBoardBody(editBoardDto.getBoardBody());
        }
        return board.getId();
    }

    @Transactional
    public Long deleteBoard(Long boardId, String userId){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFound("존재하지 않는 게시글입니다."));
        if(!board.getBoardWriterId().equals(userId)){
            throw new BadParameter("작성자만 삭제할 수 있습니다.");
        }
        boardRepository.delete(board);
        return board.getId();
    }
}
