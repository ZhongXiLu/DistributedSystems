
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty invite}">
	<div class="modal fade" id="inviteRequestModal" tabindex="-1" role="dialog" aria-labelledby="addInviteLabel">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="addInviteLabel">Invite</h4>
				</div>

				<div class="modal-body">
					<p>${invite.getFromUser().getName()} has invited you to his/her private channel: ${invite.getName()}</p>

					<button onclick="location.href='InviteServlet?action=acceptInvite&amp;inviteId=${invite.getId()}'" class="btn btn-success">Accept</button>
					<button onclick="location.href = 'InviteServlet?action=declineInvite&amp;inviteId=${invite.getId()}'" class="btn btn-danger">Decline</button>
				</div>
			</div>
		</div>
	</div>
</c:if>