<?php 
session_start(); 
if ($_SESSION['login'] ==''){
	header('Location: index.php');
}
include ('bdi.php');


				  //INSERT INTO `Dish`(`Name`, `Value`, `Category_ID`, `Kitchen_ID`) VALUES ('fff',343,(SELECT Category.ID_Category FROM Category WHERE Name = "Пицца"),1)
				 if (isset($_POST['submit'])){
					 $name = $_POST['name'];
					 $value = $_POST['value'];
					 $category = $_POST['category'];
					 $kitchen = $_POST['kitchen'];
					 $id  = $_GET['id_dish'];
					$sqlUpdate = "UPDATE `Dish` SET \n"

					. "`Name`= '$name',\n"

					. "`Value`='$value',\n"

					. "`Category_ID`=(SELECT Category.ID_Category FROM Category Where Category.Name = '$category'),\n"

					. "`Kitchen_ID`=(SELECT Kitchen.ID_Kitchen FROM Kitchen WHERE Kitchen.Name = '$kitchen') \n"

					. "WHERE `ID_Dish` = '$id'";
					 $resultUpdate= mysqli_query($db,$sqlUpdate);
					 
					 header("Location: dish.php");
				  }
				  
				 
				  ?>


<html>
<head>

<link href="css/simple-sidebar.css" rel="stylesheet">
<link href="css/bootstrap.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-expand navbar-dark bg-dark">
      <a class="navbar-brand" href="#">Сайт Менеджера</a>
      <div class="navbar-collapse">
        <ul class="navbar-nav mr-auto">
		<li class="nav-item">
            <a class="nav-link active" href="#">Справочники</a>
          </li>
		  <li class="nav-item">
            <a class="nav-link" href="#">Отчеты</a>
          </li>
        </ul>
        <div class = "col-md-2">
			<p class = "my-md-0 text-right" style = "color: #FFFFFF">Логин: <?php echo $_SESSION['login'];?></p>
		</div>
		<form  class = "form-inline my-md-0" action="index.php">
						<input class="btn" type="submit" value="Выйти">
					</form>
      </div>
    </nav>
		<div id="wrapper" class="toggled">
		<div id ="spigot"> 
		</div>
        <div class = "bg-dark" id="sidebar-wrapper">
            <ul class="sidebar-nav">
                <li>
                    <a style="color: #FFF;" href="dish.php">Блюда</a>
                </li>
                  <li>
                    <a href="category.php">Категории</a>
                </li>
                <li>
                    <a href="kitchen.php">Цеха</a>
                </li>
                <li>
                    <a href="worker.php">Работники</a>
                </li>
                <li>
                    <a href="position.php">Должности</a>
                </li>
            </ul>
        </div>
        <?PHP 
			 if (isset($_GET['id_dish'])){
					$id_dish = $_GET['id_dish'];
					
					$sql = "SELECT `ID_Dish`, Dish.Name, `Value`, Category.Name as 'Category', Kitchen.Name as 'Kitchen' \n"

					. "FROM `Dish`,`Kitchen`,`Category` \n"

					. "WHERE Dish.Category_ID = Category.ID_Category \n"

					. "AND Dish.Kitchen_ID = Kitchen.ID_Kitchen\n"

					. "AND Dish.ID_Dish = '$id_dish'";
					//$sql = "SELECT * FROM Dish WHERE ID_Dish = '$id_dish'";
					$result = mysqli_query($db,$sql);
					$myrow = mysqli_fetch_assoc($result);
					
				  }
		
		?>
        <div id="page-content-wrapper">
            <div class="container-fluid">
				    <form action = "#" method="post" class="form-signin">
				    <h2 class="form-signin-heading" >Редактировать</h2>
				    <p>
							<label>Наименование:<br>
								<input class="form-control" size="50" maxlength="50" name="name" type="text" value = "<?PHP echo $myrow['Name'];?>" required></label>
					
							<label>Стоимость:<br>
								<input class="form-control" size="30" maxlength="50" name="value" type="text" value = "<?PHP echo $myrow['Value'];?>" required></label>
					</p>
					
					<p><label>Категория:<br>
								<select style="width:170;" class="form-control" name="category" type="text"  required>
									<?
										$sqlCat = "SELECT * FROM `Category`";
										$resultCat = mysqli_query($db,$sqlCat);
										while ($myrowCat = mysqli_fetch_assoc($resultCat)){
											
											if($myrowCat['Name'] == $myrow['Category']){
												echo "<option selected>".$myrowCat['Name']."</option>";
											}
											else{
												echo "<option>".$myrowCat['Name']."</option>";
											}
										}
									?>
								</select>
							</label>
					
					<label>Цех:<br>
								<select style="width:170;" class="form-control" name="kitchen" type="text" required>
									<?
										$sqlKit = "SELECT * FROM `Kitchen`";
										$resultKit = mysqli_query($db,$sqlKit);
										while ($myrowKit = mysqli_fetch_assoc($resultKit)){
											if($myrowKit['Name'] == $myrow['Kitchen']){
												echo "<option selected>".$myrowKit['Name']."</option>";
											}
											else{
												echo "<option>".$myrowKit['Name']."</option>";
											}
										}
									?>
								</select>
							</label> </p>
					
					<p>
							<input style="width:455px" class="btn btn-lg btn-dark" type="submit" name="submit" value="Редактировать">
							</p>
					
				  </form>
				  
				  
            </div>
        </div>
    </div>
	</div>
</body>
</html>