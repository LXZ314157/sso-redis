package cn.com.lxz.system.b;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.com.lxz.system.b.mapper")
public class SystemBApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemBApplication.class, args);
    }

}
