package egovframework.kf.dao;

import java.io.IOException;
import java.net.URLEncoder;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import egovframework.kf.common.CommonUtil;
import egovframework.kf.common.DCUtil;
import egovframework.kf.data.ParameterVO;
import egovframework.kf.data.RestResultVO;
import egovframework.kf.data.SearchVO;
import egovframework.rte.fdl.property.EgovPropertyService;

/**
 * Class Name : BoardDAO.java
 * Description : 검색대상 board 조회
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
@Repository
public class BoardDAO {
	private static final Logger logger = LoggerFactory.getLogger(BoardDAO.class);
	
	/** 엔진 공통 유틸 */
	@Resource(name = "dcUtil")
	private DCUtil dcUtil;
	
	/** common util Setting */
	@Resource(name = "commonUtil")
	private CommonUtil commonUtil;
	
	/** REST 모듈 */
	@Resource(name = "restModule")
	private RestModule restModule;
		
	/** EgovPropertyService */
	@Resource(name = "konanPropertiesService")
	protected EgovPropertyService konanPropertiesService;	

	/**
	 * 키워드에 맞는 문서관리 내용 리턴
	 * 
	 * @param kwd
	 * @throws IOException
	 */
	public RestResultVO boardSearch(ParameterVO paramVO) {
		SearchVO searchVO = new SearchVO();
		// 쿼리 생성
		StringBuffer query = new StringBuffer();
		StringBuffer sbLog = new StringBuffer();
		String strNmFd = paramVO.getFields().isEmpty()? "text_idx": paramVO.getFields();
		RestResultVO restVO = new RestResultVO();
		
		if(paramVO.getCondition().isEmpty()) {
			query = dcUtil.makeQuery( strNmFd , paramVO.getKwd(), "natural synonym('d0')", query, "AND");
		} else {
			switch(paramVO.getCondition()) {
				case "boolean" :
				case "proxkeymatch":
				case "allstring":
				case "anystring":
					query = dcUtil.makeQuery( strNmFd , paramVO.getKwd(), paramVO.getCondition(), query, "AND");
					break;
					
				// 고급검색 (anyword, allword, alladjacent, allorderadjacent, allwordthruindex, someword, somewordthruindex)
				default:
					// query = dcUtil.makeQuery( strNmFd , paramVO.getKwd(), paramVO.getCondition() + " synonym('d0')", query, "AND");
					
					// 제외어 쿼리 부분
					if(paramVO.getExclusiveKwd().isEmpty()) {	// 상세검색에서 제외어를 설정하지 않았을 경우
						query = dcUtil.makeQuery( strNmFd , paramVO.getKwd(), paramVO.getCondition() + " synonym('d0')", query, "AND");
					} else {
						paramVO.getExclusiveKwd().replaceAll(" ", "");		// 입력한 제외어 문자열의 공백을 모두 제거
						String[] strArr = paramVO.getExclusiveKwd().split(",");		// 콤마(,)를 기준으로 문자열을 분리
				
						String str = "";
						for(int i=0; i < strArr.length; i++) {
							strArr[i]= "\'" + strArr[i].replaceAll(" ", "") + "\',";		// 분리한 제외어들을 홑따옴표(')로 묶고 뒤에 콤마(,)를 붙임
							str += strArr[i];		// 홑따옴표로 묶은 각각의 인덱스들을 하나의 문자열로 합침
						}
						
						str = str.substring(0, str.length()-1);		// 맨 마지막 단어에 붙은 콤마(,)를 지워주는 역할
						
						paramVO.setExclusiveKwd(str);		// 제외어에 해당하는 파라미터에 작업한 문자열을 삽입
						query = dcUtil.makeQuery( strNmFd , paramVO.getKwd(),
													paramVO.getCondition() + " synonym('d0') -text_idx in {" + paramVO.getExclusiveKwd() + "}", query, "AND");
					}
			}
		}
		
		
		logger.info("##################################### paramVO.getCondition() = " + paramVO.getCondition());
		logger.info("##################################### paramVO.getKwd() = " + paramVO.getKwd());

		//결과내재검색
		/* if( paramVO.getReSrchFlag() ){
			int preCnt = paramVO.getPreviousQueries().length + 1;
			query.append(" AND ")
					// .append(dcUtil.makePreQuery(strNmFd , paramVO.getKwd(), paramVO.getPreviousQueries(), preCnt ,"allword") );	
		}*/ 
		
		//결과내재검색
	    if(paramVO.getReSrchFlag() && paramVO.getPreviousQueries() != null && paramVO.getPreviousQueries().get("board") != null) {
	    	if(paramVO.getPreviousQueries().toString().isEmpty() != true && paramVO.getPreviousQueries().get("board").toLowerCase().equals("null") != true){
	    		query.append(" AND  (").append(paramVO.getPreviousQueries().get("board")).append(")"); //map key는 볼륨명과 같게 지정         
	    	}
	    }
	
		/**
		 * 날짜검색기간 조회
		 * 기간/일/월/년도, 구간검색으로 조회시 자바스크립트에서 시작날짜와 종료날짜 조회하여 전달함.
		 */
		if (!paramVO.getStartDate().isEmpty() && !paramVO.getEndDate().isEmpty()){
			query = dcUtil.makeRangeQuery("regdate", paramVO.getStartDate().replace(".", "")+"000000",
											paramVO.getEndDate().replace(".", "")+"999999", query) ;
		}
		
		restVO.setPreviousQuery(query.toString()); // 이전 쿼리에 현재 쿼리를 넣음
		
		//정렬조건  (최신순)
		if(!paramVO.getCondition().equals("proxkeymatch") && !paramVO.getCondition().equals("allstring") && !paramVO.getCondition().equals("anystring")) {
		    if ("d".equals(paramVO.getSort())){
		    	query.append(" order by regdate desc");
		    } else {
		    	query.append(" order by $RELEVANCE desc");
		    }
		}

		
		//로그기록 
		//SITE@인물+$|첫검색|1|정확도순^코난	
		sbLog.append(  dcUtil.getLogInfo(commonUtil.null2Str(paramVO.getSiteNm(),"SITE"),
				"게시판",
				commonUtil.null2Str( paramVO.getUserId(),""), paramVO.getKwd(),
				paramVO.getPageNum(),
				false,
				paramVO.getSiteNm(),
				commonUtil.null2Str( paramVO.getRecKwd(),"" )) );
	
		searchVO.setUrl(konanPropertiesService.getString("url"));
		searchVO.setCharset(konanPropertiesService.getString("charset"));
		searchVO.setFields(konanPropertiesService.getString("boardField"));
		searchVO.setFrom(konanPropertiesService.getString("boardFrom"));
		searchVO.setHilightTxt(konanPropertiesService.getString("boardHilight"));
		try {
			searchVO.setQuery(URLEncoder.encode(query.toString(), konanPropertiesService.getString("charset") ));
			searchVO.setLogInfo(URLEncoder.encode(sbLog.toString(), konanPropertiesService.getString("charset")));
		
	
		// URL 생성
		//String restUrl = dcUtil.getRestURL(paramVO, searchVO); //get방식 URL생성
		String postParamData = dcUtil.getParamPostData(paramVO, searchVO);
		logger.debug("RESTURL board : " + postParamData);
		
		
		//boolean success = restModule.restSearch(restUrl, restVO, searchVO.getFields());  //get방식 호출
		
		boolean success = restModule.restSearchPost(searchVO.getUrl() , postParamData, restVO, searchVO.getFields()); //post 방식 호출
		
		
		//초기화
		query.charAt(0);
		sbLog.charAt(0);
		
		if(!success) 
			return null;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return restVO;		
	}
}
