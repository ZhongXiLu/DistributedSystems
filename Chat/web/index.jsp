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

        <script type="text/javascript">
            $(document).ready(function() {
                // Redirect to login if user is not logged in
                $.get("UserServlet", {"action": "checkLoggedIn"}, function(response) {
                    if(response == "false") {
                        window.location = "WebsiteServlet?link=login";
                    } else {
                        window.location = "WebsiteServlet?link=chat";
                    }
                });
            });
        </script>

    </body>
</html>
