<?PHP
include("../bdi.php");

if (isset($_POST['id_ord'])){
	
	$id_ord = $_POST['id_ord'];
	
	$sql = "SELECT Dish.Name, `Amount`, Dish.Value  \n"

    . "FROM `DishOrd`,`Dish` \n"

    . "WHERE DishOrd.ID_Dish = Dish.ID_Dish \n"

    . "AND DishOrd.ID_Ord = $id_ord";
	
	$result = mysqli_query($db,$sql);
	
	$response = array();
	
	$i = 0;
		while($myrow = mysqli_fetch_assoc($result)){
			
			$response['ord'][$i]["name"] = $myrow['Name'];
			
			$response['ord'][$i]["amount"] = $myrow['Amount'];
			
			$response['ord'][$i]["value"] = $myrow['Value'];
			
			$i++;
		}
	
	
	echo json_encode($response);
	}
 
?>