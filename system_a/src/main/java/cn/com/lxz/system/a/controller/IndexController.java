package cn.com.lxz.system.a.controller;


import cn.com.lxz.system.a.common.Constant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/")
public class IndexController {

    @GetMapping
    public String index(HttpServletRequest request){
        if(request.getSession().getAttribute(Constant.SESSIONUSER)!=null){
            return "success";
        }else{
            return "redirect:/checklogin";
        }
    }
}
