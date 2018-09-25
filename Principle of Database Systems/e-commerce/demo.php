<?php
	function get($name){
		return isset($_REQUEST[$name])? $_REQUEST[$name]:'';
	}
	function  is_valide_index($index,$array){
		return $index>=0 && $index < count($array);
	}
	function get_options($color){
		$options ='';
		for ($i=0;$i<count($color);$i++){
			$options .= '<option value="'. ($i+1).'">'.$color[$i]. '</option>';	
			
		}	
		return $options;
	}
	function print_stuff(){
		echo "you called me ";
	}
?>




<!DOCTYPE html>
<html>
	<head>
	</head>
	<body>
		<form>
		<?php
			
			$color=array('blue','green','red','yellow');
			echo '<select name="color">';
			echo get_options($color);
			echo '</select>';
		?>
		<input type='submit'>
		</form>
		<?php
			if (get('color')){
				$color_id=get('color');
							
				if (is_valide_index($color_id-1, $color)){
					echo "You have selected ". $color[$color_id-1]." color";
					if( strcmp ($color[$color_id-1],'red')==0){
						echo 'you selected red color ####';
						include_once 'red.php';
					}
				}
				else{
					echo '<span style = "color:red"> Invalid color code </span>';
				}
			}
			
		?>
	</body>
</html>
