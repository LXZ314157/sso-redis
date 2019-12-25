package cn.com.lxz.system.a.controller;

import cn.com.lxz.system.a.common.Constant;
import cn.com.lxz.system.a.domain.Token;
import cn.com.lxz.system.a.service.UserService;
import cn.com.lxz.system.a.util.AESUtil;
import cn.com.lxz.system.a.util.TokenUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @Value("${redis.cookiekey}")
    private String cookiekey;

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private UserService userService;

    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @RequestMapping("/checklogin")
    public ModelAndView login(HttpServletRequest request, String username, String password){
        Cookie[] cookies = request.getCookies();
        if(cookies!=null && cookies.length>0){
            for(Cookie cookie : cookies){
                String cookiName = cookie.getName();
                if(cookiName.equals(cookiekey)){
                    if(redisTemplate.opsForValue().get(cookiekey)!=null){
                        String encookie = redisTemplate.opsForValue().get(cookiekey).toString();
                        Token tokenUser = JSON.parseObject(AESUtil.decrypt(encookie,Constant.TOKEN_SECRETKEY),Token.class);
                        if(tokenUser!=null){
                            if(cookiName.equals(tokenUser.getUsername())){
                                request.getSession().setAttribute(Constant.SESSIONUSER,tokenUser);
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
                redisTemplate.opsForValue().set(cookiekey,newToken);
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
