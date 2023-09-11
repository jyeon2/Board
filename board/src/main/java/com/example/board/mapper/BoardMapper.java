package com.example.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.board.dto.BoardDto;

@Mapper
public interface BoardMapper {

	//List<BoardDto> selectBoardList(Paging paging) throws Exception;
	List<BoardDto> selectBoardList(Integer page, Integer pageSize) throws Exception;

	List<BoardDto> selectSearchList(Integer page, Integer pageSize, String type, String keyword) throws Exception;

	int insertBoard(BoardDto board) throws Exception;

	int readCount(Integer bno) throws Exception;

	BoardDto selectBoardOne(Integer bno) throws Exception;

	int updateBoard(BoardDto board) throws Exception;

	int deleteBoard(Integer bno) throws Exception;

	int replyUpdate(BoardDto board) throws Exception;

	int replyInsert(BoardDto board) throws Exception;

	List<BoardDto> selectBoardReplyList(Integer bno, Integer ref) throws Exception;

	int getTotalCnt(String type, String keyword) throws Exception;


}
