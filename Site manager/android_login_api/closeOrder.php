<?PHP
include("../bdi.php");

if (isset($_POST['id_ord'])){
	
	$DateTime = date("Y-m-d H:i:s");
	
	$id = $_POST['id_ord'];
	
	$sql = "UPDATE `Ord` SET `DateTimePay`= '$DateTime' WHERE Ord.ID_Ord = $id";
	
	$result = mysqli_query($db,$sql);
	
	$response = array();
	
	if($result){
		$response['error'] = false;
	}
	
	
	echo json_encode($response);
	}
 
?>