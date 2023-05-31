<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="box">
	<div class="box-header">
		<div class="title">인기검색어</div>
		<div class="actions">
			<span class="box-collapse ui-icon ui-icon-carat-1-n"></span>
		</div>
	</div>
	<div id="rankings" class="box-content">
		<ol class="konan-rankings">
			<c:forEach var="ppkResult" items="${ppkList}" varStatus="status">
				<li>
					<span class="rank">
						<em>${status.count}</em>
					</span>
					<a>${ppkResult.ppk}</a>
					
					<c:choose>
						<c:when test="${ppkResult.meta eq 'new'}">
							<span class="new">new</span>
						</c:when>
						<c:when test="${ppkResult.meta.indexOf('-') > -1}">
							<span class="arrow-down">${ppkResult.meta}</span>
						</c:when>
						<c:otherwise>
							<span class="arrow-up">${ppkResult.meta}</span>
						</c:otherwise>
					</c:choose>
					
				</li>											
			</c:forEach>
		</ol>
	</div>
</div><!--/.box-->
