package cn.codethink.xiaoming.platform.util;

import cn.chuanwise.common.util.Reflections;
import cn.codethink.common.util.Preconditions;
import cn.codethink.common.util.StaticUtilities;
import cn.codethink.common.util.Strings;
import cn.codethink.xiaoming.platform.annotation.Comment;
import cn.codethink.xiaoming.platform.configuration.FileServiceConfiguration;
import cn.codethink.xiaoming.platform.configuration.YamlCommentConfiguration;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Yaml 注释工具
 *
 * @author Chuanwise
 */
public class YamlComments
    extends StaticUtilities {
    
    public static String addComments(String yaml, Class<?> dataClass) {
        Preconditions.namedArgumentNonNull(yaml, "yaml");
        return null;
    }
    
    public static String addComments(InputStream inputStream, Class<?> dataClass) {
        return null;
    }
    
    public static List<String> commented(List<String> yaml, Class<?> dataClass, YamlCommentConfiguration configuration) {
        Preconditions.namedArgumentNonNull(yaml, "yaml");
        Preconditions.namedArgumentNonNull(dataClass, "data class");
        Preconditions.namedArgumentNonNull(configuration, "configuration");
        
        final Stack<Class<?>> classStack = new Stack<>();
        classStack.add(dataClass);
        
        final List<String> results = new ArrayList<>(yaml.size());
        
        // 检查头注释
        for (int i = 0; i < yaml.size(); i++) {
//            Strings.indexOfExcludedCharacters()
        }
        
        return null;
    }
    
    private static List<String> appendSpaceLineIfNonEmpty(List<String> results) {
        if (!results.isEmpty()) {
            results.add("");
        }
        return results;
    }
}
