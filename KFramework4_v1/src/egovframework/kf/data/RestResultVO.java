package egovframework.kf.data;
/**
 * Class Name : RestResultVO.java
 * Description : 검색결과 반환 VO
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
import java.util.Map;
import java.util.List;

public class RestResultVO {
	private String status;
	private long total;
	private int rows;
	private String previousQuery;
	
	private List<Map<String, String>> result;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public List<Map<String, String>> getResult() {
		return result;
	}

	public void setResult(List<Map<String, String>> result) {
		this.result = result;
	}

	public String getPreviousQuery() {
		return previousQuery;
	}

	public void setPreviousQuery(String previousQuery) {
		this.previousQuery = previousQuery;
	}
}
