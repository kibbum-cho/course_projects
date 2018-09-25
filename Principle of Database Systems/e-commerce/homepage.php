<?php
	include_once 'header.php';

	if (isset($_SESSION['isCustomerLogin'])){
    	header("Location: customerLoggedin.php");
    	exit;
    } 
    else if (isset($_SESSION['isSellerLogin'])){
    	header("Location: sellerLoggedin.php");
    	exit;
    }
    else if (isset($_SESSION['isEmployeeLogin'])){
    	header("Location: employeeLoggedin.php");
    	exit;
    }

	function get($name){
		return isset($_REQUEST[$name])? $_REQUEST[$name]:'';
	}
	function  is_valide_index($index,$array){
		return $index>=0 && $index < count($array);
	}
	function get_options($logintype){
		$options ='';
		for ($i=0;$i<count($logintype);$i++){
			$options .= '<option value="'. ($i+1).'">'.$logintype[$i]. '</option>';				
		}	
		return $options;
	}
?>

<section class="main-container"> 
	<div class="main-wrapper">
		<h2 class="h2_class">Home</h2> 
	</div>
	<div class ="search_class">Search the products you want to buy 
	<form name ='form1' method ='POST'	action='searchresults.php'>
	<input name = 'search' type='text' size='40' maxlength='50'/>
	<input type = 'submit' name ='submit' value='Search' />
	</form>
	</div>
	<style>
		.search_class{
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
</section>
<div class="login_class">Login as a Customer, Seller, or Employee
		<form action=login.php>
		<input type ='submit' method='POST' name='submit' value='Login' >
		</form>
	
<style>
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
</div>

<div class="signupclass">Sign up as a Customer, Seller, or Employee
			<form>
			<?php
			$signuptype=array('Customer','Seller','Employee');
			echo '<select class="select-class", name="signup-type">';
			echo get_options($signuptype);
			echo '</select>';
			?>	
			<input type ='submit' value='Sign Up' >
			</form>
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
		<?php
			if (get('signup-type')){
				$signuptype_id=get('signup-type');
				if (is_valide_index($signuptype_id-1, $signuptype)){
					$_SESSION['selected_signup_type']='';
					echo "You have selected ". $signuptype[$signuptype_id-1]." type";
					if( strcmp ($signuptype[$signuptype_id-1],'Seller')==0){
						session_start();
						$_SESSION['selected_signup_type']=$signuptype[$signuptype_id-1];
						header("Location: signup.php");
					}
					else if( strcmp ($signuptype[$signuptype_id-1],'Customer')==0){
						session_start();
						$_SESSION['selected_signup_type']=$signuptype[$signuptype_id-1];
						header("Location: signup.php");
					}
					else if(strcmp($signuptype[$signuptype_id-1],'Employee')==0){
						session_start();
						$_SESSION['selected_signup_type']=$signuptype[$signuptype_id-1];
						header("Location: signup.php");
					}		
					
				}
				else{
					echo '<span style = "color:red"> Invalid login type code </span>';
				}
			}				
			
	?>	
	
		</div>
  

  

<?php
	
	include_once 'footer.php';
?>