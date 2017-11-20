<%-- 
    Document   : navbar
    Created on : Nov 20, 2017, 11:25:10 AM
    Author     : zhongxilu
--%>

<%@page import="chat_user.ChatUser"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<% ChatUser user = (ChatUser)session.getAttribute("user"); %>
<nav class="navbar navbar-default">
  <div class="container-fluid">
    <div class="navbar-header">
      <a class="navbar-brand" href="WebsiteServlet?link=index">Chat!</a>
    </div>
    <ul class="nav navbar-nav navbar-right">
        <%
            if(user != null) {
        %>
            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">${sessionScope.username} <span class="caret"></span></a>
                <ul class="dropdown-menu">
                    <li><a href="#">Log Out</a></li>
                </ul>
            </li>
        <%
            }
        %>
    </ul>
  </div>
</nav>
