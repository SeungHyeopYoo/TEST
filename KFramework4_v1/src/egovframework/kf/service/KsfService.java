package egovframework.kf.service;


import java.util.List;
import java.util.Map;


/**
 * Class Name : KsfService.java
 * Description : 코난 검색엔진의 KSF 모듈을 이용한 검색을 위한 서비스
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
public interface KsfService {
	
	
	/**
	 * 인기검색어를 호출한다.
	 * 
	 * @param kwd
	 */
	public List<Map<String, String>> getPopularKwd(int doaminNo, int maxResult);
	

	/**
	 * 추천(연관) 검색어를 호출한다.
	 * 
	 * @param doaminNo
	 * @param maxCount
	 * @param kwd
	 * @return
	 */
	public String getRelatedKwd(int doaminNo, int maxCount, String kwd);
	
	
    /** 
     * 검색어 자동완성 메소드
     * 
     * @param seed 키워드
     * @param maxResultCount 최대 결과 수
     * @param flag 결과 형식 플래그 (앞, 뒤 단어 일치 여부)
     * @param mode 첫단어, 끝단어
     * @param domainNo 모듈 도메인 번호
     * @return String 검색결과 반환
     */
	public String getAutocomplete(String seed, String mode, int maxResultCount, int domainNo);
	
	/**
	 * 검색어에 대한 오타검색결과를반환
	 * @param term
	 * @return String 검색결과 반환
	 */
	public String getSpellChek(String term);


}
