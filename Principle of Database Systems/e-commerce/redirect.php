<?php
	session_start();

	if (isset($_POST['DeleteReview'])){		
		$dbServername="localhost";
		$dbUsername="root";
		$dbPassword="12345";
		$dbName="ecommence"; 	
		$conn=mysqli_connect($dbServername,$dbUsername,$dbPassword,$dbName);

		$reviewid = $_GET['reviewid'];
		$sql="DELETE FROM review WHERE ReviewId = ". $reviewid.";";
		mysqli_query($conn,$sql);

	}


	$itemid = $_SESSION['itemid'];
	header("Location: detail.php?itemid=$itemid");
?>