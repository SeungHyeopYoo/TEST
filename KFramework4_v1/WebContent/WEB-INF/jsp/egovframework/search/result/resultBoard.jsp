<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:if test="${params.category eq 'BOARD'}">
	<div>
		<ul class="nav nav-tabs">
			<li class="nav-item dropdown-toggle"><a class="nav-link">자연어 검색</a>
				<ul class="nav nav-pills dropdown-menu">
					<li class="nav-item"><a onclick="goCondition('natural');" class="nav-link">NATURAL</a></li>
				</ul>
			</li>
		
			<li class="nav-item dropdown-toggle"><a class="nav-link">고급 검색</a>
				<ul class="nav nav-pills dropdown-menu">
					<li class="nav-item"><a onclick="goCondition('anyword');" class="nav-link">ANYWORD</a></li>
					<li class="nav-item"><a onclick="goCondition('allword');" class="nav-link">ALLWORD</a></li>
					<li class="nav-item"><a onclick="goCondition('alladjacent');" class="nav-link">ALLADJACENT</a></li>
					<li class="nav-item"><a onclick="goCondition('allorderadjacent');" class="nav-link">ALLORDERADJACENT</a></li>
					<li class="nav-item"><a onclick="goCondition('allwordthruindex');" class="nav-link">ALLWORDTHRUINDEX</a></li>
					<li class="nav-item"><a onclick="goCondition('someword');" class="nav-link">SOMEWORD</a></li>
					<li class="nav-item"><a onclick="goCondition('somewordthruindex');" class="nav-link">SOMEWORDTHRUINDEX</a></li>
				</ul>
			</li>
		  	
			<li class="nav-item dropdown-toggle"><a class="nav-link">불리언 검색</a>
				<ul class="nav nav-pills dropdown-menu">
					<li class="nav-item"><a onclick="goCondition('boolean');" class="nav-link">BOOLEAN</a></li>
				</ul>
			</li>
			
			<li class="nav-item dropdown-toggle"><a class="nav-link">근접어 검색</a>
				<ul class="nav nav-pills dropdown-menu">
					<li class="nav-item"><a onclick="goCondition('proxkeymatch');" class="nav-link">PROXKEYMATCH</a></li>
				</ul>
			</li>
			
			<li class="nav-item dropdown-toggle"><a class="nav-link">STRING LIST 타입 검색</a>
				<ul class="nav nav-pills dropdown-menu">
					<li class="nav-item"><a onclick="goCondition('allstring');" class="nav-link">ALLSTRING</a></li>
					<li class="nav-item"><a onclick="goCondition('anystring');" class="nav-link">ANYSTRING</a></li>
				</ul>
			</li>
		</ul>
		<c:if test="${params.kwd != null}">
			<p class="nowResult">현재 검색 조건 : ${params.condition}</p>
		</c:if>
	</div>
</c:if>
<c:if test="${boardTotal == 0}">
	<section>
	<h3>게시판</h3>
	<div class="result-box">
		<span style="color: red;">'${params.kwd}'</span> 에 대한 검색결과가 존재하지 않습니다.
	</div>
	</section>
</c:if>
<c:if test="${boardTotal > 0}">
	<section>
		<h3>게시판
		<c:if test="${params.category != 'TOTAL'}">	
			<small><fmt:formatNumber value="${boardTotal}" groupingUsed="true"/>건</small>
		</c:if>
		</h3>
		<ul class="media-list">
		<c:forEach var="result" items="${boardList}" varStatus="status">
		<fmt:parseDate value="${result.regdate}" var="dateFmt" pattern="yyyyMMddHHmmss"/>
	    <li class="media">
	      <div class="media-body">
	        <a class="media-heading"><c:out value="${result.title}"  escapeXml="false"/></a>
	        <span><c:out value="${result.fullpath}"  escapeXml="false"/> | <c:out value="${result.writer}"/> |  <fmt:formatDate value="${dateFmt}"  pattern="yyyy-MM-dd"/></span>
	        <p><c:out value="${result.content}"  escapeXml="false"/></p>
	
			<!-- <c:if test="${not empty result.thumbnail}"> -->
			<!-- 첨부파일 미리보기 -->
		    <!--
		      <div class="media-link">
		      	<ul class="list-inline">
		      		<c:forTokens var="filename" items="${result.thumbnail}" delims="|">
		      			<li><a class="filename"><c:out value="${filename}" escapeXml="false"/></a></li>
			      		<li><span id="" class="preview label label-default">미리보기</span></li>
		      		</c:forTokens>
		      	</ul>
		      </div>
			</c:if>
			-->
	      </div>
	      
			<c:forTokens items = "${result.DOCUMENT}" delims = "##@@##" var = "filebody">
			<%-- <c:forEach items = "${fn:split(result.DOCUMENT, '##@@##')}" var = "filebody"> --%>
				<div class="">
					<div id="preview-dialog" class="preview-dialog modal-dialog collapse">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
								<h6>미리보기</h6>
							</div>
							<div class="modal-body">
								<p><c:out value = "${filebody}" escapeXml="false"/></p>
							</div>
						</div>
					</div>      
				</div>      
			<%-- </c:forEach> --%>      
			</c:forTokens>      
	      
	    </li>
		</c:forEach>
	  </ul>
		<c:if test="${boardTotal > params.pageSize}">
		  <c:choose>
		  	<c:when test="${params.category eq 'TOTAL'}">
		      <div class="text-right more">
		      	<a data-target="BOARD">더보기</a>
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
	<c:if test="${boardTotal  > boardRow}">
		$('#pagination').pagination(<c:out value="${boardTotal}"/>, {
	        current_page: <fmt:formatNumber value="${params.offSet / params.pageSize}" minFractionDigits="0"/>,
	        callback: gotopage
	    });
	</c:if>
	});
	</script>
</c:if>