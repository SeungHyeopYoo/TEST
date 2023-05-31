package egovframework.kf.service;

import egovframework.kf.data.ParameterVO;
import egovframework.kf.data.RestResultVO;

/**
 * 게시판 검색하기 위한 서비스 인터페이스
 * 
 * @author seunghee.kim
 * @since 2016.06.30
 */
public interface BoardService {
	
	/**
	 * 게시판정보를 검색한다.
	 * @param paramVO
	 * @return
	 * @throws Exception
	 */
	public RestResultVO BoardSearch(ParameterVO paramVO) ;
}
