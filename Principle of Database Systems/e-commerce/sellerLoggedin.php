<?php
	include_once 'header.php'; 
	if (!isset($_SESSION['isSellerLogin'])){
		echo "You are not logged, redirecting you to homepage!";
		header( "refresh:3;url=homepage.php" );
		exit();
	}	
	$start=$_SESSION['isSellerLogin']['duration'];
	$duration=$_SESSION['isSellerLogin']['start'];
	$logintype=$_SESSION['isSellerLogin']['logintype'];
	$SellerId=$_SESSION['isSellerLogin']['Id'];
	
?>
<section class="main-container"> 
	<div class="main-wrapper">
		<form class="sellerfunction"  method="POST" >
			<input type='submit' value='See what is in shop' name='submit_seeitems' >			
			<input type='submit' value='Add items to shop' name='submit_additems' >
			<input type='submit' value='Update seller information' name='submit_updateSeller' >
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
		$ItemName=$_POST['itemname'];
		$price=$_POST['price'];
		$quantity=$_POST['quantity'];
		$conn=mysqli_connect($dbServername,$dbUsername,$dbPassword,$dbName);
		$sql= "SELECT * FROM inventory WHERE SellerId = ". $SellerId ." AND ItemName = "."'".$ItemName."'".";"; 
		$result=mysqli_query($conn,$sql);
		$resultCheck=mysqli_num_rows($result);		
		if($resultCheck>=1){
			echo "You can update the quantity.";
			$row=mysqli_fetch_array($result);
			$oldQuantity=$row['Quantity'];
			$newQuantity= $quantity+$oldQuantity;
			$sql= "UPDATE inventory set Quantity = ".$newQuantity. " WHERE SellerId =  ". $SellerId ." AND ItemName = "."'".$ItemName."'".";";
			echo $sql;
			mysqli_query($conn,$sql);
			echo "<br>";
			echo "Just updated";
			exit();
		}
		else if ($resultCheck==0){
			$sql= "INSERT INTO inventory (SellerId, ItemName, Quantity, Price ) VALUES (" .$SellerId .","."' " .$ItemName." '". " , ". $quantity. " , ". $price. ");";
			echo "Item added successfully!";
			mysqli_query($conn,$sql);
		}
	}
	 if (isset($_POST['update'])){
		$sql_update="UPDATE inventory SET ItemName = "."'".$_POST['ItemName'] ."',"." Price= ". $_POST['ItemPrice'] .","." Quantity = ". $_POST['Quantity']. " WHERE ItemId = ".$_POST['hidden_ItemId'] .";";
		$dbServername="localhost";
		$dbUsername="root";
		$dbPassword="12345";
		$dbName="ecommence";
		$conn=mysqli_connect($dbServername,$dbUsername,$dbPassword,$dbName);
		mysqli_query($conn,$sql_update);	 
	}
	if (isset($_POST['update_SellerInfor'])){
		$sql_update="UPDATE seller SET Password = "."'".$_POST['Password'] ."',"." FirstName= "."'". $_POST['FirstName'] ."',"." LastName = "."'". $_POST['LastName']. "',"." Address = "."'".$_POST['Address']."',"." Email = '".$_POST['Email']."', "."PhoneNumber =' ".$_POST['PhoneNumber']."' WHERE SellerId = ".$SellerId.";";
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
		echo "<table border=1> <tr> <th>Item Name</th> <th>Item Price</th> <th>Quantity</th></tr>";	
		$dbServername="localhost";
		$dbUsername="root";
		$dbPassword="12345";
		$dbName="ecommence";		
		$conn=mysqli_connect($dbServername,$dbUsername,$dbPassword,$dbName);
		$sql= "SELECT * FROM inventory WHERE SellerId = ".$SellerId. ";";
		$result=mysqli_query($conn,$sql);
		if(mysqli_num_rows($result)!=0){
		$row=mysqli_fetch_array($result);	
		do {
		echo "<form action='sellerLoggedin.php' method='POST'>";
		echo "<tr>";
		echo "<td>" ."<input type=text name= ItemName value='". $row['ItemName'] ." ' </td>"; 
		echo "<td>" ."<input type=text name= ItemPrice value= ". $row['Price']. " </td>"; 
		echo "<td>" ."<input type=text name= Quantity value= ". $row['Quantity']. " </td>";
		echo "<td>" ."<input type=hidden name= hidden_ItemId value= ". $row['ItemId']. " </td>";
		echo "<td>" ."<input type=submit name= update value= update ". " </td>";
		echo "</tr>";
		echo "</form>"; 
		 }while($row=mysqli_fetch_array($result));
	
		}	
	}
	else if (isset($_POST['submit_additems'])){
		echo "<section class='main-container'> ";
		echo "<div class='main-wrapper'>";
		echo "<h2>Add item to shop</h2>";
		echo "<form class='sellerupdate-form'  method='POST'>";
		echo "<input type='text' name='itemname' placeholder='item name' required>";
		echo "<input type='text' name='price' placeholder='price' required>";
		echo "<input type='text' name='quantity' placeholder='quantity' required>";
		echo "<input type='submit' value='add' name='submit_add' >";
		echo "</form>";
		echo "</div>";
		echo "<style>";
		echo ".sellerupdate-form{ width:400px; margin:0 auto; padding-top:30px;	}";
		echo ".sellerupdate-form input{float:left; width:90%; height:40px; padding:0px 5%;margin-right:10px;margin-bottom:4px;border:none;background-color:#fff;font-family:arial;font-size:16px;color: #111;line-height:40px;";
		echo "</style>";
		echo "</section>";

	
	
}
	
	else if(isset($_POST['submit_updateSeller'])){
		//echo "you want to see the seller information?";
		echo "<table border=1> <tr><th> Password </th> <th> FirstName </th> <th> LastName </th> <th> Address </th><th> Email </th><th> PhoneNumber </th></tr>";	
		$dbServername="localhost";
		$dbUsername="root";
		$dbPassword="12345";
		$dbName="ecommence";		
		$conn=mysqli_connect($dbServername,$dbUsername,$dbPassword,$dbName);
		$sql= "SELECT * FROM seller WHERE SellerId = ".$SellerId. ";";
		$result=mysqli_query($conn,$sql);
		if(mysqli_num_rows($result)!=0){
		$row=mysqli_fetch_array($result);	
		echo "<form action='sellerLoggedin.php' method='POST'>";
		echo "<tr>";
		echo "<td>" ."<input type=text name= Password value= '". $row['Password']. "' </td>";
		echo "<td>" ."<input type=text name= FirstName value= '". $row['FirstName']. "' </td>"; 
		echo "<td>" ."<input type=text name= LastName value= '". $row['LastName']. "' </td>"; 
		echo "<td>" ."<input type=text name= Address value= '". $row['Address']. "' </td>";
		echo "<td>" ."<input type=text name= Email value= '". $row['Email']. "' </td>";
		echo "<td>" ."<input type=text name= PhoneNumber value= '". $row['PhoneNumber']. "' </td>";
		echo "<td>" ."<input type=hidden name= hidden_SellerId value= '". $row['SellerId']. "' </td>";
		echo "<td>" ."<input type=submit name= update_SellerInfor value= update ". " </td>";
		echo "</tr>";
		echo "</form>"; 
	}
		
	}
	else if(isset($_POST['submit_logout'])){
		echo "You are logged out! Now redirecting you to the homepage!";
		unset($_SESSION['isSellerLogin']['duration']);
		unset($_SESSION['isSellerLogin']['start']);
		unset($_SESSION['isSellerLogin']['logintype']);
		unset($_SESSION['isSellerLogin']['Id']);
		unset($_SESSION['isSellerLogin']);
		session_destroy();
		header( "refresh:3;url=homepage.php" );
	}






	include_once 'footer.php';
?>