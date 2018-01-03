<?xml version="1.0" encoding="UTF-8"?>
<%@page contentType="application/xml" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<data>
	<table class="table table-hover">
		<c:forEach items="${onlineUsers}" var="user">
			<c:choose>
				<c:when test="${user.getName() == sessionScope.username}">
					<tr class="success">
						<td class="active">
							${user.getName()}
						</td>
						<%--<c:if test="${user.getIsModerator()}">--%>
						<td><a target="blank" href="MessageServlet?action=getUserInfo&amp;user=${user.getName()}" style="float: right"><span style="font-size: 16px" class="glyphicon glyphicon-info-sign"></span></a></td>
						<%--</c:if>--%>
					</tr>
				</c:when>
				<c:otherwise>
					<tr 
					<c:if test="${user.getIsModerator()}">
					class="danger"
					</c:if>
					>
						<td 
							data-toggle="modal" data-target="#addInvite" data-user="${user.getName()}"
							class="openInviteModal">
							${user.getName()}
						</td>
						<%--<c:if test="${user.getIsModerator()}">--%>
						<td><a target="blank" href="MessageServlet?action=getUserInfo&amp;user=${user.getName()}" style="float: right"><span style="font-size: 16px" class="glyphicon glyphicon-info-sign"></span></a></td>
						<%--</c:if>--%>
					</tr>
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</table>
</data>