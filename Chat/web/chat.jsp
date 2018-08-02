<%-- 
    Document   : chat
    Created on : Nov 12, 2017, 4:21:03 PM
    Author     : zhongxilu
--%>

<%@page import="EntityClasses.ChatUser"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
				<div id="channels"></div>
				<jsp:include page="addChannel.jsp"></jsp:include>   
				
		</td>
            <td class="col-md-8">
                <h4>Chat</h4>
				<div class="panel panel-default">
				<div id="messageBox" class="panel-body" style="height: calc(100vh - 225px); overflow-y: auto;">
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
				
				<jsp:include page="addInvite.jsp"></jsp:include>
            </td>
        </tr>
    </table>
				
	<div id="inviteRequest"></div>

    <script type="text/javascript">
		function update() {
			$.get("UserServlet", {"action": "getOnlineUsers"}, function(responseXml) {
				$("#onlineUsers").html($(responseXml).find("data").html());
			});
			$.get("ChannelServlet", {"action": "getChannels"}, function(responseXml) {
				$("#channels").html($(responseXml).find("data").html());
			});
			$.get("MessageServlet", {"action": "getLatestMessages"}, function(responseXml) {
				$("#messages").html($(responseXml).find("data").html());
			});
			if(!($("#inviteRequestModal").data('bs.modal') || {}).isShown) {	// check if there's already an invite open on screen
				$.get("InviteServlet", {"action": "getOpenInvite"}, function(responseXml) {
					$("#inviteRequest").html(responseXml);
					$("#inviteRequestModal").modal("show");
				});
			}
			setTimeout(update, 1000);
		}
		
		function refreshTime() {
			var time = ($("#time").text()).split(':');
			var date = new Date();
			date.setHours(time[0]);
			date.setMinutes(time[1]);
			date.setSeconds(time[2]);
			date.setMilliseconds(parseInt(time[3]) + 100 + (parseInt("${cookie.driftValue.value}")/60)*100);
			$("#time").text(date.format("HH:mm:ss:fff"));
			setTimeout(refreshTime, 100);
		}
		
		function syncTime() {
			// Christian algorithm
			var startTime = new Date().getTime();
			$.get("TimeServlet", {"action": "getTime"}, function(responseText) {
				var rtt = new Date().getTime() - startTime;	// in milliseconds
				var time = responseText.split(':');
				var date = new Date();
				date.setHours(time[0]);
				date.setMinutes(time[1]);
				date.setSeconds(time[2]);
				date.setMilliseconds(parseInt(time[3]) + rtt/2);
				$("#time").text(date.format("HH:mm:ss:fff"));
			});
			setTimeout(syncTime, 10000);	// sync time every 10 seconds
		}
		
		$(document).ready(function() {
            // Redirect to login if user is not logged in
            $.get("UserServlet", {"action": "checkLoggedIn"}, function(response) {
                if(response == "false") {
                    window.location = "WebsiteServlet?link=login";
                }
			});
            
			$("#time").text("${cookie.initialTime.value}"+":00");
			refreshTime();
			syncTime();
			
			update();

			$("#sendMessage").click(function () {
				$.ajax({
					
					url: "MessageServlet?action=sendMessage",
					type: "POST",
					data: {
						message: $("#message").val(),
					},
					success: function() {
						$("#message").val("");	// clear input
						$("#messageBox").scrollTop($("#messageBox")[0].scrollHeight);	// scroll to bottom
					} 
				});
			});
		});
		
		// "Enter" triggers submit
		$("#message").keyup(function(event) {
			if(event.keyCode === 13) {
				$("#sendMessage").click();
			}
		});
    </script>
    
</body>


</html>