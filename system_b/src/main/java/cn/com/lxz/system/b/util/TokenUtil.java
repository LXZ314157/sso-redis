package cn.com.lxz.system.b.util;

import cn.com.lxz.system.b.domain.TokenUser;
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
        TokenUser token = new TokenUser();
        token.setUsername(username);
        token.setPassword(password);
        token.setTokenId(DateUtil.getTimestamp());
        return AESUtil.encrypt(JSON.toJSONString(token),key);
    }

}
