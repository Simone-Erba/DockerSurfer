<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head></head>
<body>
	<h1>User Page</h1>
	<s:form action="/User/Logout" validate="true">
		<s:textfield name ="email" id="hidden" escape="false" hidden="true"/>
		<s:textfield name ="password" id="pass" value="whatever" escape="false" hidden="true"/>
	<s:submit value="logout"/>
	</s:form>
	Images you follow:
	<table id="table">
	<tr>
	<th>Name</th>
    <th>popularity</th>
    <th>stability</th>
    <th>stop follow</th>
    </tr>
	</table>

</body>
<script>
var mail=<s:property value="email" escape="false"/>;
document.getElementById("hidden").value=mail;
var mess=<s:property value="mess" escape="false"/>;
var data=JSON.parse(mess);
var array=data.data;
var table=document.getElementById("table");
for(var i=0;i<array.length;i++)
	{
	table.innerHTML+="<tr><td>"+array[i].name+"</td><td>"+array[i].pagerank+"</td><td>"+array[i].betweeness+"</td><td><a href=\"./StopFollowing\">Unfollow</a></td></tr>";
	}
</script>
</html>