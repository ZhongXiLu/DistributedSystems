<?xml version="1.0" encoding="UTF-8"?>
<%@page contentType="application/xml" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<data>
	<ul class="list-group">
		<c:forEach items="${publicChannels}" var="channel">
			<c:choose>
				<c:when test="${channel.getName() == myChannel}">
					<button type="button" class="list-group-item list-group-item-success">${myChannel}</button>
				</c:when>
				<c:otherwise>
					<button type="button" class="list-group-item">${channel.getName()}</button>
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</ul>
</data>