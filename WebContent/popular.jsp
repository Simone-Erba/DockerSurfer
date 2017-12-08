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

<link rel="stylesheet" type="text/css" href="/DockerSurferWebApp/green.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Popular Docker Images</title>
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

<div id="pop">
<p class="plist">Most popular images</p>
<p class="listheader">Tag Name<span>Popularity</span></p>
<ul id="ulpop">

</ul>
</div>
<script type="text/javascript">
var json=<c:out value="${message}" escapeXml="false"/>
var j=JSON.parse(json);

for(i=0;i<j.length;i++)
{
	document.getElementById("ulpop").innerHTML+="<a href=\"/rest/res/"+j[i].user+"/"+j[i].repo+"/"+j[i].tag+"\"><li><p class=\"split-para\">"+j[i].name+"<span>"+j[i].pagerank+"</span></p></li></a>";
}
</script>
</body>
</html>