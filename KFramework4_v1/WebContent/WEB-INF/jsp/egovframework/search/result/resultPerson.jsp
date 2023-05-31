<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:if test="${personTotal == 0}">
	<section>
	<h3>인물</h3>
		<div class="result-box">
			<span style="color: red;">'${params.kwd}'</span> 에 대한 검색결과가 존재하지 않습니다.
		</div>
	</section>
</c:if>

<c:if test="${personTotal > 0}">
<section>
	<h3>인물
	<c:if test="${params.category != 'TOTAL'}">	
		<small><fmt:formatNumber value="${personTotal}" groupingUsed="true"/>건</small>
	</c:if>
	</h3>
	<ul class="media-list">
	<c:forEach var="result" items="${personList}" varStatus="status">
	<fmt:parseDate value="${result.regdate}" var="dateFmt" pattern="yyyyMMddHHmmss"/>
    <li class="media">
      <div class="media-body">
        <a class="media-heading"><c:out value="${result.prs_nm_ko}"  escapeXml="false"/></a>
        <span><c:out value="${result.tree_data}"/> | <c:out value="${result.email}"/></span>
        <p><c:out value="${result.tree_data}"  escapeXml="false"/></p>

      </div>
    </li>
	</c:forEach>
  </ul>
	<c:if test="${personTotal > params.pageSize}">
	  <c:choose>
	  	<c:when test="${params.category eq 'TOTAL'}">
	      <div class="text-right more">
	      	<a data-target="PERSON">더보기</a>
	      </div>
	    </c:when>
	    <c:otherwise>
	    	<div class="text-center"><ul class="pagination" id="pagination"></ul></div>
	  	</c:otherwise>
	  </c:choose>
	</c:if>	
</section>
<script>
$(function() {
<c:if test="${personTotal  > personRow}">
    $('#pagination').pagination(<c:out value="${personTotal}"/>, {
        current_page: <fmt:formatNumber value="${params.offSet / params.pageSize}" minFractionDigits="0"/>,
        callback: gotopage
    });
</c:if>
});
</script>
</c:if>