<?PHP
include("../bdi.php");
if(isset($_POST['tableNumb']) && isset($_POST['worker'])){
	//$dish = $_POST['dish'];
	$tableNumb = $_POST["tableNumb"];
	$worker = $_POST['worker'];
	//$count = $_POST['count'];
	$date = date("Y-m-d H:i:s");
	
	$sqlWorker = "SELECT `ID_Worker` FROM `Worker` WHERE Name = '$worker'";
	$resultWorker = mysqli_query($db,$sqlWorker);
	$myrowWorker = mysqli_fetch_assoc($resultWorker);
	$id_worker = $myrowWorker['ID_Worker'];
	
	$sql = "INSERT INTO `Ord`(`DateTimeOrder`, `DateTimePay`, `TableNumb`, `Worker_ID`) VALUES ('$date',null,'$tableNumb','$id_worker')";
	$result = mysqli_query($db,$sql);
	
	$id_order = mysqli_insert_id($db);
	
	$response['id_order'] = $id_order;
	
	echo json_encode($response);
	
	
	
}
 
?>