<html>
<head>
<meta charset="ISO-8859-1">
<title>Docker Surfer</title>
<link rel="stylesheet" type="text/css" href="Style.css">
</head>
<body>
	<h3>Welcome</h3>
			<h3>Search for an user:</h3><br>
		<input name="user" type="text" id="u" required><br>
		<h3>Search for a repository:</h3> <br>
		<input name="repo" type="text"  id="r"><br>

		<h3>Search for a tag:</h3><br>
		<input name="tag" type="text"  id="t"><br>
		<br>
		 <button onclick="searchURL()">Send</button> 
	<form action="./popular" method="post">
	<input type="submit" value="popular" name="popular" class="searchPopular">
	</form>	
</body>
<script type="text/javascript">
  function searchURL(){
	  var user=document.getElementById('u').value;
	  var repo=document.getElementById('r').value;
	  var tag=document.getElementById('t').value;
	  alert(user);
	  alert(repo);
	  alert(tag);
	  if(user!=null&&repo!=null&&tag!=null)
		  {
   			 window.location = "/DockerSurferWebApp/rest/res/"+user+"/"+repo+"/"+tag;
		  }
	  else
		  {
			  if(user!=null&&repo!=null)
			  {
					 window.location = "/DockerSurferWebApp/rest/res/"+user+"/"+repo;
			  }
			  else
				  {
			  		if(user!=null)
			  		{
						 window.location = "/DockerSurferWebApp/rest/res/"+user;
			  		}
				  }
		  }
  }
</script>
</html>
