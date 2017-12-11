<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head></head>
<body>

	<s:form action="Registration">
		<s:textfield name="username" label="Username" />
		<s:password name="password" label="Password" />
		<s:password name="city" label="city" />
		<s:password name="country" label="country" />
		<s:password name="email" label="email" />
		<s:password name="name" label="name" />
		<s:submit />
	</s:form>

</body>
</html>