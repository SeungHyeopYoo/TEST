<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="alert alert-danger" role="alert">
	<p><c:out value="${error.message}"/></p>
</div>