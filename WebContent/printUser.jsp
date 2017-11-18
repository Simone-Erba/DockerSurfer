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
<div id="user">
<h1><label id="name"></label></h1>
<p>This user on<label id="dockerhub"></label></p>
</div>
<div id="images">
<p class="plist">User Repositories</p>
<ul id="ul1">
</ul>
</div>
<script type="text/javascript">
var json=<c:out value="${message}" escapeXml="false"/>
var j=JSON.parse(json);
document.getElementById("name").innerHTML+=j.name;
document.getElementById("dockerhub").innerHTML+="<a href=\""+j.dockerhub+"\"> Dockerhub</a>";
for(i=0;i<j.l.length;i++)
{
	document.getElementById("ul1").innerHTML+="<a href=\"/DockerSurferWebApp/rest/res/"+j.l[i].name+"\"><li>"+j.l[i].repo+"</li></a>";
}
</script>
</body>
</html>