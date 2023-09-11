package com.example.board.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.board.dto.BoardDto;
import com.example.board.dto.Paging;
import com.example.board.service.BoardService;


@Controller
public class BoardController {

	@Autowired
	private BoardService boardService;
	
	//private final int DEFAULT_PAGE_SIZE = 10;
	
	//게시판 목록(메인)
	@GetMapping("/list")
	public String main(@RequestParam(defaultValue = "1") int page, Model model,
			@RequestParam(value="type", required=false) String type, 
			@RequestParam(value="keyword", required=false) String keyword) throws Exception {
		
		
		/*
		 * String pageSizeParam = req.getParameter("s"); int pageSize =
		 * DEFAULT_PAGE_SIZE; if (pageSizeParam != null) { pageSize =
		 * Integer.parseInt(pageSizeParam); }
		 */

		
		//전체 게시물 수
		int totalListCnt = boardService.getTotalCnt(type,keyword);
		
		//전체 게시물 수, 현재 페이지 전달
		Paging paging = new Paging(totalListCnt,page);
	
		//한 페이지 당 보여지는 게시글의 최대 개수
		int pageSize = paging.getPageSize();
		
		List<BoardDto> list = null;
		
		//검색 type과 keyword가 없으면 기존 화면을 보여주기
		if (type == null && keyword == null) {
			
			list = boardService.selectBoardList(page,pageSize);
			
        } else { //검색 type과 keyword가 들어오면 검색 keyword에 맞게 리스트 출력
            
        	list = boardService.selectSearchList(page,pageSize,type,keyword);
        }
		//list = boardService.selectBoardList(page,pageSize,type,keyword);
		
		model.addAttribute("paging", paging);
		model.addAttribute("list", list);
		return "list";
	}
	 
	//글 작성
	@GetMapping("/write")
	public String writeform(Model model) throws Exception {
		model.addAttribute("board", new BoardDto());	
		return "writeForm";
	}
	
	@PostMapping("/write")
	public String write(@ModelAttribute BoardDto board) throws Exception {
		boardService.insertBoard(board);
		return "redirect:/list";
	}
	
	//글 상세보기
	@GetMapping("/detail/{bno}")
	public String detail(@PathVariable("bno")Integer bno, Model model) throws Exception {
		boardService.readCount(bno);
		BoardDto boardDto = boardService.selectBoardOne(bno);
		
		model.addAttribute("board", boardDto);
		return "detailForm";
	}
	
	//글 수정
	@GetMapping("/update/{bno}")
	public String modify(@PathVariable("bno")Integer bno, Model model) throws Exception {
		//BoardDto boardDto = boardService.selectBoardOne(bno);
		
		model.addAttribute("board", boardService.selectBoardOne(bno));
		return "updateForm";
	}
	
    @ResponseBody
	@PostMapping("/update/{bno}")
	public Map<String, String> update(@PathVariable("bno")Integer bno, 
			@RequestBody Map<String, String> bodyMap) throws Exception {
		
    	//비밀번호 일치 시 수정, 일치하지 않으면 수정x, 다시 입력
		Map<String, String> result = new HashMap<>();
		BoardDto board = boardService.selectBoardOne(bno);
		String original_pw = board.getPw();
		
		String pw = "";
		if(bodyMap!=null) {
			pw = bodyMap.get("pw");
			
			board.setTitle( bodyMap.get("title_new") ); 
			board.setContent( bodyMap.get("content_new") );
			board.setWriter( bodyMap.get("writer_new") );
			
		}
		
		int count = 0;
		if(original_pw.equals(pw)) { //기존 비번과 일치하는지 비교
			count = boardService.updateBoard(board);
			if(count>0) {
				result.put("RESULT", "SUCCESS"); //수정 성공
				result.put("MSG", "성공적으로 수정이 완료되었습니다.");
			}else {
				result.put("RESULT", "FAIL");
				result.put("MSG", "수정 실패");
			}
		}else {
			result.put("RESULT", "FAIL");
			result.put("MSG", "비밀번호가 일치하지 않습니다.");
		}
		
        //return "redirect:/detail/"+bno;
		return result;
	}
	
	//글 삭제
	@GetMapping("/delete/{bno}")
	public String deleteForm(@PathVariable(value="bno", required=false)Integer bno, Model model, BoardDto board) throws Exception {
		
		model.addAttribute("board", board);
		model.addAttribute("bno", bno);
		return "deleteForm";
	}
	
	@ResponseBody
	@PostMapping("/delete/{bno}")
	public Map<String, String> delete(@PathVariable(value="bno", required=false)Integer bno, 
							@RequestBody Map<String, String> bodyMap //@RequestParam("pw") String pw
			            ) throws Exception {
		
		Map<String, String> result = new HashMap<>();
		BoardDto board = boardService.selectBoardOne(bno);
		String original_pw = board.getPw();
		
		String pw = "";
		if(bodyMap!=null) {
			pw = bodyMap.get("pw");
		}
		
		int count = 0;
		if(original_pw.equals(pw)) {
			count = boardService.deleteBoard(bno);
			if(count>0) {
				result.put("RESULT", "SUCCESS");
				result.put("MSG", "성공적으로 삭제가 완료되었습니다.");
			}else {
				result.put("RESULT", "FAIL");
				result.put("MSG", "삭제 실패");
			}
		}else {
			result.put("RESULT", "FAIL");
			result.put("MSG", "비밀번호가 일치하지 않습니다.");
		}
		
		//return "redirect:/list";
		return result;
	}

	//답글 쓰기
	@GetMapping("/reply/{bno}")
	public String replyForm(@RequestParam(value="bno", required=false) Integer bno,
			              @RequestParam(value="ref", required=false) Integer ref, 
			              @RequestParam(value="re_step", required=false) Integer re_step,
			              @RequestParam(value="re_level", required=false) Integer re_level, Model model, BoardDto board) throws Exception{
		
		model.addAttribute("ref", ref);
		model.addAttribute("re_step", re_step);
		model.addAttribute("re_level", re_level);
		model.addAttribute("board", board);
		
		return "replyForm";
	}
	
	@PostMapping("/reply/{bno}")
	public String reply(BoardDto board) throws Exception{
		
		board.setRef(board.getBno()); //답글 ref에 부모글의 번호가 들어오도록 설정
		boardService.replyUpdate(board);
		boardService.replyInsert(board);
		
		return "redirect:/list";
	}

}
