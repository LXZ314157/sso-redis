package cn.com.lxz.system.a.util;

import cn.com.lxz.system.a.domain.Token;
import com.alibaba.fastjson.JSON;

/**
 * @author lvxuezhan
 * @date 2019/6/15
 **/
public class TokenUtil {

    /**
     * username、password、时间戳和秘钥生成token
     * @param username
     * @param password
     * @param key
     * @return
     */
    public static String generateToken(String username,String password,String key){
        Token token = new Token();
        token.setUsername(username);
        token.setPassword(password);
        token.setTimestamp(DateUtil.getTimestamp());
        return AESUtil.encrypt(JSON.toJSONString(token),key);
    }

}
