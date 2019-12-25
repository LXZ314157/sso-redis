package cn.com.lxz.system.a.domain;

import lombok.Data;

/**
 * @author lvxuezhan
 * @date 2019/6/15
 * 封装token实体
 **/
@Data
public class Token {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 时间戳
     */
    private String timestamp;



}
