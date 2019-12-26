package cn.com.lxz.system.a.domain;

import lombok.Data;

@Data
public class TokenUser {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * tokenId
     */
    private String tokenId;

}
