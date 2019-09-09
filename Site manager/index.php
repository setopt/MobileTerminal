<?php session_start();

 ?>
<!DOCTYPE html>
	<html>
    <head>
    <title>Вход</title>
	<link href="css/bootstrap.min.css" rel="stylesheet">
	<link href="css/signin.css" rel="stylesheet">
	<script src="valid.js"></script>
    </head>
    <body>
  
	<div class ="text-center">
	 
    <form class = "form-signin" action="testreg.php" method="post" class="form-signin">
	<h2 class="form-signin-heading" >Вход</h2>
   
 <p>
    <label>
		<input class="form-control" name="login" type="text" size="50" maxlength="50" placeholder="Login" required ></label>
    </p>
    <p>

    <label>
    <input  class="form-control"name="password" type="password" size="50" maxlength="50" placeholder="Password" required></label>
    </p>


    <p>
    <input style="width:420px" class="btn btn-secondary" onClick="return Formdata(this.form)"  type="submit" name="submit" value="Войти">

 
<br>
    </p></form>
    <br>
		
	</div>
	
    </body>
</html>