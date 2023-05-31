package egovframework.kf.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.kf.dao.BoardDAO;
import egovframework.kf.data.ParameterVO;
import egovframework.kf.data.RestResultVO;
import egovframework.kf.service.BoardService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.fdl.property.EgovPropertyService;

@Service("boardService")
public class BoardServiceImpl extends EgovAbstractServiceImpl implements BoardService {
	
	/** BoardDAO */
	@Resource(name = "boardDAO")
	private BoardDAO boardDAO;
	
	/** EgovPropertyService */
	@Resource(name = "konanPropertiesService")
	protected EgovPropertyService konanPropertiesService;
	
	@Override
	public RestResultVO BoardSearch(ParameterVO paramVO)  {
		RestResultVO resultVO = null;
		try {
			resultVO = boardDAO.boardSearch(paramVO);
			if (resultVO == null)
				throw processException("info.nodata.msg");
		}catch (Exception e) {
			e.printStackTrace();
		}
		return resultVO;
	}
}
