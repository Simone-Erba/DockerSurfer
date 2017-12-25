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
<title>Docker Repository overview</title>
</head>
<body>
<div class="header">
<label class="home">
<a href="/index.html">Home</a>
</label>
<label class="docs">
<a href="/docs/overview-summary.html">Documentation</a>
</label>
<label class="github">
<a href="https://github.com/Simone-Erba/DockerSurfer">Github</a>
</label>
<label class="github">
<a href="/User/UserHome.action">Sign in</a>
</label>
</div>
<div id="repo">
<h1><label id="name"></label></h1>
<p>Popularity: <label id="pop"></label></p>
<p><label id="docker"></label></p>
</div>
<div id="tags">
<p class="listheader">Tag Name<span>Popularity</span></p>
<ul id="ul2">
</ul>
</div>
<script type="text/javascript">
var json=<c:out value="${message}" escapeXml="false"/>
var j=JSON.parse(json);
document.getElementById("name").innerHTML+="<a href=\"/rest/res/"+j.user+"\">"+j.name+"</a>";
document.getElementById("pop").innerHTML+=+j.popularity;
document.getElementById("docker").innerHTML+="<a href=\""+j.dockerhub+"\">Find this image on Dockerhub</a>";
for(i=0;i<j.tags.length;i++)
{
	document.getElementById("ul2").innerHTML+="<a href=\"/rest/res/"+j.tags[i].user+"/"+j.tags[i].repo+"/"+j.tags[i].tag+"\"><li><p class=\"split-para\">"+j.tags[i].tag+"<span>"+j.tags[i].pagerank+"</span></p></li></a>";
}
</script>
</body>
</html>