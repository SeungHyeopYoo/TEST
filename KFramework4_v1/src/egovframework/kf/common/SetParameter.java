package egovframework.kf.common;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.kf.data.ParameterVO;
import egovframework.rte.fdl.property.EgovPropertyService;


/**
 * Class Name : SetParameter.java
 * Description : 통합검색 조회를 파라미터 설정
 *
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
@Component("setParameter")
public class SetParameter {
	
	/** Common Util */
	@Resource(name = "commonUtil")
	private CommonUtil commonUtil;
	
	/** EgovPropertyService */
	@Resource(name = "konanPropertiesService")
	protected EgovPropertyService konanPropertiesService;

		
	public ParameterVO setParameter(@RequestParam Map<String, String> map){
		ParameterVO rtnParam = new ParameterVO();
		
		// 사이트명
		rtnParam.setSiteNm(konanPropertiesService.getString("site"));
		
		// 키워드 (기본적으로 콤마는 삭제한다.)
		try {
			rtnParam.setKwd(getValue(map, "kwd", "", true).replaceAll("\\'", ""));
		
		
		// 하이라이트 키워드를 세팅한다.
		rtnParam.setHilightKwd(commonUtil.extractHighlightKwd(rtnParam.getKwd()));
		
		// 카테고리
		rtnParam.setCategory(getValue(map, "category", "TOTAL", false));
			
		// 페이지 번호
		rtnParam.setPageNum(getValue(map, "pageNum", 1));
		
	
		// 페이지 사이즈 세팅
		if("TOTAL".equals(rtnParam.getCategory())) {
			rtnParam.setPageSize(getValue(map, "pageSize", konanPropertiesService.getInt("pageSizeTotal")));
		} else {
			rtnParam.setPageSize(getValue(map, "pageSize", konanPropertiesService.getInt("pageSize")));
		}		
		
		
		// offSet 번호
		rtnParam.setOffSet((rtnParam.getPageNum()-1)*rtnParam.getPageSize() );
		
		// 검색대상
		rtnParam.setSrchFd(getValue(map, "srchFd", "ALL", false));
		
		// 정렬코드
		rtnParam.setSort(getValue(map, "sort", "R", false));
		
		// 정렬명
		if("R".equals(rtnParam.getSort())) {
			rtnParam.setSortNm("정확도순");
		} else {
			rtnParam.setSortNm("최신순");
		}
		
		// 상세검색 여부
		rtnParam.setDetailSearch(getValue(map, "detailSearch"));
		
		// 파일 확장자
		rtnParam.setFileExt(getValue(map, "fileExt", "", false));
		
		// 시작일
		rtnParam.setStartDate(getValue(map, "startDate", "", false));
		
		// 종료일
		rtnParam.setEndDate(getValue(map, "endDate", "", false));
		
		// 년도
		rtnParam.setYear(getValue(map, "year", "", false));
		
		// 작성자
		rtnParam.setWriter(getValue(map, "writer", "", false));
		
		// 제외어
		rtnParam.setExclusiveKwd(getValue(map, "notWord", "", false));
		
		// 오늘날짜
		rtnParam.setNowDate(getValue(map, "nowDate", commonUtil.getTargetDate(0), false));
		
		// 검색영역
		rtnParam.setFields(getValue(map, "fields", "", false));
	
		// 재검색여부
		rtnParam.setReSrchFlag( "true".equals(getValue(map, "resrch", "", false))?true:false );
		
		// 이전 쿼리
		rtnParam.setPreviousQuery(getValue(map, "previousQuery", "", false));
		
		// 이전 쿼리
		rtnParam.setOriginalQuery(getValue(map, "originalQuery", "", false));	
		
		//map -> controll string -> jsp -> controll string -> map
	      // 이전 쿼리들
	      //rtnParam.setPreviousQueries( getValues(map,"previousQuery") );
	      if(map.get("previousQueries") != null) {
	         rtnParam.setPreviousQueries(jsonToMap(map.get("previousQueries").toString()));
	      }
	    
	    // 조건
	    rtnParam.setCondition(getValue(map, "condition", "", false));
		
		// 이전 쿼리들
		// rtnParam.setPreviousQueries( getValues(map,"previousQuery"));	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rtnParam;
	}
	
	
	/**
	 * 파라미터 값을 바인딩한다.
	 * @param map 파라미터
	 * @param key 파라미터 키
	 * @param rtnValue null 인 경우 바인딩 
	 * @param chk <script 체크여부
	 * @return
	 * @throws Exception
	 */
	public String getValue(Map<String, String> map, String key, String rtnValue, boolean chk) throws Exception {		
		if(StringUtils.isEmpty(map.get(key)))
			return rtnValue;
		
		
		if(!checkValue(map.get(key).toString(), chk)){
	
			throw new Exception("허용되지 않는 URL입니다.");
		}
		return map.get(key).toString();
	}
	
	
	/**
	 * 정수 리턴 메소드
	 * 
	 * @param map
	 * @param key
	 * @param rtnValue
	 * 
	 * @return integer
	 */
	public int getValue(Map<String, String> map, String key, int rtnValue) {
		if(StringUtils.isEmpty(map.get(key)) || !NumberUtils.isNumber(map.get(key))){
			return rtnValue;
		}
		
		return Integer.parseInt(map.get(key));
	}
	
	
	/**
	 * boolean 리턴 메소드
	 * 
	 * @param map
	 * @param key
	 * @param rtnValue
	 * 
	 * @return boolean
	 */
	public boolean getValue(Map<String, String> map, String key) {		
		if(StringUtils.isEmpty(map.get(key)))
			return false;
		
		if("true".equals(map.get(key)) || "on".equals(map.get(key)))
			return true;
		
		return false;
	}
	
	/**
	 * 문자열에 특수문자 및 이상여부를 체크하여 반환한다.
	 * @param value 문자열
	 * @param chk script, frame 체크
	 * @return
	 */
	public boolean checkValue(String value, boolean chk){		

		if(chk){
			if(  (value.toLowerCase()).indexOf("<script") > -1
					|| (value.toLowerCase()).indexOf("<frame") > -1
				    || (value.toLowerCase()).indexOf("<iframe") > -1 ) {
				return false;
			}
		}

		/*
		if(!value.matches("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝]*")) {
			return false;
		}
		*/
		
		return true;
	}
	
	public HashMap<String, String> jsonToMap(String result) throws IOException {
	       Properties props = new Properties();
	       props.load(new StringReader(result.substring(1, result.length() - 1).replace(", ", "\n")));
	       HashMap<String, String> map = new HashMap<>();
	       for (HashMap.Entry<Object, Object> e : props.entrySet()) {
	           map.put((String) e.getKey(), (String) e.getValue());
	           //System.out.println("key : " + (String) e.getKey());
	           //System.out.println("value : " + (String) e.getValue());
	       }
	       //System.out.println(map);
	       return map;
	   }

}