<?php 
session_start(); 
if ($_SESSION['login'] ==''){
	header('Location: index.php');
}
include ('bdi.php');


				 if (isset($_POST['submit'])){
					 $name = $_POST['name'];
					 $sqlInsert = "INSERT INTO `Kitchen`(`Name`) VALUES ('$name')";
					 $resultInsert= mysqli_query($db,$sqlInsert);
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
                    <a href="dish.php">Блюда</a>
                </li>
                <li>
                    <a href="category.php">Категории</a>
                </li>
                <li>
                    <a  style="color: #FFF;" href="kitchen.php">Цеха</a>
                </li>
                <li>
                    <a href="worker.php">Работники</a>
                </li>
                <li>
                    <a href="position.php">Должности</a>
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
					  <th>Наименование</th>
					  <th></th>
					</tr>
				  </thead>
				  <tbody>
				  <?php
							$sql = "SELECT `ID_Kitchen`, `Name` FROM `Kitchen`";
							//$date = $_POST['dateV'];
							//$position = $_POST['position'];
							$result = mysqli_query($db,$sql);
							$i = 1;
							while ($myrow = mysqli_fetch_assoc($result)){
								$id = $myrow['ID_Kitchen'];
								echo $doc = "<tr><th scope='row'>".$i."</th><td>".$myrow["Name"]."</td><td><a href = ''>Редактировать</a><br /><a href ='scriptDel.php?table=Kitchen&id=$id&idname=ID_Kitchen'>Удалить</a></td></tr> ";
							$i++;
							}
							?>
				    </tbody>
					
					
				    </table>
				    <form action = "#" method="post" class="form-signin">
				    <h2 class="form-signin-heading" >Добавить</h2>
				    <p>
							<label>Наименование:<br>
								<input class="form-control" size="50" maxlength="50" name="name" type="text" required></label>
					</p>
					
					<p>
							<input style="width:455px" class="btn btn-lg btn-dark" type="submit" name="submit" value="Добавить">
							</p>
					
				  </form>
				  
				  
            </div>
        </div>
        <!-- /#page-content-wrapper -->

    </div>
	</div>
</body>
</html>