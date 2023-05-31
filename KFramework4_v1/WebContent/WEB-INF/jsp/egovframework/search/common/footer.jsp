<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<footer>
	<div class="container">	
	<form class="form-inline" role="form" style="display:none">
		<input type="text" class="form-control" name="pwd" value="<c:out value='${params.kwd}'/>"/>
		<span class="input-group-btn">
								<button class="btn" type="submit"><i class="fa fa-search"></i></button>
		</span>
	</form>			
	<p>Copyright(c) 2016. Konan Technology Inc. All rights reserved.</p>
		
	</div>
</footer>