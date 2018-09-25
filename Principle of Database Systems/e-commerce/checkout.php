<?php
	// need to check if the ssession variables has value greater than 0
	include_once 'header.php';	
	$CustomerId=$_SESSION['isCustomerLogin']['Id'];
	$addressInfor=$_SESSION['addressInfor'];
	$paymentInfor=$_SESSION['paymentInfor'];
	$shipmentInfor=$_SESSION['shipInfor'];
	$itemsInShoppingCart=$_SESSION['productsAdded'];
	if (count($addressInfor)<5){
		echo "Customer address info is not complete!";
		header( "refresh:3;url=checkoutOrKeepShopping.php" );
		exit();
	}
	else if (count($paymentInfor)<3){
		echo "Payment info is not complete!";
		header( "refresh:3;url=checkoutOrKeepShopping.php" );
		exit();
	}
	else if (count($shipmentInfor)<3){
		echo "Shipment info is not complete!";
		header( "refresh:3;url=checkoutOrKeepShopping.php" );
		exit();		
	}
	
?>

<div class ="checkout">
	<form method='POST' action= 'placeorder.php'>
	<input type='submit' name= 'PlaceOrder' value='PlaceOrder' >
	</form>
</div>
<div class ="checkout">
	<form method='POST' >
	<input type='submit' name= 'submit_logout' value='Logout' >
	</form>
</div>
<?php
	if(isset($_POST['submit_logout'])){
		echo "You are logged out! Redirecting you to the homepage!";
		unset($_SESSION['isCustomerLogin']['duration']);
		unset($_SESSION['isCustomerLogin']['start']);
		unset($_SESSION['isCustomerLogin']['logintype']);
		unset($_SESSION['isCustomerLogin']['Id']);
		unset($_SESSION['isCustomerLogin']);
		session_destroy();
		header( "refresh:3;url=homepage.php" );
}




	echo "<p1> Items in shopping cart </p1>";
	echo "<table border=1><tr><th>SellerId</th><th>ItemId</th><th>ItemName</th><th>Price</th><th>Quantity</th></tr>";	
	for($i=0;$i<count($_SESSION['productsAdded']);$i++){
		echo "<form method='POST'>";
		echo "<tr>";
		echo "<td>" .$_SESSION['productsAdded'][$i][0]. " </td>"; 
		echo "<td>" .$_SESSION['productsAdded'][$i][1]. " </td>"; 
		echo "<td>" .$_SESSION['productsAdded'][$i][2]. " </td>";
		echo "<td>" .$_SESSION['productsAdded'][$i][4]. " </td>";
		echo "<td>" .$_SESSION['productsAdded'][$i][5]. " </td>";
		echo "</tr>";
		echo "</form>"; 
	}
		echo "<table border=1> <tr><th> FirstName </th> <th> LastName </th> <th> Address </th><th> Email </th><th> PhoneNumber </th></tr>";	
		echo "<h1> Customer's address </h1>";
		echo "<form  method='POST'>";
		echo "<tr>";
		echo "<td>".$_SESSION['addressInfor'][0]."</td>"; 
		echo "<td>".$_SESSION['addressInfor'][1]."</td>"; 
		echo "<td>".$_SESSION['addressInfor'][2]."</td>";
		echo "<td>".$_SESSION['addressInfor'][3]."</td>";
		echo "<td>".$_SESSION['addressInfor'][4]."</td>";
		echo "</tr>";
		echo "</form>"; 

		echo "<table border=1> <tr><th> shipmentDetail </th> <th> ShipmentType </th> <th> ShipCharge </th></tr>";	
		echo "<h1> Shipment information</h1>";
		echo "shipdetail: next-day/regular";
		echo "shiptype: USPS/UPS";		
		echo "<form  method='POST'>";
		echo "<tr>";
		echo "<td>".$_SESSION['shipInfor'][0]."</td>"; 
		echo "<td>".$_SESSION['shipInfor'][1]. "</td>"; 
		echo "<td>".$_SESSION['shipInfor'][2]. "</td>";
		echo "</tr>";
		echo "</form>";	
	// payment information
		echo "<table border=1> <tr><th> CardType </th> <th> ExpirationDate </th> <th> CardNumber </th></tr>";	
		echo "<h2> Your payment information</h2>";
		echo "<form  method='POST'>";
		echo "<tr>";		
		echo "<td>".$_SESSION['paymentInfor'][0]. "</td>"; 
		echo "<td>".$_SESSION['paymentInfor'][1]."</td>"; 
		echo "<td>".$_SESSION['paymentInfor'][2] ."</td>";
		echo "</tr>";
		echo "</form>";
		echo "<table border=1> <tr><th>TOTAL CHARGE IS</th></tr>";	
		echo "<table border=1> <tr><th>".$_SESSION['totalCharge']."</th></tr>";
?>

<?php	
	include_once 'footer.php';

?>