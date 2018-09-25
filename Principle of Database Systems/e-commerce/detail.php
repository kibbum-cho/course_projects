<?php
	include_once"header.php";
	$dbServername="localhost";
	$dbUsername="root";
	$dbPassword="12345";
	$dbName="ecommence"; 
?>	

<?php
	$itemid = $_GET['itemid'];
	$_SESSION['itemid'] = $itemid;
	if (isset($_SESSION['isCustomerLogin'])){
		$customerid = $_SESSION['isCustomerLogin']['Id'];
	}

		// show item information
		$conn=mysqli_connect($dbServername,$dbUsername,$dbPassword,$dbName);
		$sql= "SELECT * FROM inventory WHERE ItemId = $itemid;";
		$result=mysqli_query($conn,$sql);
		if(mysqli_num_rows($result)!=0){
			
			$row=mysqli_fetch_array($result);			
			$myData=array();			
				
			$dataElement=array(
				$row['SellerId'],
				$row['ItemId'],
				$row['ItemName'],
				$row['Quantity'],
				$row['Price']
			);

			array_push($myData, $dataElement);

			echo "<br>".$row['ItemName']. ' | Price: ' .$row['Price']. ' | Quantity: '.$row['Quantity']."</br>";
		}

		echo "<br>Customer Reviews</br>";

		// show item reviews
		$sql="SELECT * FROM review WHERE ItemID = $itemid;";
		$result=mysqli_query($conn,$sql);
		if(mysqli_num_rows($result)!=0){
			$count=0;
			$row=mysqli_fetch_array($result);			
			$myData=array();			
			do{	
				$dataElement=array(
					$row['ReviewId'],
					$row['ItemId'],
					$row['Rating'],
					$row['DetailedReview'],
					$row['DatePosted'],
					$row['CustomerId']
				);

				array_push($myData, $dataElement);

				echo "--------------------------------------------------------------------------------------------------------------------------------------";

				echo "<br> Rating: ".$row['Rating']. "</br>";
				echo "<br>" .$row['DetailedReview']. ' | ' .$row['DatePosted']. "</br>";

				///
				$cid = $row['CustomerId'];
				$sql2="SELECT * FROM customer WHERE CustomerId = $cid;";
				$result2=mysqli_query($conn,$sql2);
				if(mysqli_num_rows($result2)!=0){
					$row2=mysqli_fetch_array($result2);			
					$myData2=array();			
					$dataElement2=array(
						$row2['CustomerId'],
						$row2['Password'],
						$row2['FirstName'],
						$row2['LastName'],
						$row2['Address'],
						$row2['Email'],
						$row2['PhoneNumber']
					);

					array_push($myData2, $dataElement2);

					echo "<br>".$row2['FirstName']. ' ' .$row2['LastName']. "</br>";

					if (isset($_SESSION['isCustomerLogin'])){
						if($cid == $customerid){
							$reviewid = $row['ReviewId'];
							//echo '<form method="POST" action= "redirect.php?reviewid=">';
							echo '<form method="POST" action= "redirect.php?reviewid=' .$reviewid. '">';
							echo '<input type="submit" name= "DeleteReview" value="delete" >';
							echo "</form>";
						}
					}
				}
				///


				$count=$count+1;		
			}while($row=mysqli_fetch_array($result));
		}

?>



<?php

	if (isset($_SESSION['isCustomerLogin'])){
		echo '<section class="main-container">';
		echo '<div class="main-wrapper">';
		echo "<h2>Write a Review</h2>";
		echo '<form class="signup-form"  method="POST">';
		echo '<input type="text" name="rating" placeholder="Rate from 1 - 5" required>';
		echo '<input type="text" name="detailedreview" placeholder="Write your thoughts on the item..." required>';
		echo '<input type="submit" value="submit" name="submit" >';

		echo "</form>";
		echo "</div>";
		echo "</section>";
	}

	if(isset($_POST['submit'])){ 

			$rating=mysqli_real_escape_string($conn,$_POST['rating']);

			if($rating < 1 || $rating > 5){
				$message = "Please use a number between 1 and 5";
				echo "<script type='text/javascript'>alert('$message');</script>";
				exit;
			}

			$detailedreview=mysqli_real_escape_string($conn,$_POST['detailedreview']);

			$tableName = "Review";
			$zero = 0;
			$customerid = $_SESSION['isCustomerLogin']['Id'];

			date_default_timezone_set("America/New_York");

			$date = date('Y-m-d');
			$sql="INSERT INTO  $tableName (ReviewId, ItemId, Rating, DetailedReview, DatePosted, CustomerId) VALUES ($zero, $itemid, $rating, '$detailedreview', '$date', '$customerid');";
			mysqli_query($conn,$sql);
			//unset($_POST['submit']);
			//header("Refresh:0");
			header('Location: redirect.php');
			exit;
	}

?>