<?PHP 
include("../bdi.php");
if (isset($_POST['login']) && isset($_POST['password'])){
	$login = $_POST['login'];
	//$login = "login1";
	
	$password = $_POST['password'];
	
	//$password = "password1";
	
	$login = trim($login);
    $password = trim($password);
	
	$sql = "SELECT * FROM `Worker` WHERE `Login` = '$login'";
	
	$result = mysqli_query($db,$sql);
	$myrow = mysqli_fetch_assoc($result);
	$response = array("error" => FALSE);
	
	if ($myrow['Password']==$password){
		$response['error'] = false;
		$response['uid'] = $myrow['ID_Worker'];
		$response['user']['Login'] = $myrow['Login'];
		$response['user']['Name'] = $myrow['Name'];
		$response['user']['Position'] = $myrow['Position_ID'];
		
		echo json_encode($response);
	}
	else {
		$response['error'] = true;
		$response["error_msg"] = "Unknown error occurred in registration!";
		echo json_encode($response);
		
	}
}



?>