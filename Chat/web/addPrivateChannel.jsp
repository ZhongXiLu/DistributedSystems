	<!--Modal for adding private Channel-->
	<div class="modal fade" id="addPrivateChannel" tabindex="-1" role="dialog" aria-labelledby="addPrivateChannelLabel">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="addPrivateChannelLabel">Add New Private Channel</h4>
				</div>
				<div class="modal-body">
					<form id="addPrivateChannelForm" class="form-horizontal" role="form" method="POST" action="">
						<!-- <c:choose>
							<c:when test="${errorMessage != null}">
								<div id="signupalert" class="alert alert-danger">
									<p>Error: ${requestScope.errorMessage}</p>
									<span></span>
								</div>	
							</c:when>
						</c:choose> -->

						<div class="form-group">
							<label for="privateChannelName" class="col-md-3 control-label">Channel Name:</label>
							<div class="col-md-9">
								<input id="privateChannelName" type="text" class="form-control" name="privateChannelName">
							</div>
						</div>


						<div class="form-group">
							<div class="col-md-offset-3 col-md-9">
								<button id="addPrivateChannelForm" name="AddPrivateChannel" class="btn btn-info">Submit</button>
							</div>
						</div>

					</form>
				</div>
			</div>
		</div>
	</div>
	
	<script type="text/javascript">
		// Pass data to modal and create unique link to servlet
		$(document).on("click", ".openPrivateChannelModal", function () {
			$("#addPrivateChannelForm").attr("action", "ChannelServlet?action=createPrivateChannel&user=" + $(this).data("user"));
		});
	</script>