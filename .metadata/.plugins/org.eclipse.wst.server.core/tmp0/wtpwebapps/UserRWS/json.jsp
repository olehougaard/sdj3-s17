<%@page import="dk.via.user.User"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/json; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<% 
List<User> users = (List<User>) request.getAttribute("users");
if (users.size() == 1) { 
    User user = users.get(0); %>
	{
		"id":<%= user.getId() %>,
	    "name":<%= user.getName() %>,
	    "email":<%= user.getEmail() %>
	}
<% 
} else { 
%>
	[
<% 
	for(User user: users) { 
%>
		{
			"id":<%= user.getId() %>,
		    "name":<%= user.getName() %>,
		    "email":<%= user.getEmail() %>
		},
<% 
	} 
%>
]
<% 
} 
%>
