<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<ul id="navi" class="nav nav-pills nav-stacked collapse in">
	<li data-target="TOTAL" <c:if test="${params.category eq 'TOTAL'}">class="active"</c:if> >
		<a onclick="goCategory('TOTAL');">통합검색</a>
	</li>
	<li data-target="PERSON" <c:if test="${params.category eq 'PERSON'}">class="active"</c:if> >
		<a onclick="goCategory('PERSON');">인물</a>
	</li>
	<li data-target="BOARD" <c:if test="${params.category eq 'BOARD'}">class="active"</c:if> >
		<a onclick="goCategory('BOARD');">게시판</a>
	</li>					
	<!-- <li data-target="DOCUMENT" <c:if test="${params.category eq 'DOCUMENT'}">class="active"</c:if> >
		<a onclick="goCategory('DOCUMENT');">문서</a>
	</li> -->
</ul>	