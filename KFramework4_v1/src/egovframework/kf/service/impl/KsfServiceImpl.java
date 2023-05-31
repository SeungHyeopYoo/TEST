package egovframework.kf.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.kf.dao.KsfModule;
import egovframework.kf.service.KsfService;

/**
 * Class Name : KsfServiceImpl.java
 * Description : 코난 검색엔진의 KSF 모듈을 이용한 검색을 위한 컨트롤러
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
@Service("ksfService")
public class KsfServiceImpl implements KsfService {
	
	/** REST 모듈 */
	@Resource(name = "ksfModule")
	private KsfModule ksfModule;
	
	@Override
	public List<Map<String, String>> getPopularKwd(int doaminNo, int maxResult) {
		return ksfModule.getPopularKwd(doaminNo, maxResult);
	}

	@Override
	public String getRelatedKwd(int doaminNo, int maxCount, String kwd) {
		return ksfModule.getRelatedKwd(doaminNo, maxCount, kwd);
	}

	@Override
	public String getAutocomplete(String seed, String mode, int maxResultCount, int domainNo) {
		return ksfModule.getAutocomplete(seed, mode,maxResultCount ,domainNo);
	}
	
	@Override
	public String getSpellChek(String term){
		return ksfModule.getSpellChek(term);
	}
}
