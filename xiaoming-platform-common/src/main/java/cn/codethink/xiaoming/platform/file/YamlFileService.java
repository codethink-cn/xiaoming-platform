package cn.codethink.xiaoming.platform.file;

import cn.codethink.common.util.Objects;
import cn.codethink.xiaoming.platform.Platform;
import cn.codethink.xiaoming.platform.configuration.FileServiceConfiguration;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

/**
 * @see cn.codethink.xiaoming.platform.file.FileService
 * @author Chuanwise
 */
public class YamlFileService
    extends AbstractFileService
    implements FileService {
    
    private final Yaml yaml;
    
    public YamlFileService(Platform platform, FileServiceConfiguration configuration) {
        super(platform, configuration);
    
        final Representer nullIgnoreRepresent = new Representer() {
            @Override
            protected NodeTuple representJavaBeanProperty(Object javaBean, Property property, Object propertyValue, Tag customTag) {
                // if value of property is null, ignore it.
                if (Objects.isEmpty(propertyValue)) {
                    return null;
                } else {
                    return super.representJavaBeanProperty(javaBean, property, propertyValue, customTag);
                }
            }
        };
        yaml = new Yaml(new CustomClassLoaderConstructor(platform.getGlobalClassLoader()), nullIgnoreRepresent);
        
        yaml.setBeanAccess(BeanAccess.FIELD);
    }
    
    @Override
    public Object load(String string) {
        final Object load = yaml.load(string);
    
        if (load instanceof Storable) {
            final Storable storable = (Storable) load;
            
            storable.setFileService(this);
        }
        
        return load;
    }
    
    @Override
    public <T> T load(Class<T> fileClass, String string) {
        final T t = yaml.loadAs(string, fileClass);
    
        if (t instanceof Storable) {
            final Storable storable = (Storable) t;
        
            storable.setFileService(this);
        }
    
        return t;
    }
    
    @Override
    public String dump(Object object) {
        return yaml.dumpAsMap(object);
    }
}
