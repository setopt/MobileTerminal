<?PHP
	include("../bdi.php");
	
	if (isset($_POST['id_dish']) && isset($_POST['id_ord'])){
    
		$id_dish = $_POST['id_dish'];
		
		$id_ord = $_POST['id_ord']);
		
		$sql = "UPDATE `DishOrd` SET `Delivered`= 1 WHERE DishOrd.ID_Dish = '$id_dish' AND DishOrd.ID_Ord = '$id_ord'";
		
		$result = mysqli_query($db,$sql);
		$response = array();
		
		if($result){
			$response['error'] = false;
		}
		
		echo json_encode($response);
		
	}


?>