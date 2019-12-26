package cn.com.lxz.system.b.authorization.interceptor;

import cn.com.lxz.system.b.authorization.annotation.Authorization;
import cn.com.lxz.system.b.common.Constant;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod)handler;
        Method method = handlerMethod.getMethod();
        if (handlerMethod.getBeanType().getAnnotation(Authorization.class) != null || method.getAnnotation(Authorization.class) != null) {
            HttpSession sessioin = request.getSession();
            if(sessioin.getAttribute(Constant.SESSIONUSER) == null){
                //跳转到登录页面
                response.sendRedirect(request.getContextPath()+"/");
                return false;
            }
        }
        return true;
    }
}