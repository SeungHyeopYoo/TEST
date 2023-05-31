package egovframework.kf.data;

import java.util.HashMap;
import java.util.List;

/**
 * Class Name : ResultListVO.java
 * Description : 검색결과 LIST 반환 VO
 * Modification Information
 *
 * 수정일                        수정자           수정내용
 * --------------------  -----------  ---------------------------------------
 * 2017년 12월  00일                       최초 작성
 *
 * @since 2017년
 * @version V1.0
 * @see (c) Copyright (C) by KONANTECH All right reserved
 */
public class ResultListVO{
	// 전체 결과 수
    private int total;
    
    // 현재 가져온 결과 수
    private int rows;
    
    // 검색 필드(컬럼) 개수
    private int cols;

    // 정확도 및 그룹카운트 스코어
    private int[] scores;

    // 엔진 로우아이디 번호
    private int[] rowIds;
    
    // 검색결과 레코드
    private List<HashMap<String, String>> list;
        
    private int groupCount;
    
	private int[] groupSize;
    
    private String[][] groupResult;
    
    private String[] colNames;
	
    public String[] result() {
		return colNames;
	}
    
	public String[] getColNames() {
		return colNames;
	}

	public void setColNames(String[] colNames) {
		this.colNames = colNames;
	}

	public int getCols() {
		return cols;
	}
	
	public void setCols(int cols) {
		this.cols = cols;
	}
	
	public List<HashMap<String, String>> getList() {
		return list;
	}

	public void setList(List<HashMap<String, String>> list) {
		this.list = list;
	}

	public int[] getRowIds() {
		return rowIds;
	}

	public void setRowIds(int[] rowIds) {
		this.rowIds = rowIds;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int[] getScores() {
		return scores;
	}

	public void setScores(int[] scores) {
		this.scores = scores;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	
    public int getGroupCount() {
		return groupCount;
	}

	public void setGroupCount(int groupCount) {
		this.groupCount = groupCount;
	}

	public int[] getGroupSize() {
		return groupSize;
	}

	public void setGroupSize(int[] groupSize) {
		this.groupSize = groupSize;
	}

	public String[][] getGroupResult() {
		return groupResult;
	}

	public void setGroupResult(String[][] groupResult) {
		this.groupResult = groupResult;
	}

	
}
