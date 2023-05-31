<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:if test="${not empty suggestions}">
  <dl class="suggestions dl-horizontal">
    <dt><span>연관어</span></dt>
    <dd>
      <ul class="list-inline">
      <c:forEach var="suggestion" items="${suggestions}">
        <li><a href="#"><c:out value="${suggestions}"/></a></li>
      </c:forEach>
      </ul>
    </dd>
  </dl>
</c:if>
<c:if test="${not empty censored}">
  <div class="page-header">
    <span><strong>"<c:out value="${censored}"/>"</strong>이(가) 검색어에서 제외되었습니다.</span>
  </div>
</c:if>             
   
<c:if test="${not empty spell}">
  <dl class="suggestions dl-horizontal">
    <dt><span>검색어제안</span></dt>
    <dd><strong>"<a><c:out value="${spell}"/></a>"</strong>로 검색하시겠습니까?</dd>
  </dl>
</c:if>
  <c:if test="${param['target'] == 'TOTAL'}">
    <div class="page-header">
      <strong>"<a><c:out value="${stuffs.query}"/></a>"에 대한 검색결과</strong> 
      <small>총 <fmt:formatNumber value="${boardTotal}" groupingUsed="true"/>건</small>
    </div>

</c:if>

<c:if test="${param['target'] == 'TOTAL'}">
    <div class="page-header">
      <strong>"<a><c:out value="${stuffs.query}"/></a>"에 대한 검색결과</strong> 
      <small>총 <fmt:formatNumber value="${personTotal}" groupingUsed="true"/>건</small>
    </div>
</c:if>

<c:if test="${not empty error}">
	<jsp:include page="error.jsp"/>
</c:if>


<!-- 게시판검색 -->
<jsp:include page="resultBoard.jsp"/>
<!-- // 게시판검색 -->				

<!-- 게시판검색 -->
<jsp:include page="resultPerson.jsp"/>
<!-- // 게시판검색 -->		
