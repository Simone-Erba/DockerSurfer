<html>
<head>
<meta charset="ISO-8859-1">
<title>Docker Surfer</title>
<link rel="stylesheet" type="text/css" href="Style.css">
</head>
<body>
	<h2>Hello World!</h2>
	<form action="./MainServlet" method="post">
			<h3>Search for an user:</h3><br>
		<input name="user" type="text"><br>
		<h3>Search for a repository:</h3> <br>
		<input name="repo" type="text"><br>

		<h3>Search for a tag:</h3><br>
		<input name="tag" type="text"><br>
		<br>
		<button id="invia" type="submit">Send</button>
		
	</form>
	<form action="./MainServlet" method="post">
	<input type="submit" value="popular" name="popular" class="searchPopular">
	</form>	
</body>
</html>
