<!DOCTYPE html>
<html>
<head>
<title></title>
	<link rel="stylesheet" type="text/css" href="style.css">
  <style>
  ul {
    list-style-type: none;
    margin: 0;
    padding: 0;
    overflow: hidden;
    background-color: #FF4828;
  }

  li {
    float: left;
  }

  li a {
    display: block;
    color: white;
    text-align: center;
    padding: 14px 16px;
    text-decoration: none;
  }

  li a:hover {
    background-color: #FF551D;
  }
</style>
</head>

<body>

<?php
ob_start();
if(!isset($_SESSION)){
    session_start();
}

if (isset($_SESSION['isCustomerLogin'])){
    echo "<ul>";
    echo '<li><a href="customerLoggedin.php">Home</a> </li>';
    echo "</ul>";
  } 
  else if (isset($_SESSION['isSellerLogin'])){
    echo "<ul>";
    echo '<li><a href="sellerLoggedin.php">Home</a> </li>';
    echo "</ul>";   
  }
  else if (isset($_SESSION['isEmployeeLogin'])){
    echo "<ul>";
    echo '<li><a href="employeeLoggedin.php">Home</a> </li>';
    echo "</ul>";
  }
  else {
    echo "<ul>";
    echo '<li><a href="homepage.php">Home</a> </li>';
    echo "</ul>";
  }



?>


</body>

</html>