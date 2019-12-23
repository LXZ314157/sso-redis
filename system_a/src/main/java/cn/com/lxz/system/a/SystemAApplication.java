package cn.com.lxz.system.a;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.com.lxz.system.a.mapper")
public class SystemAApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemAApplication.class, args);
    }

}
