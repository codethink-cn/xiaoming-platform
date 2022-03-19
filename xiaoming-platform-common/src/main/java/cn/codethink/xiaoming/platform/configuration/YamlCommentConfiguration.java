package cn.codethink.xiaoming.platform.configuration;

import lombok.Data;

/**
 * Yaml 文件注释配置
 *
 * @author Chuanwise
 */
@Data
public class YamlCommentConfiguration {
    
    /**
     * 是否在属性注解前插入空行
     */
    protected boolean insertSpaceLineBeforeFieldComment = true;
    
    /**
     * 是否在属性注解后插入空行
     */
    protected boolean insertSpaceLineAfterFieldComment = true;
    
    /**
     * 是否在属性注解前插入空行
     */
    protected boolean insertSpaceLineBeforeNonCommentField = true;
    
    /**
     * 是否在属性注解后插入空行
     */
    protected boolean insertSpaceLineAfterNonCommentField = true;
}
