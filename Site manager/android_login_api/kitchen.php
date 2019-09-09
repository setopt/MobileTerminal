<?PHP
include("../bdi.php");

	$sql = "SELECT `Name` FROM `Kitchen`";
	$result = mysqli_query($db,$sql);
	$response = array();
	$i = 0;
	while($myrow = mysqli_fetch_assoc($result)){
		$response[$i]["kitchen"] = $myrow['Name'];
		$i++;
	}
	
	echo json_encode($response);
	
	
	

 
?>