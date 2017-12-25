<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head></head>
<body>

	<s:form action="ProcessRegistration" validate="true">
		<s:textfield name="email" label="Email" requiredLabel="true"/>
		<s:password name="password" label="Password" requiredLabel="true"/>
		<s:textfield name="name" label="name" requiredLabel="true"/>
		<s:textfield name="occupation" label="Occupation" requiredLabel="true"/>
		<s:textfield name="city" label="city" requiredLabel="true"/>
		<s:textfield name="country" label="country" requiredLabel="true"/>
		<s:submit />
	</s:form>

</body>
</html>