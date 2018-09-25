<?php
	include_once 'header.php'; // header.php has home 
?>
<section class="main-container"> 
	<div class="main-wrapper">
		<h2 class="h2_class">Home</h2> </div>
		
		</style>
		</section>
		<div class="login_class">Login as Customer, Seller, or Employee?
				<select class="select-class", name="login-type">
					<option value="">Select...</option>
					<option value="Customer">Customer</option>
					<option value="Seller"> Seller </option>
					<option value="Employee">Employee</option>
				</select>
		<style>
			.select-class{	
				margin-top:30px;
				padding-right:20px;
				padding-left:20px;
			}
			.login_class{
			background-color: tomato;
			height:100px;
			color: white;
			margin:20px;
			padding: 20px;
			font-size:30px;
			
			text-align:center;
			vertical-align:middle;
			
			} 
	</style>
	<?php	if($_POST['submit'])
		{
			$logintype=$_POST['login-type'];
			echo"yo login type is ";
			echo "$logintype";
		}
	?>
	
	
	</div>
		<div class="signupclass">Sign up as Customer, Seller, or Employee?
				<select class="signuptype">
					<option value="">Select...</option>
					<option value="Customer">Customer</option>
					<option value="Seller"> Seller </option>
					<option value="Employee">Employee</option>
				</select>
		<style>
			.signuptype{	
				margin-top:30px;
				padding-right:20px;
				padding-left:20px;
			}
			.signupclass{
			background-color: tomato;
			height:100px;
			color: white;
			margin:20px;
			padding:20px;
			font-size:30px;
			
			text-align:center;
			vertical-align:middle;
			
			} 
	</style>
		</div>
  

<?php
	
	include_once 'footer.php';
?>