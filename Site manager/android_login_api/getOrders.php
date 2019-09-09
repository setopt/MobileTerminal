<?PHP
include("../bdi.php");

if (isset($_POST['name'])){
	
	$name = $_POST['name'];
	
	$sql = "SELECT Ord.ID_Ord, `DateTimeOrder`, `DateTimePay`, `TableNumb`, SUM(Dish.Value) as 'sum'\n"

    . "FROM `Ord`,`Worker`, `DishOrd`,`Dish`\n"

    . "WHERE Ord.Worker_ID = Worker.ID_Worker \n"

    . "AND Ord.DateTimePay IS NULL \n"

    . "AND Worker.Name = '$name' \n"
	
	. "AND DishOrd.ID_Dish = Dish.ID_Dish\n"

    . "AND DishOrd.ID_Ord = Ord.ID_Ord\n"

    . "GROUP BY Ord.ID_Ord";
	
	$result = mysqli_query($db,$sql);
	
	$response = array();
	
	$i = 0;
		while($myrow = mysqli_fetch_assoc($result)){
			
			$response['ord'][$i]["id"] = $myrow['ID_Ord'];
			
			$response['ord'][$i]["table_numb"] = $myrow['TableNumb'];
			
			$response['ord'][$i]["sum"] = $myrow['sum'];
			
			$i++;
		}
	
	
	echo json_encode($response);
	}
 
?>