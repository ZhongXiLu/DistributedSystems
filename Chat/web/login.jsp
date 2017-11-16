<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<html>
    
    <head>
        <title>Log In</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        
        <jsp:include page="includes.html"></jsp:include>
    </head>
    
    <body>
        <jsp:include page="navbar.html"></jsp:include>
        
        <div class="container">    
            <div style="margin-top:50px;" class="mainbox col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">                    
                <div class="panel panel-info" >
                    <div class="panel-heading">
                        <div class="panel-title">Log In</div>
                    </div>

                    <div style="padding-top:30px" class="panel-body" >

                        <div style="display:none" id="login-alert" class="alert alert-danger col-sm-12"></div>

                        <form id="loginform" class="form-horizontal" role="form" method="POST" action="./ChatUserServlet?action=login">

                            <div style="margin-bottom: 25px" class="input-group">
                                <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
                                <input id="login-username" type="text" class="form-control" name="username" value="" placeholder="Username">                                        
                            </div>
                            <div style="margin-bottom: 25px" class="input-group">
                                <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
                                <input id="login-password" type="password" class="form-control" name="password" placeholder="Password">
                            </div>

                            <div style="margin-top:10px" class="form-group">
                                <div class="col-sm-12 controls">
                                    <a id="btn-login" href="" class="btn btn-success">Login</a>
                                </div>
                            </div>


                            <div class="form-group">
                                <div class="col-md-12 control">
                                    <div style="border-top: 1px solid#888; padding-top:15px" >
                                        Don't have an account ?
                                        <a href="WebsiteServlet?link=register">Sign Up</a>
                                    </div>
                                </div>
                            </div>    
                        </form>     



                    </div>                     
                </div>  
            </div>
        </div>

    </body>
</html>
