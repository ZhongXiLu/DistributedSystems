<%-- 
    Document   : chat
    Created on : Nov 12, 2017, 4:21:03 PM
    Author     : zhongxilu
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="includes.html"></jsp:include>
</head>

<body>

    <jsp:include page="navbar.jsp"></jsp:include>

    <table class="table">
        <tr>
            <td class="col-md-2">
                <h4>Private Channel</h4>
                <ul class="list-group">
                  <button type="button" class="list-group-item">My Private Channel</button>
                </ul>
                <h4>Public Channels</h4>
				<div id="publicChannels"></div>
            </td>
            <td class="col-md-8">
                <h4>Chat</h4>
                <div class="panel panel-default">
                  <div class="panel-body">
                        <p>User 1: Hello World!</p>
                        <p>User 1: Hello World!</p>
                        <p>User 1: Hello World!</p>
                        <p>User 1: Hello World!</p>
                        <p>User 1: Hello World!</p>
                        <p>User 1: Hello World!</p>
                        <p>User 1: Hello World!</p>
                        <p style="color:red;">User 2: Stop with spamming!</p>
                        <p>User 1: Hello World!</p>
                        <p>User 1: Hello World!</p>
                        <p><i>User 2 has banned User 1 from this channel.</i></p>
                  </div>
                </div>
                <div class="input-group">
              <input type="text" class="form-control">
              <span class="input-group-btn">
                <button class="btn btn-default" type="button">Send</button>
              </span>
                </div>
            </td>
            <td class="col-md-2">
                <h4>Online Users</h4>
				<div id="onlineUsers"></div>
            </td>
        </tr>
    </table>
    
    <script type="text/javascript">
		function update() {
			$.get("UserServlet", {"action": "getOnlineUsers"}, function(responseXml) {
				$("#onlineUsers").html($(responseXml).find("data").html());
			});
			$.get("ChannelServlet", {"action": "getPublicChannels"}, function(responseXml) {
				$("#publicChannels").html($(responseXml).find("data").html());
			});
			setTimeout(update, 1000);
		}
		
		$(document).ready(function() {
			update();
		});
    </script>
    
</body>


</html>