package cn.com.lxz.system.b.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface UserMapper {

    int checkUser(@Param("username") String username, @Param("password") String password);

}
