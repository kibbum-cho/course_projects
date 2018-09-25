<?php
	include_once 'header.php';	
	$CustomerId=$_SESSION['isCustomerLogin']['Id'];
	$dbServername="localhost";
	$dbUsername="root";
	$dbPassword="12345";
	$dbName="ecommence";
	
	
	
?>
<div class ="checkout">
	<form method='POST' action= 'checkout.php'>
	<input type='submit' name= 'ContinueCheckout' value='ContinueCheckout' >
	</form>
</div>
<div class ="checkout">
	<form method='POST' >
	<input type='submit' name= 'submit_logout' value='Logout' >
	</form>
</div>
<div class ="viewInfor">
	<form method='POST' action= 'checkoutOrKeepShopping.php'>
	<input type='submit' name= 'ViewAddressInfo' value='ViewAddressInfo' >
	<input type='submit' name= 'ViewShipInfo' value='ViewShipInfo' >
	<input type='submit' name= 'ViewPaymentInfo' value='ViewPaymentInfo' >	
	<input type='submit' name= 'ViewItemInShoppingCart' value='ViewItemInShoppingCart' >
	</form>
</div>
<?php
if (isset($_POST['keepsearch'])){
	echo "You want keep searching?";
	header( "refresh:2;url=searchresults.php" );
	exit();
}
else if(isset($_POST['submit_logout'])){
		echo "You are logged out! Redirecting you to the homepage!";
		unset($_SESSION['isCustomerLogin']['duration']);
		unset($_SESSION['isCustomerLogin']['start']);
		unset($_SESSION['isCustomerLogin']['logintype']);
		unset($_SESSION['isCustomerLogin']['Id']);
		unset($_SESSION['isCustomerLogin']);
		session_destroy();
		header( "refresh:3;url=homepage.php" );
}
else if (isset($_POST['checkout'])){
	 $totalChargeForItems=0;
	// items in shopping cart 
	$itemsInShoppingCart=$_SESSION['productsAdded'];
	echo "<p1> Items in shopping cart </p1>";
	echo "<table border=1><tr><th>SellerId &emsp;&emsp;&emsp;</th><th>ItemId &emsp;&emsp;&emsp;</th><th>ItemName &emsp;&emsp;&emsp;</th><th>Price &emsp;&emsp;&emsp;</th><th>Quantity &emsp;&emsp;&emsp;</th></tr>";	
	for($i=0;$i<count($_SESSION['productsAdded']);$i++){
		//$_SESSION['totalCharge']=$totalChargeForItems+$_SESSION['productsAdded'][$i][4]*$_SESSION['productsAdded'][$i][5];
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
	
	// Customer Address
		echo "<table border=1> <tr><th> FirstName </th> <th> LastName </th> <th> Address </th><th> Email </th><th> PhoneNumber </th></tr>";	
		$conn=mysqli_connect($dbServername,$dbUsername,$dbPassword,$dbName);
		$sql= "SELECT * FROM customer WHERE CustomerId = ".$CustomerId. ";";
		$result=mysqli_query($conn,$sql);
		if(mysqli_num_rows($result)!=0){
		$row=mysqli_fetch_array($result);
		$_SESSION['addressInfor']=array($row['FirstName'],$row['LastName'],$row['Address'],$row['Email'],$row['PhoneNumber']);
		echo "<h1> Use this address? (Customer's address)</h1>";
		echo "<form  method='POST'>";
		echo "<tr>";
		echo "<td>" ."<input type=text name= FirstName value= ". $row['FirstName']. "></td>"; 
		echo "<td>" ."<input type=text name= LastName value= ". $row['LastName']. " ></td>"; 
		echo "<td>" ."<input type=text name= Address value= ". $row['Address']. " ></td>";
		echo "<td>" ."<input type=text name= Email value= ". $row['Email']. " ></td>";
		echo "<td>" ."<input type=text name= PhoneNumber value= ". $row['PhoneNumber']. "> </td>";
		echo "<td>" ."<input type=submit name= update_shipping Address value= Change To This Address ". "></td>";
		echo "</tr>";
		echo "</form>"; 
	}	
	// shipment information
		echo "<table border=1> <tr><th> Shipment detail </th> <th> Shipment type </th> <th> Shipment charge </th></tr>";	
		echo "<h1> Shipment information</h1>";
		echo "shipdetail: next-day/regular";
		echo "shiptype: USPS/UPS";		
		echo "<form  method='POST'>";
		echo "<tr>";
		$_SESSION['shipInfor']=array('next-day','USPS',10.00);
		//$_SESSION['totalCharge']=$_SESSION['totalCharge']+$_SESSION['shipInfor'][2];
		echo "<td>" ."<input type=text name= shipmentdetail value= ". "next-day". " ></td>"; 
		echo "<td>" ."<input type=text name= shipmenttype value= "."USPS". " ></td>"; 
		echo "<td>" . "10.00 ". "</td>";
		echo "<td>" ."<input type=submit name= update_shippingInfor value= 'Use this shipment type' ". " </td>";
		echo "</tr>";
		echo "</form>";
	
	// payment information
	echo "<table border=1> <tr><th> Card Type </th> <th> Expiration Date </th> <th> Card Number </th></tr>";	
	echo "<h2> Your payment information</h2>";
		echo "<form  method='POST'>";
		echo "<tr>";
		$_SESSION['paymentInfor']=array();
		echo "<td>" ."<input type=text name= CardType value= "." ". " ></td>"; 
		echo "<td>" ."<input type=text name= ExpirationDate value= "." ". " ></td>"; 
		echo "<td>" ."<input type=text name= CardNumber value= "." ". "> </td>";
		echo "<td>" ."<input type=submit name= update_payment value= 'Use this payment' ". " </td>";
		echo "</tr>";
		echo "</form>";		
		// NOW ECHO THE TOTAL CHARGE INCLUSING THE SHIPPING 
		$_SESSION['totalCharge']=0;
		for($i=0;$i<count($_SESSION['productsAdded']);$i++){
			$_SESSION['totalCharge']=$totalChargeForItems+$_SESSION['productsAdded'][$i][4]*$_SESSION['productsAdded'][$i][5];
		}
		$_SESSION['totalCharge']=$_SESSION['totalCharge']+$_SESSION['shipInfor'][2];
		echo "<table border=1> <tr><th>TOTAL CHARGE IS</th></tr>";	
		echo "<table border=1> <tr><th>".$_SESSION['totalCharge']."</th></tr>";	
}
	if (isset($_POST['update_shippingAddress'])){
		$shippingAddress=array();
		array_push($shippingAddress,$_POST['FirstName']); 
		array_push($shippingAddress,$_POST['LastName']); 
		array_push($shippingAddress,$_POST['Address']); 
		array_push($shippingAddress,$_POST['Email']); 
		array_push($shippingAddress,$_POST['PhoneNumber']); 
		$_SESSION['addressInfor']=$shippingAddress;
	}
	if (isset($_POST['update_shippingInfor'])){
		$shipInfor=array();
		array_push($shipInfor,$_POST['shipmentdetail']); 
		array_push($shipInfor,$_POST['shipmenttype']); 
		$dbServername="localhost";
		$dbUsername="root";
		$dbPassword="12345";
		$dbName="ecommence"; 
		$conn=mysqli_connect($dbServername,$dbUsername,$dbPassword,$dbName);
		$sql_search_ship_charge= "SELECT * FROM shipment WHERE ShipmentDetail = ". "'".$_POST['shipmentdetail']."'"." AND ShipmentType = "."'". $_POST['shipmenttype']."'". ";";
		$result=mysqli_query($conn,$sql_search_ship_charge);
		if(mysqli_num_rows($result)!=0){
			$row=mysqli_fetch_array($result);
			array_push($shipInfor,$row['ShipmentCharge']);
			array_push ($shipInfor,$row['ShipmentId']);
			$_SESSION['shipInfor']=$shipInfor;	
			for($i=0;$i<count($_SESSION['productsAdded']);$i++){
				$_SESSION['totalCharge']=$_SESSION['totalCharge']+$_SESSION['productsAdded'][$i][4]*$_SESSION['productsAdded'][$i][5];
			}
			$_SESSION['totalCharge']=$_SESSION['totalCharge']+$_SESSION['shipInfor'][2];
			echo "NOW the total charge is :   ";		
			echo $_SESSION['totalCharge'];	
		}
		else {
			echo "No shipping method was found ! Use the default?";
			$shipInfor=array();
			array_push($shipInfor,'next-day'); 
			array_push($shipInfor,'USPS'); 
			// and search for the ID and charge; 
			$sql_search_ship_charge= "SELECT * FROM shipment WHERE ShipmentDetail = ". "'"."next-day"."'"." AND ShipmentType = "."'".'USPS'."'". ";";
			$result=mysqli_query($conn,$sql_search_ship_charge);
			$row=mysqli_fetch_array($result);			
			array_push($shipInfor,$row['ShipmentCharge']);
			array_push ($shipInfor,$row['ShipmentId']);
			$_SESSION['shipInfor']=$shipInfor;	
			echo $_SESSION['shipInfor'][0];
			echo $_SESSION['shipInfor'][1];
			echo $_SESSION['shipInfor'][2];
			echo $_SESSION['shipInfor'][3];
			header( "refresh:3;url=checkoutOrKeepShopping.php" );
			exit();
			
		}
		
	}
	if (isset($_POST['update_payment'])){
		$paymentInfor=array();
		array_push($paymentInfor,$_POST['CardType']); 
		array_push($paymentInfor,$_POST['ExpirationDate']); 
		array_push($paymentInfor,$_POST['CardNumber']); 
		$_SESSION['paymentInfor']=$paymentInfor;
	}

?>


<?php
	if (isset($_POST['ViewAddressInfo'])){
		if (count($_SESSION['addressInfor'])==0){			
			echo "No address information available";
			echo "<h1> No address info is available. You can add it here</h1>";
			echo "<table border=1> <tr><th> FirstName </th> <th> LastName </th> <th> Address </th><th> Email </th><th> PhoneNumber </th></tr>";	
			echo "<form  method='POST'>";
			echo "<tr>";
			echo "<td>" ."<input type=text name= FirstName value= "." " ."</td>"; 
			echo "<td>" ."<input type=text name= LastName value= "." ". " ></td>"; 
			echo "<td>" ."<input type=text name= Address value= "." ". " ></td>";
			echo "<td>" ."<input type=text name= Email value= ". " ". " ></td>";
			echo "<td>" ."<input type=text name= PhoneNumber value= ". " ". "> </td>";
			echo "<td>" ."<input type=submit name= update_shippingAddress value= Change ". " </td>";
			echo "</tr>";
			echo "</form>"; 
		}
		else {
			echo "<h1> Use this address? (Customer's address)</h1>";
			echo "<table border=1> <tr><th> First Name </th> <th> Last Name </th> <th> Address </th><th> Email </th><th> Phone Number </th></tr>";	
			echo "<form  method='POST'>";
			echo "<tr>";
			echo "<td>" ."<input type=text name= FirstName value= ".$_SESSION['addressInfor'][0] . " </td>"; 
			echo "<td>" ."<input type=text name= LastName value= ". $_SESSION['addressInfor'][1]. " </td>"; 
			echo "<td>" ."<input type=text name= Address value= ".$_SESSION['addressInfor'][2]. " </td>";
			echo "<td>" ."<input type=text name= Email value= ". $_SESSION['addressInfor'][3]. " </td>";
			echo "<td>" ."<input type=text name= PhoneNumber value= ". $_SESSION['addressInfor'][4]. " </td>";
			echo "<td>" ."<input type=submit name= update_shippingAddress value= ChangeToThisAddress ". " </td>";
			echo "</tr>";
			echo "</form>"; 
		}
	}
	if (isset($_POST['ViewPaymentInfo'])){
		if (count($_SESSION['paymentInfor'])==0){
			echo "No payment info available! You can update here!";
			echo "<table border=1> <tr><th> CardType </th> <th> ExpirationDate </th> <th> CardNumber </th></tr>";	
			echo "<h2> Your payment information</h2>";
			echo "<form  method='POST'>";
			echo "<tr>";
			echo "<td>" ."<input type=text name= CardType value= "."". " ></td>"; 
			echo "<td>" ."<input type=text name= ExpirationDate value= "."". " ></td>"; 
			echo "<td>" ."<input type=text name= CardNumber value= "." ". "> </td>";
			echo "<td>" ."<input type=submit name= update_payment value= useThisPayment ". " </td>";
			echo "</tr>";
			echo "</form>"; 
		}
		else{
			echo "<table border=1> <tr><th> Card Type </th> <th> Expiration Date </th> <th> Card Number </th></tr>";	
			echo "<h2> Your payment information</h2>";
			echo "<form  method='POST'>";
			echo "<tr>";
			echo "<td>" ."<input type=text name= CardType value= ".$_SESSION['paymentInfor'][0]. " ></td>"; 
			echo "<td>" ."<input type=text name= ExpirationDate value= ".$_SESSION['paymentInfor'][1]. " ></td>"; 
			echo "<td>" ."<input type=text name= CardNumber value= ".$_SESSION['paymentInfor'][2]. "> </td>";
			echo "<td>" ."<input type=submit name= update_payment value= useThisPayment ". " </td>";
			echo "</tr>";
			echo "</form>"; 
		}		
	}
	// view ship infor
	if (isset($_POST['ViewShipInfo'])){
		if (count($_SESSION['shipInfor'])==0){
			echo "No shipment information available! You can update here!";
			echo "shipdetail: next-day/regular";
			echo "shiptype: USPS/UPS";	
			echo "<table border=1> <tr><th> Shipment detail </th> <th> Shipment type </th> <th> Shipment charge </th></tr>";	
			echo "<h2> Your shipment information</h2>";
			echo "<form  method='POST'>";
			echo "<tr>";
			echo "<td>" ."<input type=text name= shipmentdetail value= "."". " ></td>"; 
			echo "<td>" ."<input type=text name= shipmenttype value= "."". " ></td>"; 
			echo "<td>" ."value= "." ". "> </td>";
			echo "<td>" ."<input type=submit name= update_shippingInfor value= usethisshipmenttype ". " </td>";
			echo "</tr>";
			echo "</form>"; 
		}
		else{
			echo "shipdetail: next-day/regular";
			echo "shiptype: USPS/UPS";	
			echo "<table border=1> <tr><th> Shipment detail </th> <th> Shipment type </th> <th> Shipment charge </th></tr>";	
			echo "<h2> Your shipment information</h2>";
			echo "<form  method='POST'>";
			echo "<tr>";
			echo "<td>" ."<input type=text name= shipmentdetail value= ".$_SESSION['shipInfor'][0]. " ></td>"; 
			echo "<td>" ."<input type=text name= shipmenttype value= ".$_SESSION['shipInfor'][1]. " ></td>"; 
			echo "<td> ".$_SESSION['shipInfor'][2]. "</td>";
			echo "<td>" ."<input type=submit name= update_shippingInfor value= usethisshipmenttype ". " </td>";
			echo "</tr>";
			echo "</form>"; 
		}		
	}
	if (isset($_POST['ViewItemInShoppingCart'])){
		$itemsInShoppingCart=$_SESSION['productsAdded'];
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
		$_SESSION['totalCharge']=0;
		for($i=0;$i<count($_SESSION['productsAdded']);$i++){
			$_SESSION['totalCharge']=$_SESSION['totalCharge']+$_SESSION['productsAdded'][$i][4]*$_SESSION['productsAdded'][$i][5];
		}
		$_SESSION['totalCharge']=$_SESSION['totalCharge']+$_SESSION['shipInfor'][2];
		echo "<table border=1> <tr><th>TOTAL CHARGE IS </th></tr>";	
		echo "<table border=1> <tr><th>".$_SESSION['totalCharge']."</th></tr>";	
	}
?>

<?php
	include_once 'footer.php'
?>



