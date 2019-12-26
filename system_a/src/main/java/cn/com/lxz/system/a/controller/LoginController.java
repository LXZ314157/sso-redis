package cn.com.lxz.system.a.controller;

import cn.com.lxz.system.a.common.Constant;
import cn.com.lxz.system.a.domain.Token;
import cn.com.lxz.system.a.service.UserService;
import cn.com.lxz.system.a.util.AESUtil;
import cn.com.lxz.system.a.util.TokenUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class LoginController {

    @Value("${redis.cookiekey}")
    private String cookiekey;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private UserService userService;

    @GetMapping("/index")
    public String index(){
        return "index";
    }


    @RequestMapping("/checklogin")
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response, String username, String password){
        HttpSession session = request.getSession();
        Cookie[] cookies = request.getCookies();
        if(cookies!=null && cookies.length>0){
            for(Cookie cookie : cookies){
                String cookiName = cookie.getName();
                if(cookiName.equals(cookiekey)){
                    ListOperations listOperations = redisTemplate.opsForList();
                    List<String> list =  listOperations.range(Constant.REDISUSER,0,-1);
                    if(list.contains(cookie.getValue())){
                        Token tokenUser = JSON.parseObject(AESUtil.decrypt(cookie.getValue(),Constant.TOKEN_SECRETKEY),Token.class);
                        if(tokenUser!=null){
                            if(cookiName.equals(tokenUser.getUsername())){
                                if(session.getAttribute(Constant.SESSIONUSER) == null){
                                    session.setAttribute(Constant.SESSIONUSER,tokenUser);
                                }
                                return new ModelAndView("success");
                            }
                        }
                    }
                    break;
                }
            }
        }
        if(!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)){
            int count = userService.checkUser(username,password);
            if(count>0){
                String newToken = TokenUtil.generateToken(username,password,Constant.TOKEN_SECRETKEY);
                //本地添加cookie
                Cookie cookie = new Cookie(cookiekey,newToken);
                cookie.setDomain("localhost");
                cookie.setPath("/");
                cookie.setValue(newToken);
                response.addCookie(cookie);

                //redis添加user
                ListOperations listOperations = redisTemplate.opsForList();
                List<String> list =  listOperations.range(Constant.REDISUSER,0,-1);
                if(!list.contains(newToken)){
                    redisTemplate.opsForList().leftPush(Constant.REDISUSER,newToken);
                }
                //session添加user
                Token tokenUser = new Token();
                tokenUser.setUsername(username);
                tokenUser.setPassword(password);
                request.getSession().setAttribute(Constant.SESSIONUSER,tokenUser);
                return new ModelAndView("success");
            }
        }
        return new ModelAndView("index","data","");
    }


}
