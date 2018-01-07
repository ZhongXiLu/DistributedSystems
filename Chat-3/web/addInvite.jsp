	<!--Modal for creating an invite -->
	<div class="modal fade" id="addInvite" tabindex="-1" role="dialog" aria-labelledby="addInviteLabel">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="addInviteLabel">Create Invite</h4>
				</div>
				<div class="modal-body">
					<form id="addInviteForm" class="form-horizontal" role="form" method="POST" action="">
						<!-- <c:choose>
							<c:when test="${errorMessage != null}">
								<div id="signupalert" class="alert alert-danger">
									<p>Error: ${requestScope.errorMessage}</p>
									<span></span>
								</div>	
							</c:when>
						</c:choose> -->

						<div class="form-group">
							<label for="channelName" class="col-md-3 control-label">Channel Name:</label>
							<div class="col-md-9">
								<input id="channelName" type="text" class="form-control" name="channelName">
							</div>
						</div>


						<div class="form-group">
							<div class="col-md-offset-3 col-md-9">
								<button id="addInviteForm" name="AddInvite" class="btn btn-info">Submit</button>
							</div>
						</div>

					</form>
				</div>
			</div>
		</div>
	</div>
	
	<script type="text/javascript">
		// Pass data to modal and create unique link to servlet
		$(document).on("click", ".openInviteModal", function () {
			$("#addInviteForm").attr("action", "InviteServlet?action=createInvite&user=" + $(this).data("user"));
		});
	</script>