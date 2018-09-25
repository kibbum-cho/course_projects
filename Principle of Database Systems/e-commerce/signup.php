<?php
	include_once'header.php';
	$signuptype = $_SESSION['selected_signup_type'];
	echo "<h1 class='h1class'> This is" ." $signuptype ". "signup </h1>";	
?>
<section class="main-container"> 
	<div class="main-wrapper">
		<h2>Sign up</h2>
		<form class="signup-form"  method="POST">
		<?php
			if(strcmp($signuptype,"Customer")==0 ||strcmp($signuptype,"Seller")==0 ){
				echo "<input type='text' name='uid' placeholder='customer 5XX seller 3XX' required>";
				echo "<input type='text' name='password' placeholder='at most 6 characters' required>";
				echo "<input type='text' name='firstname' placeholder='first name' required>";
				echo "<input type='text' name='lastname' placeholder='last name' required>";
				echo "<input type='text' name ='address' placeholder='address' required>";
				echo "<input type='text' name='email' placeholder='email address with @' required>";
				echo "<input type='text' name='phone' placeholder='phone number(without -)' required>";
				echo "<input type='submit' value='submit' name='submit' >";
			}
			else if (strcmp($signuptype,"Employee")==0){
				echo "<input type='text' name='uid' placeholder='int as 1XX' required>";
				echo "<input type='text' name='password' placeholder='at most 6 characters' required>";
				echo "<input type='text' name='firstname' placeholder='first name' required>";
				echo "<input type='text' name='lastname' placeholder='last name' required>";
				echo "<input type='text' name ='address' placeholder='address' required>";
				echo "<input type='text' name='email' placeholder='email address with @' required>";
				echo "<input type='text' name='phone' placeholder='phone number(without -)' required>";
				echo "<input type='text' name='role' placeholder='role' required>";
				echo "<input type='submit' value='submit' name='submit' >";
			}
		?>
			
		</form>
	</div>
</section>

<?php
	include_once "includes/signup.inc.php";
	include_once 'footer.php';
?>

