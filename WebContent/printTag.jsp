<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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

<link rel="stylesheet" type="text/css" href="/green.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Docker Tag Overview</title>
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
<div id="father">
<h1>The image use <label id="fathername"></label></h1>
<p><label id="fathername2"></label> has a popularity of <label id="fatherpop"></label>, <label id="fatherstab"></label> images can influence it</p>
<p>Find the image on <label id="fatherdh"></label> and <label id="fatherimlay"></label></p>
</div>
<div id="node">
</div>
<div id="children">
<h1>Used by</h1>
<p>Children nodes are influenced by <label id="stability"></label> images</p>
<p class="listheader2">Tag Name<span>ImageLayers</span><span>DockerHub</span><span>Popularity</span></p>
<ul id="ul3">
</ul>
</div>
<script type="text/javascript">
var json=<c:out value="${message}" escapeXml="false"/>
var j=JSON.parse(json);
if(j.father!=null)
{
	var name="<a href=\"/rest/res/"+j.father.user+"/"+j.father.repo+"/"+j.father.tag+"\">"+j.father.name+"</a>";
	document.getElementById("fathername").innerHTML+=name;
	document.getElementById("fathername2").innerHTML+=name;
	document.getElementById("fatherpop").innerHTML+=j.father.pagerank;
	document.getElementById("fatherimlay").innerHTML+="<a href=\""+j.father.imagelayers+"\">ImageLayers</a>";
	document.getElementById("fatherstab").innerHTML+=j.father.betweeness;
	document.getElementById("fatherdh").innerHTML+="<a href=\""+j.father.dockerhub+"\">DockerHub</a>";
}
else
{
	var el = document.getElementById("father");
	el.parentNode.removeChild( el );
}
document.getElementById("node").innerHTML+="<p><h1>"+j.name+"</h1></p>";
document.getElementById("node").innerHTML+="<p>Find this image on <a href=\""+j.dockerhub+"\">Dockerhub</a> and <a href=\""+j.imagelayers+"\">ImageLayers</a></p>";
document.getElementById("node").innerHTML+="<p>"+j.betweeness+" images can influence the image</p>";
document.getElementById("node").innerHTML+="<p>popularity is "+j.pagerank+"</p>";
var n=j.betweeness+1;
document.getElementById("stability").innerHTML+=n;
if(j.children.length==0)
{
	var el = document.getElementById("children");
	el.parentNode.removeChild( el );
}
else
{
	for(i=0;i<j.children.length;i++)
	{
		var dockerhub="<a href=\""+j.children[i].dockerhub+"\">Dockerhub</a>";
		var imlay="<a href=\""+j.children[i].imagelayers+"\">ImageLayers</a>";
		document.getElementById("ul3").innerHTML+="<li><p class=\"split-para2\">"+imlay+"<span id=\"name\"><a href=\"/rest/res/"+j.children[i].user+"\""+j.children[i].repo+"\""+j.children[i].tag+">"+j.children[i].name+"</a></span><span>"+j.children[i].pagerank+"</span><span>"+dockerhub+"</span>"+"</p></li>";
	}
}
</script>
</body>
</html>