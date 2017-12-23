<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="includes.html"></jsp:include>
</head>

<body>

    <jsp:include page="navbar.jsp"></jsp:include>

	<div class="container-fluid">

	<div class="page-header">
		<h1>${username}'s History</h1>
	</div>
	
	<table class="table table-bordered">
		<tr>
			<th>Message</th>
			<th>Channel</th>
			<th>Timestamp</th>
		</tr>

		<c:forEach items="${messages}" var="message">
			<tr>
				<td>${message.getContent()}</td>
				<td>${message.getChannelId().getName()}</td>
				<td>${message.getTimestamp()}</td>
			</tr>
		</c:forEach>

	</table>
	
	</div>
	
</body>

</html>