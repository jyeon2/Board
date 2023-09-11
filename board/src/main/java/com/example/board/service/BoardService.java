package com.example.board.service;

import java.util.List;

import com.example.board.dto.BoardDto;

public interface BoardService {

	List<BoardDto> selectBoardList(Integer page, Integer pageSize) throws Exception;

	List<BoardDto> selectSearchList(int page, int pageSize, String type, String keyword) throws Exception;

	int insertBoard(BoardDto board) throws Exception;

	int readCount(Integer bno) throws Exception;

	BoardDto selectBoardOne(Integer bno) throws Exception;

	int updateBoard(BoardDto board) throws Exception;

	int deleteBoard(Integer bno) throws Exception;

	int replyUpdate(BoardDto board) throws Exception;

	int replyInsert(BoardDto board) throws Exception;

	int getTotalCnt(String type, String keyword) throws Exception;


}
