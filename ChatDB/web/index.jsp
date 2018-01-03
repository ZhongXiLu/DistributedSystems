<%-- 
    Document   : index
    Created on : Nov 12, 2017, 3:32:21 PM
    Author     : zhongxilu
--%>

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
        <%
            // Redirect to login if not logged in
            if(user == null) {
                response.sendRedirect("WebsiteServlet?link=login");
            } else {
                response.sendRedirect("WebsiteServlet?link=chat");
            }
        %>
        
    </body>
</html>
