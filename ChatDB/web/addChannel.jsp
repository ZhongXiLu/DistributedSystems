

<div class="modal fade" id="addChannel" tabindex="-1" role="dialog" aria-labelledby="addChannelLabel">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="addChannelLabel">Add New Channel</h4>
			</div>
			<div class="modal-body">
				<form id="addChannel" class="form-horizontal" role="form" method="POST" action="ChannelServlet?action=addPublicChannel">
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
							<button id="addChannel" name="AddChannel" class="btn btn-info">Submit</button>
						</div>
					</div>

				</form>
			</div>
		</div>
	</div>
</div>
