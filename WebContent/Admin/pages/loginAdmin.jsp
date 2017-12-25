<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

	<s:form action="ProcessLoginAdmin">
		<s:password name="password" label="Password" />
		<s:submit />
	</s:form>
</body>
</html>