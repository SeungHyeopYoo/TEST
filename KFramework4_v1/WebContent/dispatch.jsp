<%@ page contentType="text/html"%>
<%@ page import = "javax.servlet.RequestDispatcher,
                   javax.servlet.http.HttpServletResponse,
                   org.apache.commons.lang3.ArrayUtils"
%>
<%!
	private static final String[] VIEWS = { "detailed-search", "preview", "smart-search" };
%>
<%
	String view = request.getParameter("view");
	if (!ArrayUtils.contains(VIEWS, view)) {
		response.sendError(HttpServletResponse.SC_NOT_FOUND);
	} else {
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/egovframework/search/result/" + view + ".jsp");
		rd.forward(request, response);
	}
%>
