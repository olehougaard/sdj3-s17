<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<ul>
		<% String parameter = request.getParameter("maxNum");
			int maxNum = Integer.parseInt(parameter);
			for(int i = 1; i <= maxNum; i++) { %> 
		   		<li><%= i %></li>
		 <% } %>
			
	</ul>
</body>
</html>