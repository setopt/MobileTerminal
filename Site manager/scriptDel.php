<?PHP //session_start();
include ('bdi.php');
$id= $_GET['id'];
$table = $_GET['table'];
$idName = $_GET['idname'];
$sql = "DELETE FROM `$table` WHERE $idName = $id";

$result = mysqli_query($db,$sql);
if ($result == true){
	header("Location: $table.php");
}
else if ($result == false){
	echo "Ошибка";
	echo "<br />";
	echo "<a href = '$table.php'>Вернутся</a>";
}
?>