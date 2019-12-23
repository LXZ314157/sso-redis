package cn.com.lxz.system.a;

import cn.com.lxz.system.a.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
public class LoginController {

    @Resource
    private UserService userService;

    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @RequestMapping("/login")
    public void login(String username,String password){
        int count = userService.checkUser(username,password);
        System.out.println(count);
    }


}
