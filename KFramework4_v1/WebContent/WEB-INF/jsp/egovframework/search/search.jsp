<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>	
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>	
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>	
<!DOCTYPE html>	
<html lang="ko">	
	<head>	
		<meta charset="utf-8">	
		<meta http-equiv="X-UA-Compatible" content="IE=edge">	
		<meta name="viewport" content="width=device-width, initial-scale=1.0">	
		<title>KFramework - connect git</title>	
	
		<!-- Stylesheets -->
		<link type="text/css" href="css/default.css" rel="stylesheet"><!-- Default -->	
		<link type="text/css" href="css/bootstrap.min.css" rel="stylesheet"><!-- Bootstrap -->	
		<link type="text/css" href="css/font-awesome.min.css" rel="stylesheet"><!-- Font Awesome -->	
		<link type="text/css" href="css/jquery-ui.css" rel="stylesheet"><!-- jQuery UI -->	
		<link type="text/css" href="css/jquery.justified.css" rel="stylesheet"><!-- Image gallery -->	
		<link type="text/css" href="css/lightbox.css" rel="stylesheet"><!-- Image lightbox -->	
		<link type="text/css" href="css/skin-konan/ui.fancytree.css" rel="stylesheet"><!-- Fancy Tree -->	
		<link type="text/css" href="css/default.css" rel="stylesheet"><!-- Main stylesheet -->	
		<link type="text/css" href="css/konan.sf.css" rel="stylesheet"><!-- KSF -->
		<link type="text/css" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css" rel="stylesheet"> <!-- Font Awesome 마커 -->
		<link type="text/css" href="css/dropdown.css" rel="stylesheet"><!-- dropdown -->	
			
		<!-- JS -->	
		<script type="text/javascript" src="js/jquery/jquery-1.11.0.js"></script><!-- jQuery -->
		<script type="text/javascript" src="js/jquery/jquery-ui.js"></script><!--jQuery UI -->
		<script type="text/javascript" src="js/jquery/jquery.cookie.js"></script><!-- Cookie -->
		<script type="text/javascript" src="js/jquery/jquery.fancytree.js"></script><!-- Fancy Tree -->
		<script type="text/javascript" src="js/jquery/jquery.deserialize.min.js"></script><!-- Form deserialization -->
		<script type="text/javascript" src="js/jquery/jquery.pagination.js"></script><!-- Pagination -->
		<script type="text/javascript" src="js/jquery/jquery.justified.js"></script><!-- Image gallery -->
		<script type="text/javascript" src="js/lightbox.js"></script><!-- Image lightbox -->
	
 		<script type="text/javascript" src="js/bootstrap.js"></script><!--Bootstrap	 -->
		<script type="text/javascript" src="js/jquery/jquery.konan.sf.js"></script><!-- KSF -->
		<script type="text/javascript" src="js/jquery/i18n/jquery.konan.sf-ko.js"></script><!-- KSF localization -->
		<script type="text/javascript" src="js/jquery/i18n/jquery.ui.datepicker-ko.js"></script><!-- Date picker localization -->
		<script type="text/javascript" src="js/jquery/jquery.inputmask.bundle.js"></script>
		<script type="text/javascript" src="js/search-ui.js"></script><!-- UI --> 
		<script type="text/javascript" src="js/search.js"></script><!-- Events -->
		<script type="text/javascript" src="js/search.ksf.js"></script><!-- KSF Events -->	
		<script type="text/javascript" src="js/dropdown.js"></script><!-- dropdown -->	
	</head>	
	<body>	
		<form id="historyForm" action="search.do" method="get">	
			<input type="hidden" id="category" name="category" value="<c:out value="${params.category}" />"/>	
			<input type="hidden" id="kwd" name="kwd" value="<c:out value="${params.kwd}" />"/>		
			<input type="hidden" id="date" name="date" value="<c:out value="${param.date}" />"/>	
			<input type="hidden" id="startDate" name="startDate" value="<c:out value="${param.startDate}" />"/>	
			<input type="hidden" id="endDate" name="endDate" value="<c:out value="${param.endDate}" />"/>		
			<input type="hidden" id="originalQuery" name="originalQuery" value="<c:out value="${param.originalQuery}" />"/>	
			<input type="hidden" id="previousQuery" name="previousQuery" value="<c:out value="${param.previousQuery}"/>"/>	
			<input type="hidden" id="previousQueries" name="previousQueries" value="<c:out value="${previousQueries}"/>"/>
			<input type="hidden" id="categorize" name="categorize" value="true"/>	
			<input type="hidden" id="page" name="page" value="0"/>	
			<input type="hidden" id="pageNum" name="pageNum" value="<c:out value="${param.pageNum}" />"/>	
			<input type="hidden" id="resrch" name="resrch" value="<c:out value="${param.resrch}" />"/>	
			<input type="hidden" id="sort" name="sort" value="<c:out value="${param.sort}" />"/>	
			<input type="hidden" id="fields" name="fields" value="<c:out value="${param.fields}" />"/>	
			<input type="hidden" id="fileext" name="fileext" value="<c:out value="${param.fileext}" />"/>	
			<input type="hidden" id="nowDate" name="nowDate" value="<c:out value="${param.nowDate}" />"/>
			<input type="hidden" id="notWord" name="notWord" value="<c:out value="${param.notword}" />"/>
			<input type="hidden" id="condition" name="condition" value="<c:out value="${param.condition}" />"/>
		</form>
		<!-- 헤더 영역 시작 -->	
		<header>
			<div class="container">	
				<nav class="navbar" role="navigation">	
					<ul class="nav navbar-nav navbar-right">	
						<li><a href="#">로그아웃</a></li>	
						<li><a class="btn btn-link btn-sm">도움말</a></li>							
					</ul>	
				</nav>	
					
				<!-- 상단 검색 -->	
				<jsp:include page="common/topSearch.jsp"/>	
				<!-- // 상단 검색 -->	
									
			</div><!--/.container-->	
		</header>	
		<!-- 헤더 영역 끝 -->	
	
		<div class="container">	
			<div class="row contents-wrapper">	
				<!-- 왼쪽 사이드바 시작 -->	
				<div id="sidebar" class="sidebar col-md-2">	
											
					<form class="form-horizontal" role="form">	
						
						<!-- 카테고리 네비게이션 -->	
						<jsp:include page="common/categoryNavigator.jsp"/>	
						<!-- // 카테고리 네비게이션 -->	
	
						<!-- 필터 -->	
						<jsp:include page="common/filter.jsp"/>	
						<!-- // 필터 -->	
					</form>	
				</div>	
				<!-- 왼쪽 사이드바 끝 -->	
	
				<div class="col-md-10 main-contents">	
					<%-- 이미지 검색 결과는 오른쪽 사이드바도 함께 사용함 --%>	
					<div class="row">	
						<div id="contents" class="col-md-9 contents">	
							<c:choose>	
								<c:when test="${header['X-Requested-With'] == 'XMLHttpRequest'}">									
									<%-- AJAX call --%>	
									<jsp:forward page="result/result.jsp"/>	
								</c:when>	
								<c:otherwise>	
									<!-- 게시판검색 -->	
									<jsp:include page="result/resultBoard.jsp"/>	
									<!-- 인물검색 -->	
									<jsp:include page="result/resultPerson.jsp"/>	
								</c:otherwise>	
								
							</c:choose>	
							
	
						
						</div>	
								
						<!-- 오른쪽 사이드바 -->	
						<div class="col-md-3 right-sidebar">								
							<!-- 인기검색어 -->	
							<jsp:include page="module/ppk.jsp"/>	
							<!-- // 인기검색어 -->	
							<!-- 최근검색어 -->	
							<jsp:include page="module/resent.jsp"/>
							<!-- //최근검색어 -->									
							<a href="#" class="scrolltop btn btn-default btn-sm">맨위로</a>	
						</div>	
						<!-- // 오른쪽 사이드바 -->	
		
					</div>					
				</div><!--/.main-contents-->	
			</div><!--/.contents-wrapper-->	
		</div><!--/.container-->	
	
		<!-- 푸터 영역 -->	
		<jsp:include page="common/footer.jsp"/>	
		<!-- // 푸터 영역  -->	
	</body>	
</html>