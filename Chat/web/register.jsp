<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<html>
    
    <head>
        <title>Register</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        
        <jsp:include page="includes.html"></jsp:include>
    </head>
    
    <body>
        <jsp:include page="navbar.jsp"></jsp:include>
                
        <div class="container">    
            <div style="margin-top:50px" class="mainbox col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">
                <div class="panel panel-info">
                    <div class="panel-heading">
                        <div class="panel-title">Sign Up</div>
                        <div style="float:right; font-size: 85%; position: relative; top:-10px"><a id="signinlink" href="WebsiteServlet?link=login">Go back</a></div>
                    </div>  
                    <div class="panel-body" >
                        <form id="signupform" class="form-horizontal" role="form" method="POST" action="ChatUserServlet?action=register">

							<c:choose>
								<c:when test="${errorMessage != null}">
									<div id="signupalert" class="alert alert-danger">
										<p>Error: ${requestScope.errorMessage}</p>
										<span></span>
									</div>	
								</c:when>
							</c:choose>
							
                            <div class="form-group">
                                <label for="username" class="col-md-3 control-label">Username</label>
                                <div class="col-md-9">
                                    <input id="username" type="text" class="form-control" name="username">
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="password" class="col-md-3 control-label">Password</label>
                                <div class="col-md-9">
                                    <input id="password" type="password" class="form-control" name="password">
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="repassword" class="col-md-3 control-label">Re-enter Password</label>
                                <div class="col-md-9">
                                    <input id="repassword" type="password" class="form-control" name="repassword">
                                </div>
                            </div>
							<div class="form-group">
                                <label for="initialTime" class="col-md-3 control-label">Initial Time</label>
                                <div class="col-md-9">
                                    <input id="initialTime" type="time" class="form-control" name="initialTime">
                                </div>
                            </div>
							<div class="form-group">
                                <label for="driftValue" class="col-md-3 control-label">Drift Value (in sec/min)</label>
                                <div class="col-md-9">
                                    <input id="driftValue" type="number" class="form-control" name="driftValue">
                                </div>
                            </div>
							
                            <div class="form-group">
                                <div class="col-md-offset-3 col-md-9">
                                    <button id="addUser" name="AddUser" class="btn btn-info">Sign Up</button>
                                </div>
                            </div>

                        </form>
                    </div>
                </div>

            </div> 
        </div>

    </body>
</html>
