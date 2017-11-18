<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="/DockerSurferWebApp/gnod.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
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
document.getElementById("name").innerHTML+="<a href=\"/DockerSurferWebApp/rest/res/"+j.user+"\">"+j.name+"</a>";
document.getElementById("pop").innerHTML+=+j.popularity;
document.getElementById("docker").innerHTML+="<a href=\""+j.dockerhub+"\">Find this image on Dockerhub</a>";
for(i=0;i<j.tags.length;i++)
{
	document.getElementById("ul2").innerHTML+="<a href=\"/DockerSurferWebApp/rest/res/"+j.tags[i].user+"/"+j.tags[i].repo+"/"+j.tags[i].tag+"\"><li><p class=\"split-para\">"+j.tags[i].tag+"<span>"+j.tags[i].pagerank+"</span></p></li></a>";
}
</script>
</body>
</html>