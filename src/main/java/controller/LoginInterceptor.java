package controller;

import java.util.Map;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class LoginInterceptor  extends AbstractInterceptor {
private String email;

		public String getEmail() {
	return email;
}

public void setEmail(String email) {
	this.email = email;
}

		@Override
        public String intercept(ActionInvocation invocation) throws Exception {
                Map<String, Object> session = invocation.getInvocationContext().getSession();
    
                String loginId = (String) session.get("loginUser");
                email=loginId;
                if (loginId == null) 
                {
                	System.out.println("User access denied");
                        return Action.LOGIN;
                } 
                else 
                {
                	System.out.println("User allowed");
                        return invocation.invoke();
                }
        }
}