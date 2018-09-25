<?php
	include_once'header.php';
	$CustomerId=$_SESSION['isCustomerLogin']['Id'];
	$addressInfor=$_SESSION['addressInfor'];
	$paymentInfor=$_SESSION['paymentInfor'];
	$shipmentInfor=$_SESSION['shipInfor'];
	$itemsInShoppingCart=$_SESSION['productsAdded'];
	$total_charge=$_SESSION['totalCharge'];
	if (isset($_POST['PlaceOrder'])){		
		$dbServername="localhost";
		$dbUsername="root";
		$dbPassword="12345";
		$dbName="ecommence"; 	
		$conn=mysqli_connect($dbServername,$dbUsername,$dbPassword,$dbName);
		for($i=0;$i<count($_SESSION['productsAdded']);$i++){
			$sql_search="SELECT * FROM inventory WHERE ItemId = ". $_SESSION['productsAdded'][$i][1].";";
			$search_result=mysqli_query($conn,$sql_search); // thre should be only one such row;
			$row=mysqli_fetch_array($search_result);	
			$old_quantity=$row['Quantity'];			
			$new_quantity=$old_quantity-$_SESSION['productsAdded'][$i][5];
			if ($new_quantity<0){
				echo "something in your shopping cart is OUT OF STOCK";
				echo "<br>";
				echo "now redirecting you to searchpage!";
				header( "refresh:5;url=searchresults.php" );
				exit();
			}
			else {$sql_update= "UPDATE inventory SET Quantity = ".$new_quantity." WHERE ItemId = ".$_SESSION['productsAdded'][$i][1]. ";";
				mysqli_query($conn,$sql_update);	
			}
		}

	// insert payment;
	// first search if it exist alreay!
	$sql_payment_exits="SELECT * FROM payment WHERE CustomerId = ". $CustomerId. " AND CardType = '".$paymentInfor[0]."' AND ExpirationDate = ". "'".$paymentInfor[1]."' AND CardNumber = ". "'".$paymentInfor[2]."';";
	//echo $sql_payment_exits;
	$exist_result=mysqli_query($conn,$sql_payment_exits);	
	$row=mysqli_fetch_array($exist_result);	
	if(mysqli_num_rows($exist_result)!=0){
		//echo "payment already exist, no need to update!";
		$paymentId=$row['PaymentId'];
	}
	if(mysqli_num_rows($exist_result)==0){
		$sql_update_payment= "INSERT INTO payment (CustomerId, CardType, ExpirationDate, CardNumber ) VALUES ( ".$CustomerId. " , " ."'".$paymentInfor[0]."' , ". "'".$paymentInfor[1]."' , "."'". $paymentInfor[2]."'" ." );";
		//echo $sql_update_payment;
		mysqli_query($conn,$sql_update_payment);	
		$sql_payment_exits="SELECT * FROM payment WHERE CustomerId = ". $CustomerId. " AND CardType = '".$paymentInfor[0]."' AND ExpirationDate = ". "'".$paymentInfor[1]."' AND CardNumber = ". "'".$paymentInfor[2]."';";
		//echo $sql_payment_exits;
		$exist_result=mysqli_query($conn,$sql_payment_exits);	
		$row=mysqli_fetch_array($exist_result);
		$paymentId=$row['PaymentId'];		
	}
	// UPDATE customerorders TABLE
	$sql_customerorders = "INSERT INTO customerorders (CustomerId, PaymentId, ShipmentId, TotalCharge ) VALUES (". $CustomerId.",".$paymentId.",".$shipmentInfor[3].",".$_SESSION['totalCharge'].");";
	//echo $sql_customerorders;
	mysqli_query($conn,$sql_customerorders);
	// insert orderitems
	// find the orderId you just added, 
	$sql_search_orderId="SELECT * FROM customerorders WHERE CustomerId = ". $CustomerId. " AND PaymentId = ". $paymentId. " AND ShipmentId = ". $shipmentInfor[3].";";
	//echo $sql_search_orderId;
	$search_orderId=mysqli_query($conn,$sql_search_orderId);
	$row=mysqli_fetch_array($search_orderId);
	$OrderId=$row['OrderId'];	
	//echo $OrderId;
	// for this $OrderId, insert stuff into the orderitems 
	//$row['SellerId'], 0
	//$row['ItemId'], 1
	//$row['ItemName'], 2
	//$row['Quantity'], 3
	//$row['Price'] 4
	// index 5 is the quantity in shopping cart
	//echo "<br>";
	for($i=0;$i<count($_SESSION['productsAdded']);$i++){
		$SellerId=$_SESSION['productsAdded'][$i][0];
		$ItemId=$_SESSION['productsAdded'][$i][1];
		$ItemName=$_SESSION['productsAdded'][$i][2];
		$totalQuantity=$_SESSION['productsAdded'][$i][3];
		$Price=$_SESSION['productsAdded'][$i][4];
		$quantityInOrder= $_SESSION['productsAdded'][$i][5];
		$sql_insert = "INSERT INTO orderitems (OrderId, ItemId, ItemName,SellerId, ItemPrice, Quantity) VALUES ( ".$OrderId.", ".$ItemId.", '".$ItemName."', ".$SellerId.", ".$Price." , ".$quantityInOrder.");";
		//echo $sql_insert;
		mysqli_query($conn,$sql_insert);
		}	
		echo "you are all SET !!!!!";
		echo "<br>";
		echo "you total charge is : ";
		echo $total_charge;
		echo "<br>";
		echo "your order id is: ";
		echo $OrderId;	
		
	}
?>
<section class="main-container"> 
	<div class="main-wrapper">
		<form class="sellerfunction"  method="POST" >			
		<input type='submit' value='Logout' name='submit_logout' >		
	</div>
</section>


<?php
	if(isset($_POST['submit_logout'])){
		echo "You are logged out! Now redirecting you to the homepage!";
		session_destroy();
		header( "refresh:3;url=homepage.php" );
	}

	include_once 'footer.php';
?>



