package cn.codethink.xiaoming.platform.plugin;

import lombok.Data;

import java.beans.Transient;
import java.io.File;

/**
 * 从 Jar 包中加载的插件的插件元数据
 *
 * @author Chuanwise
 */
@Data
public class JarPluginMetadata
        extends AbstractPluginMetadata {
    
    /**
     * 插件主类名
     */
    protected String main;
    
    /**
     * 插件文件
     */
    protected transient File file;
    
    @Transient
    public File getFile() {
        return file;
    }
}
