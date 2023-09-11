package com.example.board.dto;

public class Paging {

	//페이징 관련 변수
	private int pageSize = 3; //한 페이지 당 보여지는 게시글의 최대 개수
	private int blockSize = 5; //페이징된 버튼의 블럭당 최대 개수
	private int page = 1; //현재 페이지
	private int totalPageCnt; //전체 페이지 개수
	private int totalListCnt; //전체 게시물 개수
	private int block = 1; //현재 블럭
	private int startPage = 1; 	//블럭 시작 페이지
	private int endPage = 1; //블럭 마지막 페이지

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getTotalPageCnt() {
		return totalPageCnt;
	}

	public void setTotalPageCnt(int totalPageCnt) {
		this.totalPageCnt = totalPageCnt;
	}

	public int getTotalListCnt() {
		return totalListCnt;
	}

	public void setTotalListCnt(int totalListCnt) {
		this.totalListCnt = totalListCnt;
	}

	public int getBlockSize() {
		return blockSize;
	}

	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

	public int getBlock() {
		return block;
	}

	public void setBlock(int block) {
		this.block = block;
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	
	public Paging(int totalListCnt, int page) {
		
		//현재 페이지
		setPage(page);
        
		//전체 게시글 수
		setTotalListCnt(totalListCnt);

		//전체 페이지 수
		//한 페이지의 최대 개수를 전체 게시물 수 * 1.0로 나누어주고 올림
		setTotalPageCnt((int) Math.ceil(totalListCnt * 1.0 / pageSize));
		
		//현재 블럭
		//현재 페이지 * 1.0을 블록의 최대 개수로 나누고 올림
		setBlock((int) Math.ceil((page * 1.0)/blockSize)); 

		//블럭 시작 페이지
		setStartPage((block - 1) * blockSize + 1);
		
		//블럭 마지막 페이지
		setEndPage(startPage + blockSize - 1);
	}
}
