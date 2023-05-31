<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="row">
	<form id="searchForm" class="form-inline" role="form">
		<div class="hidden-xs col-md-2">
			<a href="search.do"><img src="images/logo.gif"/></a>
		</div>
		<div class="col-md-5 col-xs-12">
			<div class="input-group input-query">
				<input type="text" class="form-control" name="kwd" value="<c:out value='${params.kwd}'/>"/>
				<span class="input-group-btn">
					<button class="btn" type="submit" style="margin-right:-1px;">
						검색
					</button>
				</span>
			</div>
			<div class="kre_wrap">
			</div>
			<div class="proofs_wrap">
			</div>
		</div><!--/.col-md-5-->
		
		<div class="hidden-xs">
			<div class="form-group">
				<label class="btn-sm"><input type="checkbox" name="resrch" value="true"/> 결과 내 재검색</label>
				<button class="btn btn-link btn-sm" type="button" id="detailed-search">상세검색 <span class="caret"></span></button>
			</div>
		</div>
	</form>
</div>