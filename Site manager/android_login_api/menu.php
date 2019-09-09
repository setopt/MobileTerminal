<?PHP
include("../bdi.php");

	$sql = "SELECT DishOrd.ID_Dish,DishOrd.ID_Ord, Dish.Name as 'Dish', Worker.Name as 'Worker', `Amount` FROM `DishOrd`,`Dish`,`Ord`,`Worker` WHERE Dish.ID_Dish = DishOrd.ID_Dish and DishOrd.ID_Ord = Ord.ID_Ord and Ord.Worker_ID = Worker.ID_Worker and `Preparedness` = 0";
	$result = mysqli_query($db,$sql);
	$response = array();
	$i = 0;
	while($myrow = mysqli_fetch_assoc($result)){
		$response[$i]['id'] = $myrow['ID_Dish'];
		$response[$i]['name'] = $myrow['Name'];
		$response[$i]['value'] = $myrow['Value'];
		$response[$i]['category'] = $myrow['Cat'];
		$response[$i]['kitchen'] = $myrow['Kit'];
		$i++;
		
	}
	echo "<pre>";
	print_r(json_encode($response));
	echo "</pre>";
	
	
?>