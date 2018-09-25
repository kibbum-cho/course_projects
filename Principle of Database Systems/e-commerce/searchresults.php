<?php
	include_once"header.php";
	$dbServername="localhost";
	$dbUsername="root";
	$dbPassword="12345";
	$dbName="ecommence"; 
?>	
<section class="main-container"> 
	<div class ="search_class">New search
	<form name ='form1' method ='POST'	action='searchresults.php'>
	<input name = 'search' type='text' size='40' maxlength='50'/>
	<input type = 'submit' name ='submit' value='search' />
	</form>
	</div>
	<style>
		.search_class{
			background-color: tomato;
			height:50px;
			color: white;
			margin:10px;
			padding: 10px;
			font-size:20px;			
			text-align:center;
			vertical-align:middle;
	} 
	</style>
</section>



<?php 
	echo "<div class ='wrapper'>";
	echo "<div class='resultsdive'>";
	
	if(isset($_POST['search'])){		
		$conn=mysqli_connect($dbServername,$dbUsername,$dbPassword,$dbName);
		$ItemName= $_POST['search'];
		$sql= "SELECT * FROM inventory WHERE ItemName LIKE  '%$ItemName%';";
		$result=mysqli_query($conn,$sql);
		if(mysqli_num_rows($result)!=0){
			echo "<form method='POST' action= 'addShoppingCart.php'>";
			$count=0;
			$row=mysqli_fetch_array($result);			
			$myData=array();
			echo "<p class='searchresults'>Search results </p></br>";
			echo "<table style='width:80%'>";
			echo "<tr>";
			echo "<th>Check</th>";
			echo "<th>Item Name</th>";
			echo "<th>Price</th>"; 
			echo "<th>Quantity</th>";
			echo "</tr>";	
			do{					
				$dataElement=array(
					$row['SellerId'],
					$row['ItemId'],
					$row['ItemName'],
					$row['Quantity'],
					$row['Price']
				);

				array_push($myData, $dataElement);
				$_SESSION['itemid'] = $dataElement[1];
				echo "<tr>";
				echo "<th><input type = 'checkbox' method='POST' name ='checkbox[]'.$count. value=$count /></th>";
				echo "<th><a href='detail.php?itemid=$dataElement[1]'>{$row['ItemName']}</a></th><th>".$row['Price']. '</th><th>'.$row['Quantity']."</th>";
				echo "</tr>";
				$count=$count+1;		
			 }while($row=mysqli_fetch_array($result));
			echo "</table>";
			echo "<input type='submit' name= 'addToCart' value='add to cart' >";	
			$_SESSION['searchResults']=$myData;
					
		}
		else{
			echo "No search products found";
		}
	}

    echo "<style>";
    echo ".resultsdive{display:block; background-color: tomato; height:300px; color: white; margin:10px; padding:10px; font-size:20px; text-align:center vertical-align:middle;autoscroll:true;}table, th, td {
        border: 2px solid white;
        vertical-align:middle;
        padding: 2px;}";
    echo "</styple>";
    echo "</form>";
    echo "</div>";
    echo "</div>";

include_once 'footer.php'; ?>
