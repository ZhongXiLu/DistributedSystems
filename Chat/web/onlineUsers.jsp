<?xml version="1.0" encoding="UTF-8"?>
<%@page contentType="application/xml" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<data>
	<ul class="list-group">
		<c:forEach items="${onlineUsers}" var="user">
			<li class="list-group-item">${user.getName()}</li>
		</c:forEach>
	  <!--<li style="color:red;" class="list-group-item">User 2</li>-->
	</ul>
</data>