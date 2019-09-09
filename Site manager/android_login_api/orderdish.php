<?PHP
include("../bdi.php");
if(isset($_POST['dish']) && isset($_POST['count'])&& isset($_POST['id_order'])){
	$dish = $_POST['dish'];
	$count = $_POST['count'];
	$id_order = $_POST['id_order'];
	
	$sqlDish = "SELECT `ID_Dish` FROM `Dish` WHERE `Name` = '$dish'";
	$resultDish = mysqli_query($db,$sqlDish);
	$myrowDish = mysqli_fetch_assoc($resultDish);
	
	$id_dish = $myrowDish['ID_Dish'];
	
	$sql = "INSERT INTO `DishOrd`(`ID_Dish`, `ID_Ord`, `Amount`, `Preparedness`) VALUES ('$id_dish','$id_order','$count',0)";
	$result = mysqli_query($db,$sql);
	
	$response = array();
	
	$response['result'] = $result;
	$response['dish'] = $dish
	$response['count'] = $count;
	$response['id_order'] = $id_order;
	echo json_encode($response);
	
}
 
?>