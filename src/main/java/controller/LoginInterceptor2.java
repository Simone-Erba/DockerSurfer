package controller;

import java.util.Map;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class LoginInterceptor2  extends AbstractInterceptor {

        @Override
        public String intercept(ActionInvocation invocation) throws Exception {
                Map<String, Object> session = invocation.getInvocationContext().getSession();

                String loginId = (String) session.get("loginAdmin");

                if (loginId==null) 
                {
                 	System.out.println("Admin access denied");
                    return Action.LOGIN;
                } 
                else 
                {
                    System.out.println("Admin allowed");
                    return invocation.invoke();
                }
        }
}