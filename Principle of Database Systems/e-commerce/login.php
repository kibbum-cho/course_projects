<?php
	include_once 'header.php'; // header.php has home 
?>

<div class ="main-wrapper">
	<form class ="signin-form"  method ="POST" action='login_auth.php'>
		<div class="container">
		<label><b>ID</b></label>
		<input type="text" placeholder="Enter ID" name="uid" required>
		<input type="text" placeholder="Enter password" name="password" required>
		<input type="submit" value="submit" name="submit" >
		</div>
	</form>	
<style>
	.container{
	margin:40px;
	}
</style>
</div>	

<?php
	include_once 'footer.php';
?>