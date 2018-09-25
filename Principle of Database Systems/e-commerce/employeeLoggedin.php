<?php
	include_once 'header.php'; 
	if (!isset($_SESSION['isEmployeeLogin'])){
	echo "You are not logged in. Now directing you to main page!";
	header( "refresh:3;url=homepage.php" );
	exit();
	}	
	$start=$_SESSION['isEmployeeLogin']['duration'];
	$duration=$_SESSION['isEmployeeLogin']['start'];
	$logintype=$_SESSION['isEmployeeLogin']['logintype'];
	$EmployeeId=$_SESSION['isEmployeeLogin']['Id'];
	
?>
<section class="main-container"> 
	<div class="main-wrapper">
		<form class="employeefunction"  method="POST" >
			<input type='submit' value='See what is in inventory' name='submit_seeitems' >			
			<input type='submit' value='Add items to inventory' name='submit_add_inventory' >
			<input type='submit' value='Update employee information' name='submit_updateEmployee' >
			<input type='submit' value='See what is in shipment' name='submit_seeshipment' >			
			<input type='submit' value='Add shipment to shipment' name='submit_add_shipment' >
			<input type='submit' value='Logout' name='submit_logout' >
		</form>
	</div>
</section>

<?php
	if(isset($_POST['submit_add'])){
		$dbServername="localhost";
		$dbUsername="root";
		$dbPassword="12345";
		$dbName="ecommence";		
		$SellerId=$_POST['SellerId'];
		$ItemName=$_POST['ItemName'];
		$Price=$_POST['Price'];
		$Quantity=$_POST['Quantity'];
		$conn=mysqli_connect($dbServername,$dbUsername,$dbPassword,$dbName);
		$sql= "SELECT * FROM inventory WHERE SellerId = ". $SellerId ." AND ItemName = "."'".$ItemName."'".";"; 
		$result=mysqli_query($conn,$sql);
		$resultCheck=mysqli_num_rows($result);		
		if($resultCheck>=1){
			echo "You can only update the quantity of the items listed.";
			$row=mysqli_fetch_array($result);
			$oldQuantity=$row['Quantity'];
			$newQuantity= $Quantity+$oldQuantity;
			$sql= "UPDATE inventory set Quantity = ".$newQuantity. " WHERE SellerId =  ". $SellerId ." AND ItemName = "."'".$ItemName."'".";";
			mysqli_query($conn,$sql);
			echo "<br>";
			echo "Just updated";
			exit();
		}
		else if ($resultCheck==0){
			$sql_search_seller="SELECT * FROM seller WHERE SellerId = ". $SellerId." ;";
			$result_search_seller=mysqli_query($conn,$sql_search_seller);
			$result_search_seller_check=mysqli_num_rows($result_search_seller);
			if ($result_search_seller_check==0){
				echo "This seller does not exist";
				echo "<br>";
				echo "Now redirecting you to homepage. Please sign up there.";
				header( "refresh:3;url=homepage.php" );
				//$sql_insert_seller= "INSERT INTO seller (SellerId) VALUES ( " .$SellerId. ");";
				//mysqli_query($conn,$sql_insert_seller);
			}
			else {
				$sql= "INSERT INTO inventory (SellerId, ItemName, Quantity, Price ) VALUES (" .$SellerId .","."' " .$ItemName." '". " , ". $Quantity. " , ". $Price. ");";
				mysqli_query($conn,$sql);
				
			}
		}
	}
	if(isset($_POST['submit_add_ship'])){
			$dbServername="localhost";
			$dbUsername="root";
			$dbPassword="12345";
			$dbName="ecommence";		
			$ShipDetail=$_POST['ShipDetail'];
			$ShipType=$_POST['ShipType'];
			$ShipCharge=$_POST['ShipCharge'];
			$conn=mysqli_connect($dbServername,$dbUsername,$dbPassword,$dbName);
			$sql_shipsearch="SELECT * FROM shipment WHERE ShipmentDetail = '".$ShipDetail."'". "AND ShipmentType = '".$ShipType."';";
			//echo $sql_shipsearch;
			$result=mysqli_query($conn,$sql_shipsearch);
		    if(mysqli_num_rows($result)!=0){
				echo "<br>";
				echo "shipment method already exist not insert it";
				header( "refresh:3;url=employeeLoggedin.php" );
				exit();
			}
			else {
			$sql= "INSERT INTO shipment (ShipmentDetail, ShipmentType, ShipmentCharge ) VALUES ('" .$ShipDetail ."',"."'" .$ShipType."'". " , ". $ShipCharge.");";
			//echo $sql;
			mysqli_query($conn,$sql);
			}
	}
	 if (isset($_POST['update'])){
		$sql_update="UPDATE inventory SET ItemName = "."'".$_POST['ItemName'] ."',"." Price= ". $_POST['ItemPrice'] .","." Quantity = ". $_POST['Quantity']." WHERE ItemId = ".$_POST['hidden_ItemId'] .";";
		
		$dbServername="localhost";
		$dbUsername="root";
		$dbPassword="12345";
		$dbName="ecommence";
		$conn=mysqli_connect($dbServername,$dbUsername,$dbPassword,$dbName);
		mysqli_query($conn,$sql_update);	 
	}
	if (isset($_POST['update_shipment'])){
		$sql_update="UPDATE shipment SET ShipmentDetail = "."'".$_POST['ShipmentDetail'] ."',"." ShipmentType= "."'". $_POST['ShipmentType'] ."',"." ShipmentCharge = ". $_POST['ShipmentCharge']." WHERE ShipmentId = ".$_POST['hidden_ShipmentId'] .";";
		echo $sql_update;
		$dbServername="localhost";
		$dbUsername="root";
		$dbPassword="12345";
		$dbName="ecommence";
		$conn=mysqli_connect($dbServername,$dbUsername,$dbPassword,$dbName);
		mysqli_query($conn,$sql_update);	
	}
	if (isset($_POST['update_EmployeeInfor'])){
		$sql_update="UPDATE employee SET Password = "."'".$_POST['Password'] ."',"." FirstName= "."'". $_POST['FirstName'] ."',"." LastName = "."'". $_POST['LastName']. "',"." Address = "."'".$_POST['Address']."',"." Email = '".$_POST['Email']."', "."PhoneNumber =' ".$_POST['PhoneNumber']."'"." WHERE EmployeeId =".$EmployeeId.";";
		echo $sql_update;
		$dbServername="localhost";
		$dbUsername="root";
		$dbPassword="12345";
		$dbName="ecommence";
		$conn=mysqli_connect($dbServername,$dbUsername,$dbPassword,$dbName);
		mysqli_query($conn,$sql_update);	 
	}
