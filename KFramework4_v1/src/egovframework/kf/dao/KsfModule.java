package egovframework.kf.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import egovframework.kf.common.DCUtil;
import egovframework.rte.fdl.property.EgovPropertyService;
/**
 * Class Name : KsfModule.java
 * Description : 검색엔진의 KSF 모듈이용하여 검색결과 조회 모듈
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
@Component("ksfModule")
public class KsfModule {
	
	private static final Logger logger = LoggerFactory.getLogger(KsfModule.class);
	
	/** EgovPropertyService */
	@Resource(name = "konanPropertiesService")
	private EgovPropertyService konanPropertiesService;

	@Autowired
	private DCUtil dc;


    /** 
     * 검색어 자동완성 메소드 (getAutocomplete).
     * 
     * @param seed 키워드
     * @param maxResultCount 최대 결과 수
     * @param flag 결과 형식 플래그 (앞, 뒤 단어 일치 여부)
     * @param mode 첫단어, 끝단어
     * @param domainNo 모듈 도메인 번호
     * @return String
     */
    public String getAutocomplete(String seed, String mode, int maxResultCount, int domainNo)
    {
    	String charset = konanPropertiesService.getString("charset");   	
        StringBuffer ksfUrl = new StringBuffer();
        ksfUrl.append( konanPropertiesService.getString("ksfUrl"));
        ksfUrl.append("suggest");
        ksfUrl.append("?target=complete");
        ksfUrl.append("&term=" + dc.urlEncode(seed,charset) );
        ksfUrl.append("&mode=" + mode);
        ksfUrl.append("&domain_no=" + domainNo); 
        ksfUrl.append("&max_count=" + maxResultCount);             
      
        logger.debug("suggest url : " + ksfUrl.toString());
        
        StringBuffer sb = dc.getUrlData(ksfUrl.toString(), charset);
        return sb.toString();
        
    }
	
	
	/**
	 * 인기검색어를 조회하
	 * @param domainNo 도메인
	 * @param maxResult 검색결과 수
	 * @return
	 */
	public List<Map<String, String>> getPopularKwd(int domainNo, int maxResult) {
		StringBuffer ksfUrl = new StringBuffer();
		ksfUrl.append(konanPropertiesService.getString("ksfUrl"));
		ksfUrl.append("rankings");
		ksfUrl.append("?domain_no=" + domainNo);
		ksfUrl.append("&max_count=" + maxResult);				 
		
		logger.debug("rankings url : " + ksfUrl.toString());
		String charset = konanPropertiesService.getString("charset");  
		StringBuffer sb = dc.getUrlData(ksfUrl.toString(), charset);

		// 결과 파싱
		try{
			
			
          JSONParser parser = new JSONParser();
          Object obj = parser.parse(sb.toString());
          
          JSONArray arr = (JSONArray) obj;

			List<Map<String, String>> list = new ArrayList<> ();
			Map<String, String> map;
			JSONArray ppk;
			for( Object o : arr){
				map = new HashMap<>();				
				ppk =(JSONArray) o;
				
				map.put("ppk", ppk.get(0).toString());
				map.put("meta", ppk.get(1).toString());
				

				
				list.add(map);
				map = null;
			}
			
			return list;

		} catch (ParseException e){
			logger.error("konan search engine search error...", e);
			return null;
		}
		
	}

	
    /**
     * 오타교정
     * @param term 키워드
     * @return String 
     */
    public String getSpellChek(String term)
    {
    	String charset = konanPropertiesService.getString("charset");
        StringBuffer ksfUrl = new StringBuffer();
        ksfUrl.append(konanPropertiesService.getString("ksfUrl"));
        ksfUrl.append("suggest");
        ksfUrl.append("?target=spell");
        ksfUrl.append("&term=" + dc.urlEncode(term, charset ) );    

        logger.debug("spell url : " + ksfUrl.toString());
        
        StringBuffer sb = dc.getUrlData(ksfUrl.toString(), charset);
        return sb.toString();
    } 
	
    
    /**
     * 추천검색어
     * @param domainNo 도메인
     * @param term 키워드
     * @param maxCount 검색결과 수
     * @return String 
     */
    public String getRelatedKwd(int domainNo, int maxCount, String term) {
    	String charset = konanPropertiesService.getString("charset");
        StringBuffer ksfUrl = new StringBuffer();
        ksfUrl.append(konanPropertiesService.getString("ksfUrl"));
        ksfUrl.append("suggest");	
        ksfUrl.append("?target=related");
        ksfUrl.append("&domain_no=" + domainNo);
        ksfUrl.append("&term=" + dc.urlEncode(term, charset ) );  
        ksfUrl.append("&max_count=" + maxCount);

        logger.debug("related url : " + ksfUrl.toString());
        
        StringBuffer sb = dc.getUrlData(ksfUrl.toString(), charset);
        return sb.toString();
    } 
	
}
