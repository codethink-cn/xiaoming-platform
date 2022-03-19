package cn.codethink.xiaoming.platform.file;

import cn.codethink.common.util.Preconditions;
import cn.codethink.xiaoming.logger.Logger;
import cn.codethink.xiaoming.logger.LoggerFactory;
import cn.codethink.xiaoming.platform.AbstractPlatformObject;
import cn.codethink.xiaoming.platform.Platform;
import cn.codethink.xiaoming.platform.configuration.FileServiceConfiguration;
import lombok.Data;

import java.io.*;
import java.nio.charset.Charset;
import java.util.function.Supplier;

/**
 * @see FileService
 * @author Chuanwise
 */
@Data
@SuppressWarnings("all")
public abstract class AbstractFileService
    extends AbstractPlatformObject
    implements FileService {
    
    protected final Logger logger;
    
    protected final FileServiceConfiguration configuration;
    
    public AbstractFileService(Platform platform, FileServiceConfiguration configuration) {
        super(platform);
        
        Preconditions.namedArgumentNonNull(configuration, "configuration");
        
        this.configuration = configuration;
        
        logger = platform.getPlatformConfiguration().getLoggerFactory().getLogger("file service");
    }
    
    @Override
    public Object load(File file, Charset charset) throws IOException {
        Preconditions.namedArgumentNonNull(file, "file");
        Preconditions.namedArgumentNonNull(charset, "charset");
        
        try (InputStream inputStream = new FileInputStream(file)) {
            final Object object = load(inputStream, charset);
    
            if (object instanceof Storable) {
                final Storable storable = (Storable) object;
    
                storable.setFile(file);
                storable.setCharset(charset);
            }
            
            return object;
        }
    }
    
    @Override
    public Object load(File file) throws IOException {
        return load(file, configuration.getCharset());
    }
    
    @Override
    public Object load(InputStream inputStream, Charset charset) throws IOException {
        Preconditions.namedArgumentNonNull(inputStream, "input stream");
        Preconditions.namedArgumentNonNull(charset, "charset");
        
        // read as string
        final int available = inputStream.available();
        final byte[] bytes = new byte[available];
        inputStream.read(bytes);
        
        // to string
        final String string = new String(bytes, charset);
        return load(string);
    }
    
    @Override
    public Object load(InputStream inputStream) throws IOException {
        return load(inputStream, configuration.getCharset());
    }
    
    @Override
    public <T> T load(Class<T> fileClass, File file, Charset charset) throws IOException {
        Preconditions.namedArgumentNonNull(fileClass, "file class");
        Preconditions.namedArgumentNonNull(charset, "charset");
        Preconditions.namedArgumentNonNull(file, "file");
        
        try (InputStream inputStream = new FileInputStream(file)) {
            final T t = load(fileClass, inputStream, charset);
    
            if (t instanceof Storable) {
                final Storable storable = (Storable) t;
                
                storable.setFile(file);
                storable.setCharset(charset);
            }
            
            return t;
        }
    }
    
    @Override
    public <T> T load(Class<T> fileClass, File file) throws IOException {
        return load(fileClass, file, configuration.getCharset());
    }
    
    @Override
    public <T> T load(Class<T> fileClass, InputStream inputStream, Charset charset) throws IOException {
        Preconditions.namedArgumentNonNull(fileClass, "file class");
        Preconditions.namedArgumentNonNull(charset, "charset");
        Preconditions.namedArgumentNonNull(inputStream, "input stream");
    
        final int available = inputStream.available();
        final byte[] bytes = new byte[available];
        inputStream.read(bytes);
    
        final String string = new String(bytes, charset);
        return load(fileClass, string);
    }
    
    @Override
    public <T> T load(Class<T> fileClass, InputStream inputStream) throws IOException {
        return load(fileClass, inputStream, configuration.getCharset());
    }
    
    @Override
    public <T> T loadOrElseGet(Class<T> fileClass, File file, Charset charset, Supplier<T> supplier) {
        Preconditions.namedArgumentNonNull(file, "file");
        Preconditions.namedArgumentNonNull(charset, "charset");
        Preconditions.namedArgumentNonNull(fileClass, "file class");
        Preconditions.namedArgumentNonNull(supplier, "supplier");
    
        if (!file.isFile()) {
            final T t = supplier.get();
    
            if (t instanceof Storable) {
                final Storable storable = (Storable) t;
        
                storable.setFile(file);
                storable.setCharset(charset);
                storable.setFileService(this);
    
                try {
                    file.createNewFile();
                    
                    storable.save();
                } catch (IOException e) {
                    logger.error("保存默认文件「" + file.getAbsolutePath() + "」时出现异常", e);
                }
            }
            
            return t;
        }
        
        try {
            return load(fileClass, file, charset);
        } catch (Throwable throwable) {
            logger.error("载入文件「" + file.getAbsolutePath() + "」时出现异常", throwable);
            
            final T t = supplier.get();
    
            if (t instanceof Storable) {
                final Storable storable = (Storable) t;
                
                storable.setFile(file);
                storable.setCharset(charset);
                storable.setFileService(this);
            }
            
            return t;
        }
    }
    
    @Override
    public <T> T loadOrElseGet(Class<T> fileClass, File file, Supplier<T> supplier) {
        return loadOrElseGet(fileClass, file, configuration.getCharset(), supplier);
    }
    
    @Override
    public <T> T loadOrElse(Class<T> fileClass, File file, Charset charset, T defaultValue) {
        return loadOrElseGet(fileClass, file, charset, () -> defaultValue);
    }
    
    @Override
    public <T> T loadOrElse(Class<T> fileClass, File file, T defaultValue) {
        return loadOrElseGet(fileClass, file, configuration.getCharset(), () -> defaultValue);
    }
    
    @Override
    public <T> T loadOrElseGet(Class<T> fileClass, InputStream inputStream, Charset charset, Supplier<T> supplier) {
        Preconditions.namedArgumentNonNull(fileClass, "file class");
        Preconditions.namedArgumentNonNull(inputStream, "inputStream");
        Preconditions.namedArgumentNonNull(charset, "charset");
        Preconditions.namedArgumentNonNull(supplier, "supplier");
    
        try {
            final int available = inputStream.available();
            final byte[] bytes = new byte[available];
            inputStream.read(bytes);
    
            final String string = new String(bytes, charset);
            return loadOrElseGet(fileClass, string, supplier);
        } catch (Throwable throwable) {
            logger.error("读取二进制流时出现异常", throwable);
    
            final T t = supplier.get();
            
            if (t instanceof Storable) {
                final Storable storable = (Storable) t;
        
                storable.setFileService(this);
            }
            
            return t;
        }
    }
    
    @Override
    public <T> T loadOrElseGet(Class<T> fileClass, InputStream inputStream, Supplier<T> supplier) {
        return loadOrElseGet(fileClass, inputStream, configuration.getCharset(), supplier);
    }
    
    @Override
    public <T> T loadOrElseGet(Class<T> fileClass, String string, Supplier<T> supplier) {
        Preconditions.namedArgumentNonNull(fileClass, "file class");
        Preconditions.namedArgumentNonNull(string, "string");
        Preconditions.namedArgumentNonNull(supplier, "supplier");
        
        try {
            return load(fileClass, string);
        } catch (Throwable throwable) {
            logger.error("读取字符串出现异常", throwable);
    
            final T t = supplier.get();
            
            if (t instanceof Storable) {
                final Storable storable = (Storable) t;
                
                storable.setFileService(this);
            }
            
            return t;
        }
    }
    
    @Override
    public <T> T loadOrElse(Class<T> fileClass, InputStream inputStream, Charset charset, T defaultValue) {
        return loadOrElseGet(fileClass, inputStream, charset, () -> defaultValue);
    }
    
    @Override
    public <T> T loadOrElse(Class<T> fileClass, InputStream inputStream, T defaultValue) {
        return loadOrElseGet(fileClass, inputStream, configuration.getCharset(), () -> defaultValue);
    }
    
    @Override
    public <T> T loadOrElse(Class<T> fileClass, String string, T defaultValue) {
        return loadOrElseGet(fileClass, string, () -> defaultValue);
    }
    
    @Override
    public void dump(Object object, OutputStream outputStream, Charset charset) throws IOException {
        Preconditions.namedArgumentNonNull(object, "object");
        Preconditions.namedArgumentNonNull(charset, "charset");
        Preconditions.namedArgumentNonNull(outputStream, "output stream");
    
        final String string = dump(object);
    
        final byte[] bytes = string.getBytes(charset);
        outputStream.write(bytes);
    }
    
    @Override
    public void dump(Object object, OutputStream outputStream) throws IOException {
        dump(object, outputStream, configuration.getCharset());
    }
    
    @Override
    public void dump(Object object, File file) throws IOException {
        dump(object, file, configuration.getCharset());
    }
    
    @Override
    public void dump(Object object, File file, Charset charset) throws IOException {
        Preconditions.namedArgumentNonNull(object, "object");
        Preconditions.namedArgumentNonNull(file, "file");
    
        if (!file.isFile()) {
            file.createNewFile();
        }
    
        try (OutputStream outputStream = new FileOutputStream(file)) {
            dump(object, outputStream, charset);
        }
    }
    
    @Override
    public FileServiceConfiguration getConfiguration() {
        return configuration;
    }
}
