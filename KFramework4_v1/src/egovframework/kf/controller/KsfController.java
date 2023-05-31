package egovframework.kf.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import egovframework.kf.service.KsfService;
import egovframework.rte.fdl.property.EgovPropertyService;

/**
 * Class Name : ksfController.java
 * Description : 코난 검색엔진의 KSF 모듈을 이용한 검색을 위한 컨트롤러
 *
 * Modification Information
 *
 * 수정일                        수정자           수정내용
 * --------------------  -----------  ---------------------------------------
 * 2017년 12월  00일     김승희            최초 작성
 *
 * @since 2017년
 * @version V1.0
 * @see (c) Copyright (C) by KONANTECH All right reserved
 */
@Controller
public class KsfController {	
	
	/** EgovPropertyService */
	@Resource(name = "konanPropertiesService")
	protected EgovPropertyService konanPropertiesService;
	
	@Autowired
	private KsfService ksfService;
	
	private static final Logger logger = LoggerFactory.getLogger(KsfController.class);

	
	/**
	 * 검색어 자동완성 단어 조회
	 * @param map
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ksf/akc.do", produces="application/json; charset=utf8")
	@ResponseBody
	public String getCompleteKwd(@RequestParam Map<String, String> map, Model model) {
		String domain = konanPropertiesService.getString("ksfDomain");
		String maxRsesult = konanPropertiesService.getString("ksfMaxResult");
		
		String result = null;
		
		if(StringUtils.isNotEmpty(map.get("term")) && map.get("term").length() > 0) {
			String kwd = map.get("term");
			String mode = (null == map.get("mode") || map.get("mode").isEmpty() )?"s":map.get("mode");
			int maxCount = Integer.parseInt((null == map.get("max_count") || map.get("max_count").isEmpty() )? maxRsesult :map.get("max_count"));
			int domainNo = Integer.parseInt((null == map.get("domain") || map.get("domain").isEmpty() )?domain:map.get("domain"));
			
			
			result = ksfService.getAutocomplete(kwd, mode, maxCount, domainNo);

		}
		return result;
	}	
	
	/**
	 * 오타교정 단어 조회
	 * @param map
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ksf/spell.do", produces="application/json; charset=utf8")
	@ResponseBody
	public String getSpellChek(@RequestParam Map<String, String> map, Model model) {
		
		String result = null;
		try {
			if(StringUtils.isNotEmpty(map.get("term")) && map.get("term").length() > 0) {
				String kwd = map.get("term");
				result = ksfService.getSpellChek(kwd);
	
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 인기검색어
	 * @param map
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/ksf/ppk.do", method = {RequestMethod.GET}, produces = "application/json; charset=utf8")
	public String getPopularKwd(@RequestParam Map<String, String> map, Model model) {
		Gson gson = new Gson();
		List<Map<String, String>> ppkList = null;
		try {
			logger.debug( "/ksf/ppk.do");
			
			String domain = konanPropertiesService.getString("ksfDomain");
			String maxRsesult = konanPropertiesService.getString("ksfMaxResult");
			
			int maxCount = Integer.parseInt((null == map.get("max_count") || map.get("max_count").isEmpty() )? maxRsesult :map.get("max_count"));
			int domainNo = Integer.parseInt((null == map.get("domain") || map.get("domain").isEmpty() )?domain:map.get("domain"));		
			
			ppkList = ksfService.getPopularKwd(domainNo, maxCount);
			
			logger.debug( gson.toJson(ppkList));
		}catch(Exception e){
			e.printStackTrace();
			logger.info(e.toString());
		}
		return gson.toJson(ppkList);
	}
	
	
	/**
	 * 추천 검색어
	 * @param map
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ksf/kre.do", produces="application/json; charset=utf8")
	@ResponseBody
	public String getRelatedKwd(@RequestParam Map<String, String> map, Model model) {
		logger.debug( "/ksf/kre.do");
		String result = null;
		try {
			String domain = konanPropertiesService.getString("ksfDomain");
			String maxResult = konanPropertiesService.getString("ksfMaxResult");
			
			int maxCount = Integer.parseInt((null == map.get("max_count") || map.get("max_count").isEmpty() )? maxResult :map.get("max_count"));
			int domainNo = Integer.parseInt((null == map.get("domain") || map.get("domain").isEmpty() )?domain:map.get("domain"));
			
			if(StringUtils.isNotEmpty(map.get("term")) && map.get("term").length() > 0) {
				String kwd = map.get("term");
				result = ksfService.getRelatedKwd(domainNo, maxCount, kwd);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
