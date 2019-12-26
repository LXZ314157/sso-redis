package cn.com.lxz.system.b.controller;


import cn.com.lxz.system.b.common.Constant;
import cn.com.lxz.system.b.domain.TokenUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/")
public class IndexController {

    @GetMapping
    public String index(HttpServletRequest request){

        HttpSession session = request.getSession();
        if(session.getAttribute(Constant.SESSIONUSER)!=null){
            TokenUser token = (TokenUser)session.getAttribute(Constant.SESSIONUSER);
            System.out.println(token.getUsername());
            System.out.println(token.getPassword());
            return "success";
        }else{
            return "redirect:/checklogin";
        }
    }
}
