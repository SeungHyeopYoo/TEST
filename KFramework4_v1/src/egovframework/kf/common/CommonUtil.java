package egovframework.kf.common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import egovframework.kf.data.ParameterVO;
import egovframework.kf.data.ResultListVO;


/**
 * Class Name : CommonUtil.java
 * Description : 공통 
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
 * */
@Component
public class CommonUtil {
	private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);
	
	/**
	 * 입력받은 문자열특수문자를 html format으로 변환. 
	 * 
	 * @param str 변환할 문자열
	 * 
	 * @return 변환된 문자열
	 */
	public static String formatHtml(String str) {
		if (str.length() == 0) {
			return new String("&nbsp;");
		}

		String t = "";
		
		for (int i = 0; i < str.length(); i++) {
			switch (str.charAt(i)) 
			{
				case '<': t += "&lt;"; break;
				case '>': t += "&gt;"; break;
				case '&': t += "&amp;"; break;
				case '\"': t += "&quot;"; break;
				case '\'':  t += "\\\'"; break;
				case '\r':
					t += "<br>\n";
					if( i<str.length()-1 && str.charAt(i+1)=='\n') i++;
					break;
				case '\n': t += "<br>\n"; break;
				default: t += str.charAt(i); break;
			}
		}
		return t;
	}

   /** 
	* YYYYMMDD 포멧의 문자열을 입력받아 정의한 구분자를 사용하여 YYYY.MM.DD 포멧으로 변환.
	* 
	* @param str 변환할 문자열
	* @param deli 구분자
	* 
	* @return 변환된 문자열
	*/
	public String formatDateStr(String str, String deli) {
		StringBuffer t = new StringBuffer("");
		if (str == null || str.length() < 8) {
			return null; 
		} else {
			t.append(str.substring(0,4)).append(deli)
             .append(str.substring(4,6)).append(deli)
		     .append(str.substring(6,8));
			 
			return t.toString();
		}
	}

	/**
	* 문자열이 긴 경우에 입력받은 문자길이로 자른다.
	*	@param str 변환할 문자열
	*	@param cutLen 스트링 길이
	*	@param tail tail 스트링
	*	@return 변환된 문자열
	*/
	public String getCutString(String str, int cutLen, String tail) { 

		int strLength = str.length();
		
		if (strLength <= cutLen) {
			return str; 
		}

		str = str.substring(0, cutLen) + tail;
		
		return str; 
	}
	
	
	/** 널이거나 빈 문자열을 원하는 스트링으로 변환한다<br>
	* 단, 좌우 공백이 있는 문자열은 trim 한다 <br>.
	*
	* @param org 입력 문자열
	* @param converted 변환 문자열
	* @return 치환된 문자열
	*/
	public String null2Str(String org, String converted) {
		if (org == null || org.trim().length() == 0) {
			return converted;
		} else {
			return org.trim();
		}
	}
	
	/** 널이거나 빈 문자열(숫자형)을 integer로 변환한다.
	*
	* @param org 입력문자열
	* @param converted 변환숫자
	* @return 치환된 Interger
	*/
	public int null2Int(String org, int converted) {
		int i = 0;
		
		if (org == null || org.trim().length() == 0) {
			return converted;
		} else {
			try {
				i = Integer.parseInt(org); 
			} catch (Exception ex) { 
				i = converted;
			}
			return i;
		}
	}
	
	/** 스트링 숫자열의 포맷을  ###,### 으로 변환하여 리턴함.
	* @param str 숫자문자열
	* @return 변환된 문자열
	*/
	public String formatMoney(String str) {
		String result = "0";
		try {
			double iAmount = (new Double(str)).doubleValue();
			java.text.DecimalFormat df = new java.text.DecimalFormat("###,###,###,###,###,###,###");
			return df.format(iAmount);
		} catch (NumberFormatException e) {
			System.out.println("formatMoney error");
		}
		return result;
	}

	/** Int형 숫자의 포맷을  ###,### 으로 변환하여 리턴함.
	* @param num 정수값
	* @return 변환된 문자열
	*/
	public String formatMoney(int num) {
		String result = "0";
		try {
			double iAmount = (new Double(num)).doubleValue();
			java.text.DecimalFormat df = new java.text.DecimalFormat("###,###,###,###,###,###,###");
			
			return df.format(iAmount);
		} catch (Exception e) {
			System.out.println("formatMoney error");
		}
		return result;
	}

    /** 
     * 문자열의 인코딩을 변환하여 리턴함.
     * 
     * @param str : 인코딩 변환할 스트링
     * @param myEnc : 현재 인코딩
     * @param targetEnc : 타겟 인코딩
     * @return 변환된 문자열 
     */
    public String changeEncode(String str, String myEnc, String targetEnc) {
        if (str == null || str.trim().equals("")) {
        	return str;
        }
        
        try {
            return new String(str.getBytes(myEnc), targetEnc);
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
    }
    
    /** 
     * 현재 날짜로부터 특정 기간 전, 후 날짜 구하여 반환함.
     * 
     * @param iDay 현재로부터 구하고자 하는 날짜 수 int (과거 : 음수, 미래 : 양수)
     * @return date 값 문자열 
     */
    public String getTargetDate(int iDay) {
		Calendar temp = Calendar.getInstance();
		StringBuffer sbDate = new StringBuffer();

		temp.add(Calendar.DAY_OF_MONTH, iDay);

		int nYear = temp.get(Calendar.YEAR);
		int nMonth = temp.get(Calendar.MONTH) + 1;
		int nDay = temp.get(Calendar.DAY_OF_MONTH);
		
		sbDate.append(nYear);
		
		if (nMonth < 10) {
			sbDate.append("0");
			sbDate.append(nMonth);
		}else{
			sbDate.append(nMonth);
		}
		
		if (nDay < 10) {
			sbDate.append("0");
			sbDate.append(nDay);
		}else{
			sbDate.append(nDay);
		}

		return sbDate.toString();
	}
    
    /** 
     * 이전검색어 히든 태그 생성 후 반환.
     * 
     * @param srchParam ParameterVO 오브젝트
     * @return 이전 검색어 태그 문자열
     */
    public StringBuffer makeHtmlForPreKwd(ParameterVO srchParam) {
    	StringBuffer preKwdStr = new StringBuffer("");
    	int tmpCnt = 0;

    	preKwdStr.append("<input type='hidden' name=\"preKwdStr\" value=\"" + srchParam.getKwd() + "\" />\n");
    	if (srchParam.getReSrchFlag()) {
    		if ( srchParam.getPreKwds() != null ) {
    			int preKwdCnt = srchParam.getPreKwds().length;
    			
    			tmpCnt = 0;
    			//if (!escapeQuery(prevKwd[i]).equalsIgnoreCase(kwd)) {
    			if (srchParam.getPreKwds()[0].equals(srchParam.getKwd()) && preKwdCnt > 1 ) {
    				tmpCnt = 1; 
    			}
    			
    			for ( ; tmpCnt < preKwdCnt; tmpCnt++) {
    				if (!srchParam.getPreKwds()[tmpCnt].equalsIgnoreCase(srchParam.getKwd())) {
    					//System.out.println(tmpCnt + " : " + srchParam.getPreKwds()[tmpCnt]);
    					preKwdStr.append( "<input type=\"hidden\" name=\"preKwd\" value=\"").append(srchParam.getPreKwds()[tmpCnt]).append("\" />\n");
    				}
    			}

    			/* 이전검색어 & 키워드가 존재하는 경우 / 첫페이지내 검색시만 생성 / 2개 키워드가 같지 않은경우*/
    			if ( srchParam.getKwd().length() > 0 && srchParam.getPageNum() == 1
    				&& !srchParam.getKwd().equals(srchParam.getPreKwds()[0]) ) {
    				srchParam.setRecKwd(srchParam.getPreKwds()[0] + "||" + srchParam.getKwd());    // 추천검색어 구성용 단어 생성
    			}
    		} // end of if
    	} // end of if  
    	
    	
    	return preKwdStr;
    }
    
    /**
     * target값과 비교값이 같을 경우 특정 값을 리턴.
     * 
     * @param target 대상값
     * @param str 비교값
     * @param returnVal 반환할 값 지정
     * @return returnVal
     */
    public String makeReturnValue(String target, String str, String returnVal) {
    	if (target.equalsIgnoreCase(str)) {
    		return returnVal;
    	} else {
    		return "";
    	}
    }
    
    /** 
     * target값과 비교값이 같을 경우 특정 값을 리턴.
     * 
	 * @param target 대상값
	 * @param str 비교값 배열
	 * @param returnVal 반환할 값 지정
	 * @return returnVal
	 */
    public String makeReturnValue(String target, String[] str, String returnVal) {
    	if (str != null) {
    		for (int i=0; i<str.length; i++) {
    			if (target.equalsIgnoreCase(str[i])) {
    				return returnVal;
        		}	
    		}
    	}
    	
    	return "";
    }
    
    /** 
     * target값에 비교값이 있는 경우 true를 리턴
     * 
     * @param target 대상값
     * @param str 비교값 배열
     * 
     * @return true or false
	 */
    public boolean isEqu(String target, String[] str) {
    	if (str != null) {
    		for (int i=0; i<str.length; i++) {
    			if (target.equalsIgnoreCase(str[i])) {
    				return true;
        		}	
    		}
    	}
    	return false;
    }
    
    
    /** 
     * target값과 비교값이 같을 경우 특정 값을 리턴.
     * 
     * @param target 대상값
	 * @param str 비교값
	 * @param trueVal target 값과 비교값이 동일할 경우 리턴값 
	 * @param falseVal target 값과 비교값이 동일하지 않을 경우 리턴값
	 * 
	 * @return trueVal or falseVal 값
	 */
    public String makeReturnValue(String target, String str, String trueVal, String falseVal) {
    	if (target.equalsIgnoreCase(str)) {
            return trueVal;
        } else {
            return falseVal;
        }
    }

    /** 
     * 첨부파일명에 따른  이미지 파일명을 리턴함.
     *  
     * @param fileName 파일명
     *  
     * @return 이미지 파일명
     */
    public static String getAttachFileImage(String fileName) {
    	
    	String fileExt = "";
    	String imgFile = "";
    	
    	//파일 확장자명 추출
    	fileExt = fileName.substring(fileName.indexOf(".")+1);
    	
    	if ("doc".equalsIgnoreCase(fileExt) || "docx".equalsIgnoreCase(fileExt)) {
    		imgFile = "ico_doc.gif";
    	} else if ("ppt".equalsIgnoreCase(fileExt) || "pptx".equalsIgnoreCase(fileExt)) {
    		imgFile = "ico_ppt.gif";
    	} else if ("xls".equalsIgnoreCase(fileExt) || "xlsx".equalsIgnoreCase(fileExt)) {
    		imgFile = "ico_xls.gif";
    	} else if ("hwp".equalsIgnoreCase(fileExt)) {
    		imgFile = "ico_hwp.gif";
    	} else if ("zip".equalsIgnoreCase(fileExt) || "gzip".equalsIgnoreCase(fileExt) 
    			|| "tar".equalsIgnoreCase(fileExt) || "azip".equalsIgnoreCase(fileExt) 
    			|| "bzip".equalsIgnoreCase(fileExt)) {
    		imgFile = "ico_zip.gif";
    	} else if ("pdf".equalsIgnoreCase(fileExt)) {
    		imgFile = "ico_pdf.gif";
    	} else {
    		imgFile = "ico_etc.gif";
    	}
    	
    	return imgFile;
    }
    
    /** 
     * 첨부파일명에 따른  이미지 파일명을 리턴함.
     *  
     * @param fileName 파일명
     *  
     * @return 이미지 파일명
     */
    public static String getAttachFileImage(Object objFileName) {
    	if(objFileName == null) 
    		return "ico_etc";
    	
    	String fileName = objFileName.toString();
    	
    	String fileExt = "";
    	String imgFile = "";
    	
    	//파일 확장자명 추출
    	fileExt = fileName.substring(fileName.indexOf(".")+1);
    	
    	if ("doc".equalsIgnoreCase(fileExt) || "docx".equalsIgnoreCase(fileExt)) {
    		imgFile = "ico_doc.gif";
    	} else if ("ppt".equalsIgnoreCase(fileExt) || "pptx".equalsIgnoreCase(fileExt)) {
    		imgFile = "ico_ppt.gif";
    	} else if ("xls".equalsIgnoreCase(fileExt) || "xlsx".equalsIgnoreCase(fileExt)) {
    		imgFile = "ico_xls.gif";
    	} else if ("hwp".equalsIgnoreCase(fileExt)) {
    		imgFile = "ico_hwp.gif";
    	} else if ("zip".equalsIgnoreCase(fileExt) || "gzip".equalsIgnoreCase(fileExt) 
    			|| "tar".equalsIgnoreCase(fileExt) || "azip".equalsIgnoreCase(fileExt) 
    			|| "bzip".equalsIgnoreCase(fileExt)) {
    		imgFile = "ico_zip.gif";
    	} else if ("pdf".equalsIgnoreCase(fileExt)) {
    		imgFile = "ico_pdf.gif";
    	} else {
    		imgFile = "ico_etc.gif";
    	}
    	
    	return imgFile;
    }
    
    /**
     * 구분자로 연결된 첨부파일정보를 각각 나눠 배열로 리턴
     * 
     * @param fileId 첨부파일ID (ex. 첨부파일id1^첨부파일id2)
     * @param fileName 첨부파일명 (ex. 첨부파일명1^첨부파일명2)
     * @param deli 첨부파일명 구분자 (ex. \\^)
     * 
     * @return retStr
     */ 
	public String[][] getMutiFileInfo(String fileId, String fileName, String deli) {
		String[][] retStr 	= null;										
		String[] tmpId		= null;									// 첨부파일 Id 배열
		String[] tmpName	= null;									// 첨부파일 Name 배열
    	 
    	if (deli!=null && deli.length()>0) {
    		if (fileId.length()>0 && fileName.length()>0) {
    			tmpId 		= fileId.split(deli);
    			tmpName		= fileName.split(deli);
    			 
    			//file id, name 수가 동일한 경우
    			if (tmpId.length == tmpName.length) {
    				retStr = new String[tmpId.length][2];
    				 
    				for (int i=0; i<tmpId.length; i++) {
    					retStr[i][0] = tmpId[i];
    					retStr[i][1] = tmpName[i]; 
    				}
    			}
    		}	 
    	}
    	return retStr;
	}
	
      
    /** 
	 * 결과페이지의 first record 번호와 last record 번호를 deli 구분자로 묶어 반환 
	 * 
	 * @param pageNum 페이지 번호
	 * @param pageSize 페이지 사이즈
	 * @param total 전체 결과 개수	
	 * @param deli 구분자
	 * 	
	 * @return 변환된 문자열
	 */
	public String showRecordsNum(int pageNum, int pageSize, int total, String deli) {
		StringBuffer t 		= new StringBuffer("");
		int firstRecord 	= (pageNum - 1) * pageSize + 1;
		int lastRecord		= firstRecord - 1 + pageSize;
		
		if (lastRecord>total)
			lastRecord = total;
		
		t.append(firstRecord).append(deli).append(lastRecord);
		
		return t.toString();
	}
	
	
	/** 
	 * 입력받은 날짜 데이터값이 유효한지 체크
	 * 
 	 * @param str 입력값
 	 * 
	 * @return 유효하지 않은 경우 "" 를 반환
	 */
	public String checkDate(String str) {
		if (str == null || str.trim().equals("")) {
        	return str;
        }
		
		int i 		= 0;
		String ret 	= null;
		
		//숫자로 변환
		try {
			i 	= Integer.parseInt(str); 
		} catch (Exception ex) { 
			ret = "";
		}
		
		if (ret==null) {
			//검색엔진 date 관련 필드 색인 최소값 & 최대값
			if (i>=19000101 && i<=20331231) {
				ret = Integer.toString(i);
			}
		}
		return ret;
	}
	
	
	/**
	 * 경로가 포함된 파일명을 받아 경로를 제외한 순수 파일명(확장자 포함)을 리턴
	 *  
	 * @param fullName 경로포함파일명
	 * 
	 * @return fileName 파일명
	 */
	public String getFileName(String fullName) {
		String fileName = "";
		
		if(fullName.equals("")){
			return fileName;
		}
				
		String[] values = fullName.split("/");
		
		if(values.length > 0){ 
			fileName = values[values.length-1];
		}
		
		return fileName;
	}
	
	
	/**
	 * 페이지 정보를 가져와서 페이지 레인지를 리턴한다.
	 * 
	 * @param pageNum 페이지넘버
	 * @param pageSize 페이지크기(블럭)
	 * @param total 총합 개수
	 * 
	 * @return returnHtml(페이지 정보 문자열)
	 */
	public String getPageRange(int pageNum, int pageSize, int total) {
		String returnHtml = "";
		
		int lastPage = pageNum * pageSize;
		int startPage = lastPage-(pageSize-1);
		
		if(lastPage > total)
			lastPage = total;
		
		returnHtml = formatMoney(startPage) + "-" + formatMoney(lastPage) + " / " + formatMoney(total);
		
		return returnHtml;
	}
	
	
	/**
	 * 쿼리를 체크하여, 불리언 검색이 존재하면 엔진에 맞는 불리언 쿼리로 변경
	 * 
	 * @param query
	 * 
	 * @return boolean query
	 */
	public String parseBooleanQuery(String query) {
		String q = query;
		
		String[] chkStr = {"+","-"};
		String[] boolStr = {"&","!"};
		
		for(int i = 0; i < chkStr.length; i++) {
			if(q.indexOf(chkStr[i]) > -1) {
				q = q.replaceAll("\\" + chkStr[i], "\\" + boolStr[i]);				
			}
		}
		
		return q;
	}
	
	
	/**
	 * 값을 체크하여, boolean값이 존재할 시 flag값 리턴
	 * 
	 * @param kwd
	 * 
	 * @return boolean flag
	 */
	public boolean chkBooleanValue(String val) {
		String[] boolStr = {"&","!","|"};
		
		for(int i = 0; i < boolStr.length; i++) {
			if(val.indexOf(boolStr[i]) > -1) {
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * Byte Size -> Kilo Byte로 변환
	 * 
	 * @param bytesSize
	 * 
	 * @return Kilo Byte(int)
	 */
	public int parseKiloBytes(int bytesSize) {
		if(bytesSize == 0)
			return 0;
		
		return (bytesSize/1024);
	}
	
	
	/**
	 * Byte Size -> Kilo Byte로 변환
	 * 
	 * @param bytesSize
	 * @param ResultListVO
	 * 
	 * @return Kilo Byte(int)
	 */
	public ResultListVO parseKiloBytes(ResultListVO result, String field) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map;
		int size = 0;
		
		for (int i = 0; i < result.getList().size(); i++) {
			map = result.getList().get(i);
			
			if (map.get(field) != null) {
				size = Integer.parseInt(map.get(field));
				if (size > 1024) {
					size = (size/1024);
				} else {
					size = 1;
				}
				
				map.put(field, Integer.toString(size));
			}
			
			list.add(map);
		}
		
		result.setList(list);
		
		return result;
	}
	
	/**
	 * 값을 비교하여, 존재하는지 체크하여 true, false를 리턴한다.
	 * 
	 * @param comp (특정 sepa로 이루어진 값)
	 * @param val (변화되는 비교값)
	 * @param sepa (구분자)
	 * @param useIgnoreCase
	 * 
	 * @return boolean
	 */
	public boolean easyChkEqual(String comp, String val, String sepa, boolean useIgnoreCase) {
		if (comp == null || comp.length() == 0 || val == null || val.length() == 0)
			return false;
		
		String[] temp;
		temp = comp.split(sepa);
		
		for (int i = 0; i < temp.length; i++) {
			if (useIgnoreCase == true) {
				if(temp[i].equalsIgnoreCase(val)) {
					return true;
				}									
			} else {
				if(temp[i].equals(val)) {
					return true;
				}					
			}			
		}
		
		return false;
	}
	
	/**
	 * YYYYMMDD 포멧의 문자열을 입력받아 정의한 구분자를 사용하여 YYYY.MM.DD 포멧으로 변환.
	 * 
	 * @param result (ResultListVO)
	 * @param field (필드명)
	 * @param deli (리턴될 구분자)
	 * 
	 * @return YYYY.MM.DD
	 */
	public ResultListVO formatDateStr(ResultListVO result, String field, String deli) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map;
		StringBuffer sb;
		
		for (int i = 0; i < result.getList().size(); i++) {
			sb = new StringBuffer("");
			map = result.getList().get(i);
			
			if (map.get(field) != null && map.get(field).length() >= 8) {
				sb.append(map.get(field).substring(0,4)).append(deli)
			      .append(map.get(field).substring(4,6)).append(deli)
				  .append(map.get(field).substring(6,8));
				
				map.put(field, sb.toString());
			}
			
			list.add(map);
		}
		
		result.setList(list);		
		return result;
	}
	
	/**
	 * 하이라이트용 키워드를 추출한다.
	 * 
	 * @param kwd
	 * 
	 * @return extractKwd
	 */
	public String extractHighlightKwd(String kwd) {
		if(kwd == null || kwd.length() == 0)
			return "";
		
		String[] booleanKwd = {"\\&","\\!","\\|","\\\"","\\'"};
		String extractKwd = kwd;
		
		for (int i = 0; i < booleanKwd.length; i++) {
			extractKwd = extractKwd.replaceAll(booleanKwd[i], "");
		}
		
		logger.debug("extractKwd : " + extractKwd);
		return extractKwd;
	}
	
	
	/**
     * 현재 날짜로부터 특정 기간 전, 후 날짜 구하여 반환함.
     * 
     * @param iMonth 현재로부터 구하고자 하는 날짜 수 int (과거 : 음수, 미래 : 양수)
     * @return date 값 문자열 
	 */
	public String getTargetMonth(int iMonth) {
		Calendar temp = Calendar.getInstance();
		StringBuffer sbDate = new StringBuffer();

		temp.add(Calendar.MONTH, iMonth);

		int nYear = temp.get(Calendar.YEAR);
		int nMonth = temp.get(Calendar.MONTH) + 1;
		int nDay = temp.get(Calendar.DAY_OF_MONTH);
		
		sbDate.append(nYear);
		
		if (nMonth < 10) {
			sbDate.append("0");
			sbDate.append(nMonth);
		}else{
			sbDate.append(nMonth);
		}
		
		if (nDay < 10) {
			sbDate.append("0");
			sbDate.append(nDay);
		}else{
			sbDate.append(nDay);
		}

		return sbDate.toString();
	}
	
	@Override
	public String toString() {
	      return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}	
	

}


