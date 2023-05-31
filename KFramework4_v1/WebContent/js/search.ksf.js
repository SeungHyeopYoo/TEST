/*******************************************************
* 프로그램명 	 : search.ksf.js 
* 설명        : 통합검색 환경에서 KSF 통해 조회하는 공통 함수 - ajax 함수 사용
* 작성일      : 2017.12.
* 작성자      : 김승희
* 수정내역    : -
*******************************************************/


/**
 * 화면 구동시 호출하는 함수정보  
 * 사용되는 함수목록은 자동완성(), 오타교정(), 인기검색어() 기본 사용합니다.
 * - 해당 정보 호출하기전에 KSF 모듈에서 기능 사용을 적용하였는지 확인되어야 합니다.
 */
$(function(){
	// 자동완성
	autocomplete();
	
	// 인기검색어
	popularkey();
	
	// 오타 교정
	getSpellchek();
	
	// 추천검색어
	getRelatedKwd();
	
	//최근검색어(위젯)
	$("#recent").recent();
	
	// 오타교정 관련
	/*
	//오타교정 건수체크하기
	var rsCount = '<c:out value="${total}" />';
	var kwd = '<c:out value="${params.kwd}" />';
	
	if(kwd.length > 0 && rsCount == 0){
		getSpellchek();
	*/
	
	//오류메시지출력
	/*
	var message = '<c:out value="${message}" />';
	if(message.length > 0){
		alert(message);
		return false;
	}
	*/
	
});

/**
* 통합검색어 인기검색어
* ksf 표준소스 기준으로 ajax 호출하여 데이터를 보여준다.
* @ return str 			
**/
function popularkey(){
	console.log("test입니다. 시작");
	$.ajax ({
		url: "/ksf/ppk.do",
		// url: "<c:url value='ksf/ppk'/>",
		type: "GET",
		contentType: 'application/json;charset=UTF-8'
	})
	.success(function(data) {
		if(data.length > 0){				
			console.log("[initPPK] success");
			var ppkHTML = "";
			var metaClass="";
			ppkHTML = "<ol class='konan-rankings'>";
			var ppkValue;
			
			$.each(data, function( i, item) {
				
				ppkValue = (item.ppk).replace(/</gi,"&lt;").replace(/>/gi,"&gt;");
				if(item.meta == "new") metaClass = "new"
				else if( Number(item.meta) == 0) metaClass = "equal"	
				else if( Number(item.meta) > 0) metaClass = "arrow-up"
				else	 metaClass = "arrow-down"
				
				ppkHTML +=	"<li>";
				ppkHTML += 	"<span class='rank'>"+i+"</span>";
				ppkHTML +=	"<a name='ppk'>" + ppkValue + "</a>";
				ppkHTML +=   "<span class ='"+metaClass+"'>" +item.meta+"</span>";
				ppkHTML +=	"</li>";
				
			})
		}
		ppkHTML += "</ol>";
		$("#rankings").html(ppkHTML);
	})
	.error(function(xhr, ajaxOptions,request, status, error){
		console.log("실패");
		console.log(xhr);
		console.log(ajaxOptions);
		console.log("code: " + request.status);
	    console.log("message: " + request.responseText);
	    console.log("error: " + error);
	});
}



/**
* 통합검색어 자동완성 
* ksf 표준소스 기준으로 ajax 호출하여 데이터를 보여준다.
* @ return str 			
**/
function autocomplete(){
	$("input[name=kwd]").autocomplete( { source: "/ksf/akc.do" } );	
}


/**
* 카테고리 매칭 함수(카운트). 
* 카테고리 코드값을 넘겨주면, total 값을 리턴한다.
* 
* @ return str 			
**/
function getSpellchek() {
	var kwd = $.trim($("input[name=kwd]").val());
	var ajax = {
			type: "GET",
			url: "",
			data: {},
			success: {},
			error: {}
	};
	ajax.url = 'ksf/spell.do';
	ajax.data = { term :kwd};
	ajax.success = function(data) {
		console.log("[getSpellchek] success");
		if(data.length > 0){
			
			var rsData;

			rsData = '<strong>' + kwd + '</strong>' + ' 로 검색한 결과입니다. ';

			data.forEach(function(item) {
				rsData += '&nbsp<strong><a href=\"javascript:getSpellKwd(\'' + item + '\')\">' + item + '</strong> ';
			});

			rsData += ' (으)로 검색하시겠습니까?</a>';
			$(".proofs_wrap").html(rsData);
			$(".proofs_wrap").css("display","block");
			}
	};	
	ajax.error = function(xhr, ajaxOptions) {
		console.log(ajaxOptions);
	};
	
	$.ajax(ajax);
};

//검색어 교정을 통해 바로 검색활성화
function getSpellKwd(value){
	$('#kwd').val(value);
	$('#historyForm').submit();
};


/**
* 추천검색어 
* 카테고리 코드값을 넘겨주면, total 값을 리턴한다.
* 
* @ return str 			
**/
function getRelatedKwd() {
	var kwd = $.trim($("input[name=kwd]").val());
	var ajax = {
			type: "GET",
			url: "",
			data: {},
			success: {},
			error: {}
	};
	ajax.url = '/ksf/kre.do';
	ajax.data = { term :kwd};
	ajax.success = function(data) {
		console.log("[getRelatedKwd] success");
		if(data.length > 0){
			var rsData;

			rsData = '<strong>관련검색 </strong>';
			
			data.forEach(function(item) {
				rsData += '&nbsp&nbsp' + '<a href=\"javascript:getRelatedKwd2(\''+item+'\')\">'+item+'</a>&nbsp&nbsp';
				rsData += ' | ';
				
			});
			
			rsData = rsData.slice(0, -2);

			$(".kre_wrap").html(rsData);
			$(".kre_wrap").css("display","block");
		}
	};	
	ajax.error = function(xhr, ajaxOptions) {
		console.log(ajaxOptions);
	};
	
	$.ajax(ajax);
};

function getRelatedKwd2(value){
	$('#kwd').val(value);
	$('#historyForm').submit();
};