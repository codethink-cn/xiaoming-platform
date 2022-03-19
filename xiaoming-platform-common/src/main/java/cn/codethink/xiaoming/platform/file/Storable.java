package cn.codethink.xiaoming.platform.file;

import java.beans.Transient;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 存储文件
 *
 * @author Chuanwise
 */
public interface Storable {
    
    /**
     * 获取编码
     *
     * @return 编码
     */
    @Transient
    Charset getCharset();
    
    /**
     * 设置编码
     *
     * @param charset 编码
     */
    void setCharset(Charset charset);
    
    /**
     * 获取文件服务
     *
     * @return 文件服务
     */
    @Transient
    FileService getFileService();
    
    /**
     * 设置文件服务
     *
     * @param fileService 文件服务
     */
    void setFileService(FileService fileService);
    
    /**
     * 获取文件
     *
     * @return 文件
     */
    @Transient
    File getFile();
    
    /**
     * 设置文件
     *
     * @param file 文件
     */
    void setFile(File file);
    
    /**
     * 保存文件
     *
     * @throws IOException 保存时出现异常
     */
    void save() throws IOException;
    
    /**
     * 保存文件
     *
     * @param charset 编码
     * @throws IOException 保存时出现异常
     */
    void save(Charset charset) throws IOException;
    
    /**
     * 保存文件
     *
     * @param file 文件
     * @throws IOException 保存时出现异常
     */
    void save(File file) throws IOException;
    
    /**
     * 保存文件
     *
     * @param file    文件
     * @param charset 编码
     * @throws IOException 保存时出现异常
     */
    void save(File file, Charset charset) throws IOException;
    
    /**
     * 保存文件
     *
     * @param fileService 文件服务
     * @throws IOException 保存时出现异常
     */
    void save(FileService fileService) throws IOException;
    
    /**
     * 保存文件
     *
     * @param fileService 文件服务
     * @param charset     编码
     * @throws IOException 保存时出现异常
     */
    void save(FileService fileService, Charset charset) throws IOException;
    
    /**
     * 保存文件
     *
     * @param fileService 文件服务
     * @param file        文件
     * @throws IOException 保存时出现异常
     */
    void save(FileService fileService, File file) throws IOException;
    
    /**
     * 保存文件
     *
     * @param fileService 文件服务
     * @param file        文件
     * @param charset     编码
     * @throws IOException 保存时出现异常
     */
    void save(FileService fileService, File file, Charset charset) throws IOException;
}