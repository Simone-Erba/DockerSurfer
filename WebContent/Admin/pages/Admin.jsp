<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<s:form action="/Admin/Logout" validate="true">
	<s:submit value="logout"/>
		</s:form>
<div id="div">
<table id="table">
   <tr>
    <th>Name</th>
    <th>email</th>
    <th>Occupation</th>
    <th>City</th>
    <th>Country</th>
  </tr>
</table>
</div>
<script>
var data=<s:property value="data" escape="false"/>;
var j=JSON.parse(data);
var array=j.data;
for(var i=0;i<array.length;i++)
	{
	document.getElementById("table").innerHTML+="<tr><td>"+array[i].name+"</td><td>"+array[i].email+"</td><td>"+array[i].occupation+"</td><td>"+array[i].city+"</td><td>"+array[i].country+"</td></tr>";
	}
</script>
</body>
</html>