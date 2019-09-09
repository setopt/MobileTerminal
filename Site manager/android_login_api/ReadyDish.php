<?PHP
	include("../bdi.php");
	
	//if (isset($_POST['name'])){
    
		$dateEnd = date("Y-m-d H:i:s");
		$dateBegin = date("Y-m-d H:i:s",(time() - (12*60*60)));
		$name = $_POST['name']);
		
		$sql = "SELECT Dish.Name as 'Dish',Ord.ID_Ord as 'Ord', Ord.TableNumb as 'TableNumb' \n"

		. "FROM `Ord`,`Worker`,`Dish`,`DishOrd` \n"

		. "WHERE DishOrd.ID_Dish = Dish.ID_Dish \n"

		. "AND Ord.Worker_ID = Worker.ID_Worker \n"

		. "AND DishOrd.ID_Ord = Ord.ID_Ord \n"

		. "AND DishOrd.Preparedness = 1 \n"

		. "AND Ord.DateTimePay IS NULL \n"

		//. "AND Ord.DateTimeOrder < '$dateEnd' \n"

		. "AND Ord.DateTimeOrder > '$dateBegin'\n"

		. "AND Worker.Name = '$name'";
		
		$result = mysqli_query($db,$sql);
		$response = array();
		$i = 0;
		while($myrow = mysqli_fetch_assoc($result)){
			
			$response[$i]["dish"] = $myrow['Dish'];
			
			$response[$i]["ord"] = $myrow['Ord'];
			
			$response[$i]["table_numb"] = $myrow['TableNumb'];
			
			$i++;
		}
		
		echo json_encode($response);
		
	//}


?>