package com.example.board.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.board.dto.BoardDto;
import com.example.board.mapper.BoardMapper;

@Service
public class BoardServicelmpl implements BoardService{

	@Autowired
	private BoardMapper boardMapper;
	
	@Override
	public List<BoardDto> selectBoardList(Integer page, Integer pageSize) throws Exception {
		//return boardMapper.selectBoardList();
		
		  //pageNumber = 2; // 보여줄 페이지 번호
		  //pageSize = 2; // 한 페이지에 보여줄 개수
		
			List<BoardDto> list = boardMapper.selectBoardList(page,pageSize);

			List<BoardDto> finalList = new ArrayList<>();
			// 부모리스트
			for (BoardDto dto : list) {
				int bno = dto.getBno(); // re_level=1 // order by bno desc
				int ref = dto.getRef();
				finalList.add(dto);

				List<BoardDto> replList = boardMapper.selectBoardReplyList(bno, ref);
				if (replList != null && replList.size() > 0) {
					finalList.addAll(replList);
				}
			}
			return finalList;
		 
	}
	
	public List<BoardDto> selectSearchList(int page, int pageSize, String type, String keyword) throws Exception {
		return boardMapper.selectSearchList(page,pageSize,type,keyword);
	}

	public int insertBoard(BoardDto board) throws Exception {
		return boardMapper.insertBoard(board);
		
	}

	public int readCount(Integer bno) throws Exception {
		return boardMapper.readCount(bno);
	}

	public BoardDto selectBoardOne(Integer bno) throws Exception {
		return boardMapper.selectBoardOne(bno);
	}

	public int updateBoard(BoardDto board) throws Exception {
		return boardMapper.updateBoard(board);
		
	}

	public int deleteBoard(Integer bno) throws Exception {
		return boardMapper.deleteBoard(bno);
		
	}

	public int replyUpdate(BoardDto board) throws Exception {
		return boardMapper.replyUpdate(board);
		
	}

	public int replyInsert(BoardDto board) throws Exception {
		board.setRe_step(0);
		board.setRe_level(0);
		return boardMapper.replyInsert(board);
		
	}

	public int getTotalCnt(String type, String keyword) throws Exception {
		return boardMapper.getTotalCnt(type,keyword);
	}

	
}
