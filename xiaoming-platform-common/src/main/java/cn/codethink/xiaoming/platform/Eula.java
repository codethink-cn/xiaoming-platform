package cn.codethink.xiaoming.platform;

import lombok.Data;

/**
 * 小明最终用户许可协议
 *
 * @author Chuanwise
 */
@Data
public class Eula {
    
    /**
     * 是否同意最终用户使用许可
     */
    protected boolean eula = false;
    
    public static final String WEB_PAGE = "https://eula.xiaoming.codethink.cn";
    
    /**
     * 文件内容
     */
    public static final String AUTHORIZATION_CONTENT =
        "# Welcome to use xiaoming platform!\n" +
        "# By changing the setting below to TRUE you are indicating your agreement to our EULA (" + WEB_PAGE + ").\n" +
        "eula: false";
}
