<%-- 
    Document   : index
    Created on : Nov 12, 2017, 3:32:21 PM
    Author     : zhongxilu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Index</title>
        
        <jsp:include page="includes.html"></jsp:include>
    </head>
    <body>
        <jsp:include page="navbar.html"></jsp:include>
        <h1>Hello World!</h1>
        
        <%
            // Redirect to login if not logged in
            response.sendRedirect("WebsiteServlet?link=login");
            // Else, send to chat application directly
            // ...
        %>
        
    </body>
</html>
