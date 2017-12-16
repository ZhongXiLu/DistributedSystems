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
        
				<h4>
					Public Channels
					<!--if moderator -->
					<a data-toggle="modal" data-target="#addChannel"><span class="glyphicon glyphicon-plus"></span></a>
					<!--endif-->
				</h4>
				<jsp:include page="addChannel.jsp"></jsp:include>
				<div id="publicChannels"></div>
            
		</td>
            <td class="col-md-8">
                <h4>Chat</h4>
				<div class="panel panel-default">
				<div class="panel-body" style="height: calc(100vh - 225px); overflow-y: auto;">
					<div id="messages"></div>
				</div>
				</div>
				
                <div class="input-group">
              <input id="message" type="text" class="form-control">
              <span class="input-group-btn">
                <button id="sendMessage" class="btn btn-default" type="button">Send</button>
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
			$.get("MessageServlet", {"action": "getLatestMessages"}, function(responseXml) {
				$("#messages").html($(responseXml).find("data").html());
			});
			setTimeout(update, 1000);
		}
		
		$(document).ready(function() {
			update();
			
			$("#sendMessage").click(function () {
				$.ajax({
					
					url: "MessageServlet?action=sendMessage",
					type: "POST",
					data: {
						message: $("#message").val(),
					},
					success: function() {
						$("#message").val("");
					} 
				});
			});
		});
    </script>
    
</body>


</html>