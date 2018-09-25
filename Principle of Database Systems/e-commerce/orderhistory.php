<?php
	include_once 'header.php'; 
	$CustomerId=$_SESSION['isCustomerLogin']['Id'];
	
	
	if (isset($_POST['view_customer_orders'])){		
		echo "<section class='main-container'> ";
		echo "<div class='main-wrapper'>";
		$dbServername="localhost";
		$dbUsername="root";
		$dbPassword="12345";
		$dbName="ecommence";
		$conn=mysqli_connect($dbServername,$dbUsername,$dbPassword,$dbName);
		$sql_see_orders="SELECT * FROM customerorders WHERE CustomerId = ". $CustomerId. ";";
		$result=mysqli_query($conn,$sql_see_orders);
		if(mysqli_num_rows($result)!=0){
			$OrderIdArray=array();
			$row=mysqli_fetch_array($result);
			do{	
				$single_order=array(
					$row['OrderId'],
					$row['TotalCharge'],
					$row['PaymentId'],
					$row['ShipmentId']
				);
				array_push($OrderIdArray,$single_order);
				}while($row=mysqli_fetch_array($result));
				$conn=mysqli_connect($dbServername,$dbUsername,$dbPassword,$dbName);	
				for($i=0;$i<count($OrderIdArray);$i++){
				$OrderId=$OrderIdArray[$i][0]; // for each order there is an array of items
				$OrderCharge=$OrderIdArray[$i][1];
				echo "<table border=1><tr><th>OrderId: ".$OrderId ."</th></tr>";	
				echo "<table border=1><tr><th>OrderCharge: ".$OrderCharge ."</th></tr>";
				$PaymentId=$OrderCharge=$OrderIdArray[$i][2];
				$sql_search_payment= "SELECT * FROM payment WHERE PaymentId = ". $PaymentId. ";";
				$result_payment=mysqli_query($conn,$sql_search_payment);
				if(mysqli_num_rows($result_payment)!=0){
					$row=mysqli_fetch_array($result_payment);
					echo "<table border=1><tr><th>PaymentCard: ".$row['CardType'] ."</th></tr>";	
					echo "<table border=1><tr><th>cardNumber: ".$row['CardNumber'] ."</th></tr>";
					echo "<table border=1><tr><th>ExpDate: ".$row['ExpirationDate'] ."</th></tr>";	
				}
				$ShipmentId=$OrderCharge=$OrderIdArray[$i][3];
				$sql_search_shipment="SELECT * FROM shipment WHERE ShipmentId = " .$ShipmentId. ";";
				$result_shipment=mysqli_query($conn,$sql_search_shipment);
				if(mysqli_num_rows($result_shipment)!=0){
					$row=mysqli_fetch_array($result_shipment);
					echo "<table border=1><tr><th>ShipmentDetail: ".$row['ShipmentDetail'] ."</th></tr>";	
					echo "<table border=1><tr><th>ShipmentType: ".$row['ShipmentType'] ."</th></tr>";
					echo "<table border=1><tr><th>ShipmentCharge: ".$row['ShipmentCharge'] ."</th></tr>";	
				}
				
				
				$order_search_query="SELECT * FROM orderitems WHERE OrderId = ".$OrderId.";";
				$order_item_result=mysqli_query($conn,$order_search_query);
				if (mysqli_num_rows($order_item_result)!=0){ // there is a bun of orderitem 
					$all_item_infor=array();
					$item_row=mysqli_fetch_array($order_item_result);
					do {
						$single_item=array(
						$item_row['OrderId'],
						$item_row['SellerId'],
						$item_row['ItemId'],
						$item_row['ItemName'],
						$item_row['ItemPrice'],
						$item_row['Quantity']						
						);
						array_push($all_item_infor,$single_item);
					}while($item_row=mysqli_fetch_array($order_item_result));
				}
				
				echo "<table border=1><tr><th>SellerId</th><th>ItemId</th><th>ItemName</th><th>ItemPrice</th><th>Quantity</th></tr>";	
				for($j=0;$j<count($all_item_infor);$j++){					
					echo "<form >";
					echo "<tr>";
					echo "<td>" .$all_item_infor[$j][1]. " </td>"; 
					echo "<td>" .$all_item_infor[$j][2]. " </td>"; 
					echo "<td>" .$all_item_infor[$j][3]. " </td>";
					echo "<td>" .$all_item_infor[$j][4]. " </td>";
					echo "<td>" .$all_item_infor[$j][5]. " </td>";
					echo "</tr>";
					echo "</form>"; 
					}
					
			echo "<br>";
	}
				
				
				
			
				
			}
			
		else if (mysqli_num_rows($result)==0){
			echo "There is no order history in your account!";
		}
	}	
	
?>
<section class="main-container"> 
	<div class="main-wrapper">
		<form class="sellerfunction" action="customerLoggedin.php"  method="POST" >
			<input type='submit' value='goback' name='goback' >			
		</form>
	</div>
</section>

<?php	
	
		
	
	include_once 'footer.php';
?>