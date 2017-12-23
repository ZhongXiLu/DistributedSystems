<?xml version="1.0" encoding="UTF-8"?>
<%@page contentType="application/xml" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<data>
	<c:choose>
		<c:when test="${!myChannel.getIsPublic()}">
			<h4>Private Channel</h4>
			<ul class="list-group">
				<li class="list-group-item">${myChannel.getName()}</li>
			</ul>
		</c:when>
	</c:choose>
			
	<h4>
		Public Channels
		<!--if moderator -->
		<a data-toggle="modal" data-target="#addChannel"><span class="glyphicon glyphicon-plus"></span></a>
		<!--endif-->
	</h4>
	
	<ul class="list-group">
		<c:forEach items="${publicChannels}" var="channel">
			<c:choose>
				<c:when test="${channel.getName() == myChannel.getName()}">
					<button onclick="location.href='ChannelServlet?action=joinChannel&amp;channelName=${channel.getName()}'" type="button" class="list-group-item list-group-item-success">
						${myChannel.getName()}
						<!--if moderator -->
						<a href="ChannelServlet?action=deleteChannel&amp;channelName=${channel.getName()}" style="float: right"><span style="font-size: 16px" class="glyphicon glyphicon-trash"></span></a>
						<!--endif-->
					</button>
				</c:when>
				<c:otherwise>
					<button onclick="location.href='ChannelServlet?action=joinChannel&amp;channelName=${channel.getName()}'" type="button" class="list-group-item">
						${channel.getName()}
						<!--if moderator -->
						<a href="ChannelServlet?action=deleteChannel&amp;channelName=${channel.getName()}" style="float: right"><span style="font-size: 16px" class="glyphicon glyphicon-trash"></span></a>
						<!--endif-->
					</button>
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</ul>
</data>