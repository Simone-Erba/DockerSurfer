<html>
<head>
<!-- Global site tag (gtag.js) - Google Analytics -->
<script async src="https://www.googletagmanager.com/gtag/js?id=UA-110130167-1"></script>
<script>
  window.dataLayer = window.dataLayer || [];
  function gtag(){dataLayer.push(arguments);}
  gtag('js', new Date());

  gtag('config', 'UA-110130167-1');
</script>

<meta charset="ISO-8859-1">
<title>Docker Surfer</title>
<link rel="stylesheet" type="text/css" href="Style.css">
</head>
<body>
<div class="header">
<label class="home">
<a href="/index.html">Home</a>
</label>
<label class="docs">
<a href="/docs/index.html">Documentation</a>
</label>
<label class="github">
<a href="https://github.com/Simone-Erba/DockerSurfer">Github</a>
</label>
</div>
<h1>Docker Surfer</h1>

	<p>This is a tool for browse and analyze the Docker Registry. The tool show connections between images by looking at their layers. <br>
	The tool has a REST interface <b>/rest/res/&#60userName&#62/&#60repositoryName&#62/&#60tag&#62</b><br>
	<b>/rest/res/library</b>   will show the official images.<br>
	<b>/rest/res/library/tomcat</b>   will show all the tags for the tomcat image.<br>
	<b>/rest/res/library/tomcat/latest</b>   will show the dependency for the tag, the date when the data was updated, information about Page Rank and betweeness centrality indexes.<br>
	the Page rank index show the popularity of an image, the betweneess centality index show the image stability.
		 <br><br>	</p>
			<div align="center">
		<input name="user" type="text" id="u" size ="40" placeholder="User name" required><br>
		<input name="repo" type="text"  id="r" size ="40" placeholder="Repository name (optional)"><br>
		<input name="tag" type="text"  id="t" size ="40" placeholder="Tag name (optional)" ><br>
		<br>
		 <button  onclick="searchURL()">Search</button> 
		 <form action="./rest/popular/" method="get">
	<input type="submit" value="Popular images" name="popular" class="searchPopular">
	</form>
	</div>
	<br>
	<p>
	<br>
	You can keep track of the Page Rank and betweeness indexes to see changes in the popularity or in the stability of the images you are interested in.<br>
	You can for example notice when an image lose popularity. This can mean that a new bug has discovered or that a better image is created.<br>
	If you don't know how to use an image, you can see how other users use it.<br>
	You can also be aware of unexpected changes in the layers of the images that you are using by using the link to DockerHub and Image Layers.<br>
	Don't hesitate to write me at simone.erba.95@gmail.com to bring your ideas, point out problems or if you have questions.
	</p>
</body>
<script type="text/javascript">
  function searchURL(){
	  var user=document.getElementById('u').value;
	  var repo=document.getElementById('r').value;
	  var tag=document.getElementById('t').value;
	  if(user!=null&&repo!=null&&tag!=null)
		  {
   			 window.location = "/rest/res/"+user+"/"+repo+"/"+tag;
		  }
	  else
		  {
			  if(user!=null&&repo!=null)
			  {
					 window.location = "/rest/res/"+user+"/"+repo;
			  }
			  else
				  {
			  		if(user!=null)
			  		{
						 window.location = "/rest/res/"+user;
			  		}
				  }
		  }
  }
</script>
</html>
