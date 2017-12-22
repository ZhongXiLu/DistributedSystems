<?xml version="1.0" encoding="UTF-8"?>
<%@page contentType="application/xml" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<data>
	<ul class="list-group">
		<c:forEach items="${onlineUsers}" var="user">
			<c:choose>
				<c:when test="${user.getName() == sessionScope.username}">
					<li class="list-group-item">${user.getName()}</li>
				</c:when>
				<c:otherwise>
					<button 
						data-toggle="modal" data-target="#addPrivateChannel" data-user="${user.getName()}"
						type="button" class="openPrivateChannelModal list-group-item">
						${user.getName()}
					</button>
				</c:otherwise>
			</c:choose>
		</c:forEach>
	  <!--<li style="color:red;" class="list-group-item">User 2</li>-->
	</ul>
</data>