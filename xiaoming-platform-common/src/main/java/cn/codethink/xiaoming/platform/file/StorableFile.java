package cn.codethink.xiaoming.platform.file;

import cn.codethink.common.util.Preconditions;
import lombok.Data;

import java.beans.Transient;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @see Storable
 * @author Chuanwise
 */
@Data
public class StorableFile
    implements Storable {
    
    protected transient Charset charset;
    
    protected transient File file;
    
    protected transient FileService fileService;
    
    @Override
    @Transient
    public FileService getFileService() {
        return fileService;
    }
    
    @Override
    @Transient
    public Charset getCharset() {
        return charset;
    }
    
    @Override
    @Transient
    public File getFile() {
        return file;
    }
    
    @Override
    public void save() throws IOException {
        save(fileService, file, charset);
    }
    
    @Override
    public void save(Charset charset) throws IOException {
        save(fileService, file, charset);
    }
    
    @Override
    public void save(File file) throws IOException {
        save(fileService, file, charset);
    }
    
    @Override
    public void save(File file, Charset charset) throws IOException {
        save(fileService, file, charset);
    }
    
    @Override
    public void save(FileService fileService) throws IOException {
        save(fileService, file, charset);
    }
    
    @Override
    public void save(FileService fileService, Charset charset) throws IOException {
        save(fileService, file, charset);
    }
    
    @Override
    public void save(FileService fileService, File file) throws IOException {
        save(fileService, file, charset);
    }
    
    @Override
    public void save(FileService fileService, File file, Charset charset) throws IOException {
        Preconditions.namedArgumentNonNull(fileService, "file service");
        Preconditions.namedArgumentNonNull(file, "file");
        Preconditions.namedArgumentNonNull(charset, "charset");
        
        fileService.dump(this, file, charset);
    }
}
