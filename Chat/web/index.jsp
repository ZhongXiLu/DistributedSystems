<%-- 
    Document   : index
    Created on : Nov 12, 2017, 3:32:21 PM
    Author     : zhongxilu
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="EntityClasses.ChatUser"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Index</title>
        
        <jsp:include page="includes.html"></jsp:include>
    </head>
    <body>
        <jsp:include page="navbar.jsp"></jsp:include>

        <% ChatUser user = (ChatUser)session.getAttribute("user"); %>
        <c:choose>
            <c:when test="${empty user}">
                <jsp:forward page="WebsiteServlet?link=login" />
            </c:when>
            <c:otherwise>
                <jsp:forward page="WebsiteServlet?link=chat" />
            </c:otherwise>
        </c:choose>

    </body>
</html>
