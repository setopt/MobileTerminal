<?PHP
include("../bdi.php");

	$sql = "SELECT DishOrd.ID_Dish,DishOrd.ID_Ord, Dish.Name as \"Dish\", Worker.Name as \"Worker\", `Amount`, Kitchen.Name as \"Kitchen\" \n"

    . "FROM `DishOrd`,`Dish`,`Ord`,`Worker`,`Kitchen` \n"

    . "WHERE Dish.ID_Dish = DishOrd.ID_Dish and\n"

    . "DishOrd.ID_Ord = Ord.ID_Ord and \n"

    . "Ord.Worker_ID = Worker.ID_Worker and \n"

    . "Dish.Kitchen_ID = Kitchen.ID_Kitchen and `Preparedness` = 0";
	$result = mysqli_query($db,$sql);
	//$myrow = mysqli_fetch_assoc($result);
	$response = array();
	$i = 0;
	while($myrow = mysqli_fetch_assoc($result)){
		$response[$i]['id_dish'] = $myrow['ID_Dish'];
		$response[$i]['id_ord'] = $myrow['ID_Ord'];
		$response[$i]['dish'] = $myrow['Dish'];
		$response[$i]['worker'] = $myrow['Worker'];
		$response[$i]['count'] = $myrow['Amount'];
		$response[$i]['kit'] = $myrow['Kitchen'];
		$i++;
	}
	
	echo json_encode($response);
	
	
	

 
?>