<?xml version="1.0" encoding="UTF-8"?>
<%@page contentType="application/xml" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<data>
	<table class="table table-hover">
		<c:forEach items="${onlineUsers}" var="onlineUser">
			<c:choose>
				<c:when test="${onlineUser.getName() == cookie.username.value}">
					<tr class="success">
						<td class="active">
                            <c:if test="${cookie.isModerator.value}">
                                <span style="font-size: 16px; margin-right: 5px" class="glyphicon glyphicon-wrench" title="Moderator"></span>
                            </c:if>
							${onlineUser.getName()}
						</td>
						<td>
						<c:if test="${cookie.isModerator.value}">
						<a target="blank" href="MessageServlet?action=getUserInfo&amp;user=${onlineUser.getName()}" style="float: right"><span style="font-size: 16px" class="glyphicon glyphicon-info-sign" title="Chat Logs"></span></a>
						</c:if>
						</td>
					</tr>
				</c:when>
				<c:otherwise>
					<tr>
						<td 
							data-toggle="modal" data-target="#addInvite" data-user="${onlineUser.getName()}"
							class="openInviteModal">
                            <c:if test="${onlineUser.getIsModerator()}">
                                <span style="font-size: 16px; margin-right: 5px" class="glyphicon glyphicon-wrench" title="Moderator"></span>
                            </c:if>
                            ${onlineUser.getName()}
						</td>
						<td>
						<c:if test="${cookie.isModerator.value}">
						<a target="blank" href="MessageServlet?action=getUserInfo&amp;user=${onlineUser.getName()}" style="float: right"><span style="font-size: 16px" class="glyphicon glyphicon-info-sign" title="Chat Logs"></span></a>
						</c:if>
						</td>
					</tr>
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</table>
</data>