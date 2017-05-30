<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Repository</title>
<script type="text/javascript" src="http://code.jquery.com/jquery-1.7.1.min.js"></script>
</head>
<body>
<script>
function getParam(variable)
{
	var query=window.location.search.substring(1);
	var vars=query.split("&");
	for(var i=0;i<vars.length;i++)
	{
		var pair=vars[i].split("=");
		if(pair[0]==variable)
		{
			return pair[1];	
		}
	}
}
$.getJSON( "./JSONReader", function( data ) {	

var user=getParam("user");
var arr=[];
var i=0;

for(i=0;i<data.length;i++)
{
	var repo=data[i];
	arr[i]=repo;	
	i++;
}
document.write("<ul id=\"list\" style=\"list-style-type:disc\">");
for(var i=0;i<arr.length;i++)
{
 	document.write("<li><a href=\MainServlet?user="+user+"&&repo="+arr[i]+">"+arr[i]+"</a><\li>");
}
document.write("</ul>");
});
</script>


</body>
</html>