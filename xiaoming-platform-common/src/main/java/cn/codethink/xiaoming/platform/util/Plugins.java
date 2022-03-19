package cn.codethink.xiaoming.platform.util;

import cn.codethink.common.util.Preconditions;
import cn.codethink.common.util.StaticUtilities;
import cn.codethink.xiaoming.VersionBranch;
import cn.codethink.xiaoming.platform.plugin.Plugin;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

/**
 * 插件工具类
 *
 * @author Chuanwise
 */
public class Plugins
    extends StaticUtilities {
    
    /**
     * 计算插件 UUID
     *
     * @param packageName 包名
     * @param pluginName  插件名
     * @param version     版本号
     * @return 插件 UUID
     */
    public static UUID calculateUUID(String packageName, String pluginName, String version, VersionBranch versionBranch) {
        Preconditions.namedArgumentNonEmpty(packageName, "package name");
        Preconditions.namedArgumentNonEmpty(pluginName, "plugin name");
        Preconditions.namedArgumentNonEmpty(version, "version");
        Preconditions.namedArgumentNonNull(versionBranch, "version branch");
        
        // 获取 UTF-8 Code
        final byte[] packageNameBytes = packageName.getBytes(StandardCharsets.UTF_8);
        final byte[] pluginNameBytes = pluginName.getBytes(StandardCharsets.UTF_8);
        final byte[] versionBytes = version.getBytes(StandardCharsets.UTF_8);
        final byte[] versionBranchBytes = versionBranch.toString().getBytes(StandardCharsets.UTF_8);
    
        // 合并到一个数组里
        final byte[] mergedBytes = new byte[packageNameBytes.length + pluginNameBytes.length + versionBytes.length + versionBranchBytes.length];
        System.arraycopy(packageNameBytes, 0, mergedBytes, 0, packageNameBytes.length);
        System.arraycopy(pluginNameBytes, 0, mergedBytes, packageNameBytes.length, pluginNameBytes.length);
        System.arraycopy(versionBytes, 0, mergedBytes, packageNameBytes.length + pluginNameBytes.length, versionBytes.length);
        System.arraycopy(versionBranchBytes, 0, mergedBytes, packageNameBytes.length + pluginNameBytes.length + versionBytes.length, versionBranchBytes.length);
        
        // 生成 UUID
        return UUID.nameUUIDFromBytes(mergedBytes);
    }
    
    /**
     * 获取插件名
     *
     * @param plugin      插件
     * @param nullDefault 当插件为 null 时的返回值
     * @return 插件名
     */
    public static String getPluginName(Plugin plugin, String nullDefault) {
        if (Objects.nonNull(plugin)) {
            return plugin.getPluginHandler().getMetadata().getName();
        } else {
            return nullDefault;
        }
    }
    
    /**
     * 获取插件名
     *
     * @param plugin 插件
     * @return 当插件为 null，返回「小明平台」
     */
    public static String getPluginName(Plugin plugin) {
        return getPluginName(plugin, "小明平台");
    }
}