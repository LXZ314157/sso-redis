package cn.com.lxz.system.b.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zjg
 * @date 2018/8/8 17:34
 * @Description 角色实体类
 */
@Data
public class TbRole implements Serializable {

    private int id;
    /**
     * 用户角色名称
     */
    private String roleName;
    /**
     * 用户角色描述
     */
    private String describe;

    public TbRole(String roleName, String describe) {
        this.roleName = roleName;
        this.describe = describe;
    }

    public TbRole() {}
}
