package egovframework.kf.data;

/**
 * Class Name : SearchVO.java
 * Description : 검색엔진 통해 검색하기위한 파라미터 VO
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
public class SearchVO {
	private String url;
	private String fields;
	private String from;
	private String charset;	
		
	private String query;
	private String logInfo;
	private String hilightTxt;
	private String orderBy;

	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getFields() {
		return fields;
	}
	public void setFields(String fields) {
		this.fields = fields;
	}	
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getLogInfo() {
		return logInfo;
	}
	public void setLogInfo(String logInfo) {
		this.logInfo = logInfo;
	}
	public String getHilightTxt() {
		return hilightTxt;
	}
	public void setHilightTxt(String hilightTxt) {
		this.hilightTxt = hilightTxt;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
}
