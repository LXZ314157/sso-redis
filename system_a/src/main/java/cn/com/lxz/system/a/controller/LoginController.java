package cn.com.lxz.system.a.controller;

import cn.com.lxz.system.a.common.Constant;
import cn.com.lxz.system.a.domain.TokenUser;
import cn.com.lxz.system.a.service.UserService;
import cn.com.lxz.system.a.util.AESUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
public class LoginController {

    @Value("${redis.cookiekey}")
    private String cookiekey;

    @Resource
    private RedisTemplate<String,String> redisTemplate;

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
                    String token = cookie.getValue();
                    String decryptjson = AESUtil.decrypt(token,Constant.TOKEN_SECRETKEY);
                    TokenUser tokenUser = JSONObject.parseObject(decryptjson,TokenUser.class);
                    String redisToken = redisTemplate.opsForValue().get(tokenUser.getTokenId());
                    if(token.equals(redisToken)){
                        if(session.getAttribute(Constant.SESSIONUSER) == null){
                            session.setAttribute(Constant.SESSIONUSER,tokenUser);
                        }
                        return new ModelAndView("success");
                    }
                    break;
                }
            }
        }

        //第一次登录
        if(!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)){
            int count = userService.checkUser(username,password);
            if(count>0){
                TokenUser tokenUser = new TokenUser();
                tokenUser.setUsername(username);
                tokenUser.setPassword(password);
                String randomkey = UUID.randomUUID().toString().toLowerCase().replace("-","");
                tokenUser.setTokenId(randomkey);

                String token = AESUtil.encrypt(JSON.toJSONString(tokenUser),Constant.TOKEN_SECRETKEY);
                //本地添加cookie
                Cookie cookie = new Cookie(cookiekey,token);
                cookie.setDomain("localhost");
                cookie.setPath("/");
                response.addCookie(cookie);

                //redis添加缓存--一分钟
                redisTemplate.opsForValue().set(randomkey,token,1,TimeUnit.MINUTES);

                //添加ession
                request.getSession().setAttribute(Constant.SESSIONUSER,tokenUser);
                return new ModelAndView("success");
            }
        }
        return new ModelAndView("index");
    }


    @RequestMapping("/logout")
    @ResponseBody
    public String logout(HttpServletRequest request,HttpServletResponse response){
        String cookieValue = "";
        //删除cookie
        Cookie [] cookies = request.getCookies();
        if(cookies!=null && cookies.length>0){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals(cookiekey)){
                    Cookie newCookie = new Cookie(cookie.getName(),null);
                    newCookie.setDomain("localhost");
                    newCookie.setPath("/");
                    newCookie.setMaxAge(0);
                    response.addCookie(newCookie);
                    cookieValue = cookie.getValue();
                    break;
                }
            }
        }
        //删除session
        request.getSession().removeAttribute(Constant.SESSIONUSER);

        //删除redis
        String decryptjson = AESUtil.decrypt(cookieValue,Constant.TOKEN_SECRETKEY);
        TokenUser tokenUser = JSONObject.parseObject(decryptjson,TokenUser.class);
        redisTemplate.delete(tokenUser.getTokenId());
        return "退出成功！";

    }

}
