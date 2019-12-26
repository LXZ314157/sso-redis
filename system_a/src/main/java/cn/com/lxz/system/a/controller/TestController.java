package cn.com.lxz.system.a.controller;


import cn.com.lxz.system.a.authorization.annotation.Authorization;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Authorization
@Controller
public class TestController {

    @GetMapping("/test")
    @ResponseBody
    public String test(){
        return "接口访问成功";
    }
}
