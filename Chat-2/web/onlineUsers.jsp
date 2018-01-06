<?xml version="1.0" encoding="UTF-8"?>
<%@page contentType="application/xml" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<data>
	<table class="table table-hover">
		<c:forEach items="${onlineUsers}" var="onlineUser">
			<c:choose>
				<c:when test="${onlineUser.getName() == sessionScope.username}">
					<tr class="success">
						<td class="active">
							${onlineUser.getName()}
						</td>
						<td>
						<c:if test="${user.getIsModerator()}">
						<a target="blank" href="MessageServlet?action=getUserInfo&amp;user=${onlineUser.getName()}" style="float: right"><span style="font-size: 16px" class="glyphicon glyphicon-info-sign"></span></a>
						</c:if>
						</td>
					</tr>
				</c:when>
				<c:otherwise>
					<tr 
					<c:if test="${onlineUser.getIsModerator()}">
					class="danger"
					</c:if>
					>
						<td 
							data-toggle="modal" data-target="#addInvite" data-user="${onlineUser.getName()}"
							class="openInviteModal">
							${onlineUser.getName()}
						</td>
						<td>
						<c:if test="${user.getIsModerator()}">
						<a target="blank" href="MessageServlet?action=getUserInfo&amp;user=${onlineUser.getName()}" style="float: right"><span style="font-size: 16px" class="glyphicon glyphicon-info-sign"></span></a>
						</c:if>
						</td>
					</tr>
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</table>
</data>