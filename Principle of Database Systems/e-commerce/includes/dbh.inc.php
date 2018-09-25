<?php
$dbServername="localhost";
$dbUsername="root";
$dbPassword="12345";
$dbName="ecommence"; 
$conn=mysqli_connect($dbServername,$dbUsername,$dbPassword,$dbName);
if ($conn->connect_error) {
   die("Connection failed: " . $conn->connect_error);
} 
else{
	echo "Connection successful";
}

?>
