<?php
	include_once 'header.php'; 
/*session will stay alive for 10 seconds if user remains idle */
	if(!isset($_SESSION)){
    	session_start();
	}
	$duration=3600; /*3600 seconds */
	$dbServername="localhost";
	$dbUsername="root";
	$dbPassword="12345";
	$dbName="ecommence"; 
	if(isset($_POST['submit'])){
		if (isset($_SESSION['isCustomerLogin'])){
			$duration=$_SESSION['isCustomerLogin']['duration'];
			$start =$_SESSION['isCustomerLogin']['start'];
			echo time()-$start;
			echo "<br>";
			echo $duration;
			echo "<br>";
			
			if ((time()-$start)>$duration){
				echo "<p> YOU OUT </p>";
				unset($_SESSION['isCustomerLogin']['duration']);
				unset($_SESSION['isCustomerLogin']['start']);
				unset($_SESSION['isCustomerLogin']['logintype']);
				unset($_SESSION['isCustomerLogin']['Id']);
				unset($_SESSION['isCustomerLogin']);
				session_destroy();
			}		
			else{
				echo "You have to logout of the current user first";
				header( "refresh:5;url=customerLoggedin.php?statust=error&msg=you should logout first!" );	
				exit();
			}
		}		
		$conn=mysqli_connect($dbServername,$dbUsername,$dbPassword,$dbName);			
		if ($conn->connect_error) {
			die("Connection failed: " . $conn->connect_error);
		}

		$uid=mysqli_real_escape_string($conn,$_POST["uid"]);
		$password=mysqli_real_escape_string($conn, $_POST["password"]);
		
		$sql_customer= "SELECT * FROM customer WHERE CustomerId". " = ".$uid;
		$sql_seller= "SELECT * FROM seller WHERE SellerId". " = ".$uid;
		$sql_employee= "SELECT * FROM employee WHERE EmployeeId". " = ".$uid;
		
		
		$result_customer=mysqli_query($conn,$sql_customer);
		$resultCheck_customer=mysqli_num_rows($result_customer);
		
		$result_seller=mysqli_query($conn,$sql_seller);
		$resultCheck_seller=mysqli_num_rows($result_seller);
		
		$result_employee=mysqli_query($conn,$sql_employee);
		$resultCheck_employee=mysqli_num_rows($result_employee);
		
		if($resultCheck_customer==1){
				$obj = $result_customer->fetch_object();
				if ($password==$obj->Password){
					echo "<P> Customer logged in successfully! </p>";
					echo "<P>Now directing you to search page! </p>";
					$_SESSION['isCustomerLogin']=array(
						"start"=>time(),
						"duration"=>$duration,
						"logintype"=>"CustomerLogin",
						"Id"=>($obj->CustomerId),
					);
					$_SESSION['productsAdded']=array();
					header( "refresh:3;url=customerLoggedin.php" );				
					exit();
				}
				else {
					echo "Wrong password, try agin!";
					header( "refresh:2;url=login.php" );	
					exit();
				}
		}
		else if($resultCheck_seller==1){
				$obj = $result_seller->fetch_object();					
				if ($password==$obj->Password){
					echo "<P> Seller logged in successfully!!! </p>";
					echo "<P>Now redirecting you to seller page! </p>";
					$_SESSION['isSellerLogin']=array(
						"start"=>time(),
						"duration"=>$duration,
						"logintype"=>"SellerLogin",
						"Id"=>($obj->SellerId),
					);
					header( "refresh:3;url=sellerLoggedin.php" );				
					exit();
				}
				else{
					echo "Wrong password, try agin!";
					header( "refresh:2;url=login.php" );	
					exit();
				}
		}
		else if ($resultCheck_employee==1){
				$obj = $result_employee->fetch_object();
				if ($password==$obj->Password){
					echo "<P> Employee Login successfully!!! </p>";
					echo "<P>Now directing you to employee page!!! </p>";
					$_SESSION['isEmployeeLogin']=array(
						"start"=>time(),
						"duration"=>$duration,
						"logintype"=>"EmployeeLogin",
						"Id"=>($obj->EmployeeId),
					);
					header( "refresh:3;url=employeeLoggedin.php" );				
					exit();	
				}
				else {
					echo "wrong password, try agin!";
					header( "refresh:2;url=login.php" );	
					exit();
				}
		}
		else{			
			echo "Account does not exist, try agin!";
			header( "refresh:2;url=login.php" );	
			exit();
		} 
	}
	else{
		header("Location: login.php?status=error&msg=Please login");
	}
	
	include_once 'footer.php';
?>