?>





<?php	
	if (isset($_POST['submit_seeitems'])){
		echo "<table border=1> <tr><th> SellerId </th> <th> ItemId</th> <th>Item Name</th> <th>Item Price</th> <th>Quantity</th></tr>";	
		$dbServername="localhost";
		$dbUsername="root";
		$dbPassword="12345";
		$dbName="ecommence";		
		$conn=mysqli_connect($dbServername,$dbUsername,$dbPassword,$dbName);
		$sql= "SELECT * FROM inventory;";
		$result=mysqli_query($conn,$sql);
		if(mysqli_num_rows($result)!=0){
		$row=mysqli_fetch_array($result);	
		do {
		echo "<form action='employeeLoggedin.php' method='POST'>";
		echo "<tr>";
		echo "<td>" . $row['SellerId']. " </td>";
		echo "<td>" . $row['ItemId']. " </td>";
		echo "<td>" ."<input type=text name= ItemName value= ". $row['ItemName']. " </td>"; 
		echo "<td>" ."<input type=text name= ItemPrice value= ". $row['Price']. " </td>"; 
		echo "<td>" ."<input type=text name= Quantity value= ". $row['Quantity']. " </td>";
		echo "<td>" ."<input type=hidden name= hidden_SellerId value= ". $row['SellerId']. " </td>";
		echo "<td>" ."<input type=hidden name= hidden_ItemId value= ". $row['ItemId']. " </td>";
		echo "<td>" ."<input type=submit name= update value= update ". " </td>";
		echo "</tr>";
		echo "</form>"; 
		 }while($row=mysqli_fetch_array($result));
	
		}	
	}
	if (isset($_POST['submit_seeshipment'])){
		echo "<table border=1> <tr><th> SellerId </th> <th> ItemId</th> <th>Item Name</th> <th>Item Price</th> <th>Quantity</th></tr>";	
		$dbServername="localhost";
		$dbUsername="root";
		$dbPassword="12345";
		$dbName="ecommence";		
		$conn=mysqli_connect($dbServername,$dbUsername,$dbPassword,$dbName);
		$sql= "SELECT * FROM shipment;";
		$result=mysqli_query($conn,$sql);
		if(mysqli_num_rows($result)!=0){
		$row=mysqli_fetch_array($result);	
		do {
		echo "<form action='employeeLoggedin.php' method='POST'>";
		echo "<tr>";
		echo "<td>" . $row['ShipmentId']. " </td>";
		echo "<td>" ."<input type=text name= ShipmentDetail value= ". $row['ShipmentDetail']. " </td>"; 
		echo "<td>" ."<input type=text name= ShipmentType value= ". $row['ShipmentType']. " </td>"; 
		echo "<td>" ."<input type=text name= ShipmentCharge value= ". $row['ShipmentCharge']. " </td>";
		echo "<td>" ."<input type=hidden name= hidden_ShipmentId value= ". $row['ShipmentId']. " </td>";
		echo "<td>" ."<input type=submit name= update_shipment value= update ". " </td>";
		echo "</tr>";
		echo "</form>"; 
		 }while($row=mysqli_fetch_array($result));
	
		}	
	}
	else if (isset($_POST['submit_add_inventory'])){
		echo "<section class='main-container'> ";
		echo "<div class='main-wrapper'>";
		echo "<h2>Add items to inventory</h2>";
		echo "<form class='employee-form'  method='POST'>";
		echo "&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;";
		echo "<input type='text' name='SellerId' placeholder='SellerId' required>";
		echo "<input type='text' name='ItemName' placeholder='ItemName' required>";
		echo "<input type='text' name='Price' placeholder='Price' required>";
		echo "<input type='text' name='Quantity' placeholder='Quantity' required>";
		echo "<input type='submit' value='add' name='submit_add' >";
		echo "</form>";
		echo "</div>";
		echo "<style>";
		echo ".sellerupdate-form{ width:400px; margin:0 auto; padding-top:30px;	}";
		echo ".sellerupdate-form input{float:left; width:90%; height:40px; padding:0px 5%;margin-right:10px;margin-bottom:4px;border:none;background-color:#fff;font-family:arial;font-size:16px;color: #111;line-height:40px;";
		echo "</style>";
		echo "</section>";
	}else if(isset($_POST['submit_add_shipment'])){
		echo "<section class='main-container'> ";
		echo "<div class='main-wrapper'>";
		echo "<h2>Add shipment option</h2>";
		echo "<form class='shipment-form'  method='POST'>";
		echo "<input type='text' name='ShipDetail' placeholder='ShipDetail' required>";
		echo "<input type='text' name='ShipType' placeholder='ShipType' required>";
		echo "<input type='text' name='ShipCharge' placeholder='ShipCharge' required>";
		echo "<input type='submit' value='add' name='submit_add_ship' >";
		echo "</form>";
		echo "</div>";
		echo "<style>";
		echo ".shipment-form{ width:400px; margin:0 auto; padding-top:30px;	}";
		echo ".shipment-form input{float:left; width:90%; height:40px; padding:0px 5%;margin-right:10px;margin-bottom:4px;border:none;background-color:#fff;font-family:arial;font-size:16px;color: #111;line-height:40px;";
		echo "</style>";
		echo "</section>";
	}
	
	else if(isset($_POST['submit_updateEmployee'])){
		echo "<table border=1><tr><th>  EmployeeId  </th><th>  Role  </th> <th>  DateJoined  </th><th> Password </th> <th> FirstName </th> <th> LastName </th> <th> Address </th><th> Email </th><th> PhoneNumber </th></tr>";	
		$dbServername="localhost";
		$dbUsername="root";
		$dbPassword="12345";
		$dbName="ecommence";		
		$conn=mysqli_connect($dbServername,$dbUsername,$dbPassword,$dbName);
		$sql= "SELECT * FROM employee WHERE EmployeeId = ".$EmployeeId. ";";
		$result=mysqli_query($conn,$sql);
		if(mysqli_num_rows($result)!=0){
		$row=mysqli_fetch_array($result);	
		echo "<form action='EmployeeLoggedin.php' method='POST'>";
		echo "<tr>";
		echo "<td>" . "  ".$row['EmployeeId']. " </td>";
		echo "<td>" . "  ".$row['Role']. " </td>";
		echo "<td>" . "  ".$row['DateJoined']. " </td>";
		echo "<td>" ."<input type=text name= Password value= '". $row['Password']. "' </td>";
		echo "<td>" ."<input type=text name= FirstName value= '". $row['FirstName']. "' </td>"; 
		echo "<td>" ."<input type=text name= LastName value= '". $row['LastName']. "' </td>"; 
		echo "<td>" ."<input type=text name= Address value= '". $row['Address']. "' </td>";
		echo "<td>" ."<input type=text name= Email value= '". $row['Email']. "' </td>";
		echo "<td>" ."<input type=text name= PhoneNumber value= '". $row['PhoneNumber']. "' </td>";		
		echo "<td>" ."<input type=hidden name= hidden_Employee value= '". $row['EmployeeId']. "' </td>";
		echo "<td>" ."<input type=submit name= update_EmployeeInfor value= update ". " </td>";
		echo "</tr>";
		echo "</form>"; 
	}
		
	}
	else if(isset($_POST['submit_logout'])){
		echo "You are logged out! Now redirecting you to the homepage!";
		unset($_SESSION['isEmployeeLogin']['duration']);
		unset($_SESSION['isEmployeeLogin']['start']);
		unset($_SESSION['isEmployeeLogin']['logintype']);
		unset($_SESSION['isEmployeeeLogin']['Id']);
		unset($_SESSION['isEmployeeLogin']);
		session_destroy();
		header( "refresh:3;url=homepage.php" );
	}
	include_once 'footer.php';
?>