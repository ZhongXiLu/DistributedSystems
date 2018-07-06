<?xml version="1.0" encoding="UTF-8"?>
<%@page contentType="application/xml" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<data>
	<c:choose>
		<c:when test="${!myChannel.getIsPublic()}">
			<h4>Private Channel</h4>
			<table class="table table-hover">
				<tr class="success">
					<td>${myChannel.getName()}</td>
				</tr>
			</table>
		</c:when>
	</c:choose>
			
	<h4>
		Public Channels
		<c:if test="${user.getIsModerator()}">
			<a data-toggle="modal" data-target="#addChannel"><span class="glyphicon glyphicon-plus" title="Add Channel"></span></a>
		</c:if>
	</h4>
	
	<table class="table table-hover">
		<c:forEach items="${publicChannels}" var="channel">
			<c:choose>
				<c:when test="${channel.getName() == myChannel.getName()}">
					<tr class="success">
						<td onclick="location.href='ChannelServlet?action=joinChannel&amp;channelName=${channel.getName()}'">
							${myChannel.getName()}
						</td>
						<td>
						<c:if test="${user.getIsModerator() && channel.getName() != 'Welcome'}">
							<a href="ChannelServlet?action=deleteChannel&amp;channelName=${channel.getName()}" style="float: right"><span style="font-size: 16px" class="glyphicon glyphicon-remove" title="Remove Channel"></span></a>
						</c:if>
						</td>
					</tr>
				</c:when>
				<c:otherwise>
					<tr>
						<td onclick="location.href='ChannelServlet?action=joinChannel&amp;channelName=${channel.getName()}'">
							${channel.getName()}
						</td>
						<td>
						<c:if test="${user.getIsModerator() && channel.getName() != 'Welcome'}">
							<a href="ChannelServlet?action=deleteChannel&amp;channelName=${channel.getName()}" style="float: right"><span style="font-size: 16px" class="glyphicon glyphicon-remove" title="Remove Channel"></span></a>
						</c:if>
						</td>
					</tr>
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</table>
</data>