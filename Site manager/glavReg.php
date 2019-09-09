<?php 
session_start(); 
if ($_SESSION['login'] ==''){
	header('Location: index.php');
}
include ('bdi.php');
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
        <div class = "bg-dark" id="sidebar-wrapper">
            <ul class="sidebar-nav">
                <li>
                    <a style="color: #FFF;" href="#">Блюда</a>
                </li>
                <li>
                    <a href="#">Категории</a>
                </li>
                <li>
                    <a href="#">Цеха</a>
                </li>
                <li>
                    <a href="#">Работники</a>
                </li>
                <li>
                    <a href="#">Должности</a>
                </li>
            </ul>
        </div>
        <!-- /#sidebar-wrapper -->

        <!-- Page Content -->
        <div id="page-content-wrapper">
            <div class="container-fluid">
			
               <table class="table">
				  <thead class="thead-default">
					<tr>
					  <th>#</th>
					  <th>Название</th>
					  <th>Стоимость</th>
					  <th>Категория</th>
					  <th>Цех</th>
					  <th></th>
					</tr>
				  </thead>
				  <tbody>
				  <?php
							$sql = "SELECT Dish.ID_Dish, Dish.Name, Dish.Value, Category.Name AS 'CatName', Kitchen.Name AS 'KitName' FROM `Dish`,`Kitchen`,`Category` WHERE Dish.Category_ID = Category.ID_Category and Dish.Kitchen_ID = Kitchen.ID_Kitchen";
							//$date = $_POST['dateV'];
							//$position = $_POST['position'];
							$result = mysqli_query($db,$sql);
							$i = 1;
							while ($myrow = mysqli_fetch_assoc($result)){
								$id = $myrow['ID_Dish'];
								echo $doc = "<tr><th scope='row'>".$i."</th><td>".$myrow["Name"]."</td><td>".$myrow["Value"]."</td><td>".$myrow["CatName"]."</td><td>".$myrow["KitName"]."</td><td><a href = ''>Редактировать</a><br /><a href ='scriptDel.php?table=Dish&id=$id'>Удалить</a></td></tr> ";
							$i++;
							}
							?>
				  </tbody>
            </div>
        </div>
        <!-- /#page-content-wrapper -->

    </div>
	</div>
</body>
</html>