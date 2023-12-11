<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@page import="dk.via.user.User"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/xml; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<% 
List<User> users = (List<User>) request.getAttribute("users");
if (users.size() == 1) { 
    User user = users.get(0); %>
	<user>
		<id><%= user.getId() %></id>
	    <name><%= user.getName() %></name>
	    <email><%= user.getEmail() %></email>
	</user>
<% 
} else { 
%>
	<users>
<% 
	for(User user: users) { 
%>
		<user>
		    <id><%= user.getId() %></id>
		    <name><%= user.getName() %></name>
		    <email><%= user.getEmail() %></email>
		</user>
<% 
	} 
%>
</users>
<% 
} 
%>
