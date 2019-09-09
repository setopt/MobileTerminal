<?PHP 
	include("../bdi.php");
	
	if(isset($_POST['id_dish']) && isset($_POST['id_ord'])){
		$id_dish = $_POST['id_dish'];
		$id_ord = $_POST['id_ord'];
		$sql = "UPDATE `DishOrd` SET `Preparedness`= 1 WHERE `ID_Dish` = '$id_dish' and `ID_Ord` = '$id_ord'";
		
		$result = mysqli_query($db,$sql);
		
		$response = array();
		
		$response["result"] = $result;
		
		echo json_encode($response);
	
	}

?>