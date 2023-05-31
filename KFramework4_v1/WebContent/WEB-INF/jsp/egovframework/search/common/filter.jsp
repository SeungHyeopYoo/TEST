<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<c:if test="${params.category ne 'PERSON' && params.category ne 'SEARCH'}">
	<div class="sidebar-widget">
		<h5>정렬</h5>
		<input class="sr-only" type="radio" id="so1" name="sort" value="r" checked><label for="so1">정확도</label>
		<input class="sr-only" type="radio" id="so2" name="sort" value="d"><label for="so2">최신순</label>
		<i id="toggle-widget" class="pull-right fa fa-border fa-chevron-down"></i>
	</div>
	
	<div class="sidebar-widget">
		<h5>영역</h5>
		<input class="sr-only" type="radio" id="fs1" name="fields" value="" checked><label for="fs1">전체</label>
		<input class="sr-only" type="radio" id="fs2" name="fields" value="title"><label for="fs2">제목</label>
	</div>
	
	<div class="sidebar-widget">
		<h5>기간</h5>
		<div id="date-radio">
			<input class="sr-only" type="radio" id="dt1" name="date" value="" checked/><label for="dt1"> 전체</label>
			<input class="sr-only" type="radio" id="dt2" name="date" value="d"/><label for="dt2"> <span class="hidden-xs">최근</span> 1일</label>
			<input class="sr-only" type="radio" id="dt3" name="date" value="w"/><label for="dt3"> <span class="hidden-xs">최근</span> 1주</label>
			<input class="sr-only" type="radio" id="dt4" name="date" value="m"/><label for="dt4"> <span class="hidden-xs">최근</span> 1달</label>
			<input class="sr-only" type="radio" id="dt5" name="date" value="y"/><label for="dt5"> <span class="hidden-xs">최근</span> 1년</label>
			<label class="empty">&nbsp;</label>
			<div class="hidden-xs hidden-sm input-group pull-right">
				<a id="open-date-input" class="btn btn-default btn-xs">직접입력</a>
			</div>
		</div>
		<div id="date-input" style="display: none;">
			<div class="pull-right input-group">
				<input type="text" name="date-from" data-toggle="date"/>
			</div>
			<div class="pull-right input-group">
				<input type="text" name="date-to" data-toggle="date"/>
			</div>
			<span class="pull-right">
				<a id="apply-date-input" class="btn btn-default btn-xs">적용</a>
				<a id="close-date-input" class="btn btn-default btn-xs">닫기</a>
			</span>
		</div>
	</div>
</c:if>
<c:if test="${params.category eq 'DOCUMENT'}">
	<div class="sidebar-widget">
		<h5>문서유형</h5>
		<ul class="list-inline">
			<li><label><input type="checkbox" name="fileext" value="ALL"/> 전체</label></li>
			<li><label><input type="checkbox" name="fileext" value="hwp"/> <img src="images/ico_hwp.gif"/> HWP</label></li>
			<li><label><input type="checkbox" name="fileext" value="doc"/> <img src="images/ico_doc.gif"/> DOC</label></li>
			<li><label><input type="checkbox" name="fileext" value="ppt"/> <img src="images/ico_ppt.gif"/> PPT</label></li>
			<li><label><input type="checkbox" name="fileext" value="xls"/> <img src="images/ico_xls.gif"/> XLS</label></li>
			<li><label><input type="checkbox" name="fileext" value="pdf"/> <img src="images/ico_pdf.gif"/> PDF</label></li>
			<li><label><input type="checkbox" name="fileext" value="etc"/> <img src="images/ico_etc.gif"/> 기타</label></li>
		</ul>
		<div class="input-group pull-right">
			<a id="apply-fileext" class="btn btn-default btn-xs">검색</a>
		</div>
	</div>
</c:if>