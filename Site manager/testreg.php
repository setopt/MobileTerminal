<?php
    session_start();
	
	if (isset($_POST['login'])) { 
		$login = $_POST['login']; 	
		
			if ($login == '') { 
				unset($login);} 
			
			} 
	if (isset($_POST['password'])) { $password=$_POST['password']; if ($password =='') { unset($password);} }
   
   if (empty($login) or empty($password)) 
		{
		exit ("Вы ввели не всю информацию, вернитесь назад и заполните все поля!");
		}
	
   
    $login = stripslashes($login);
    $login = htmlspecialchars($login);
	$password = stripslashes($password);
    $password = htmlspecialchars($password);

    $login = trim($login);
    $password = trim($password);


    include ("bdi.php");
 
	$result = mysqli_query($db,"SELECT * FROM `Worker` WHERE Login='$login'"); 
    $myrow = mysqli_fetch_assoc($result);
	//if (!empty($_SESSION['login'])){
		if (empty($myrow['Password']))
		{
			exit ("Извините, введённый вами login или пароль неверный.");
		}
		else {
		
			if($myrow['Position_ID'] == 3){
				if ($myrow['Password']==$password) {
					$_SESSION['login']=$myrow['Login']; 
					$_SESSION['name']=$myrow['Name']; 
					$_SESSION['id_users']=$myrow['ID_Worker'];

						if ($myrow['Login'] =="admin" and $myrow['Password'] = "admin"){
							header('Location: dish.php');
						}
						else {
							header('Location: dish.php');
						}
					}
				 else {
					exit ("Извините, введённый вами login или пароль неверный.");
				}
			}
			else {exit ("У вас нет доступа к сайту"); }
	}
	//}
?>



