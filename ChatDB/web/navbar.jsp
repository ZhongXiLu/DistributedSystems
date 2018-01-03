<%-- 
    Document   : navbar
    Created on : Nov 20, 2017, 11:25:10 AM
    Author     : zhongxilu
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="EntityClasses.ChatUser"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<style>
	@media (min-width: 768px) {
		.navbar-nav.navbar-center {
			position: absolute;
			left: 50%;
			transform: translatex(-50%);
		}
	}
</style>

<% ChatUser user = (ChatUser)session.getAttribute("user"); %>
<nav class="navbar navbar-default">
	<div class="container-fluid">

		<div class="navbar-header">
			<a class="navbar-brand" href="WebsiteServlet?link=index">Chat!</a>
		</div>
		<c:choose>
			<c:when test="${user != null}">
				<ul class="nav navbar-nav navbar-center">
					<li><a id="time">00:00:00</a></li>
				</ul>
				<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
					<a class="dropdown-toggle pull-right" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">${sessionScope.username} <span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a href="UserServlet?action=logout">Log Out</a></li>
					</ul>
				</li>
				</ul>
			</c:when>
		</c:choose>
	</div>
</nav>
