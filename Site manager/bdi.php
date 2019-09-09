<?php  

$host = 'localhost'; // адрес сервера 
$database = 'terminal'; // имя базы данных
$user = 'root'; // имя пользователя
$passwordd = ''; // пароль

 $db = mysqli_connect ($host,$user,$passwordd,$database);
       
	   
    if (!$db) { 
   printf("Невозможно подключиться к базе данных. Код ошибки: %s\n", mysqli_connect_error()); 

} 

?>