<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<c:forEach var="result" items="${imageList}" varStatus="status">
      <img data-link="<c:out value='${result.link}'/>" data-src="<c:out value='${result.thumbnail}'/>" data-width="<c:out value='${result.sizewidth}'/>" data-height="<c:out value='${result.sizeheight}'/>"
      />
  </c:forEach>
  