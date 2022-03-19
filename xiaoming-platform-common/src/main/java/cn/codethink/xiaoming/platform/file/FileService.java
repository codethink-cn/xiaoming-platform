package cn.codethink.xiaoming.platform.file;

import cn.codethink.common.util.Preconditions;
import cn.codethink.xiaoming.logger.Logger;
import cn.codethink.xiaoming.platform.PlatformObject;
import cn.codethink.xiaoming.platform.configuration.FileServiceConfiguration;

import java.io.*;
import java.nio.charset.Charset;
import java.util.function.Supplier;

/**
 * 序列化器
 *
 * @author Chuanwise
 */
public interface FileService
        extends PlatformObject {
    
    /**
     * 获取日志记录器
     *
     * @return 日志记录器
     */
    Logger getLogger();
    
    /**
     * 载入文件
     *
     * @param file    文件
     * @param charset 编码
     * @return 载入的对象
     * @throws IOException 载入出现异常
     */
    Object load(File file, Charset charset) throws IOException;
    
    /**
     * 载入文件
     *
     * @param file 文件
     * @return 载入的对象
     * @throws IOException 载入出现异常
     */
    Object load(File file) throws IOException;
    
    /**
     * 载入文件
     * <p>
     * 任何时候都不会关闭输入流。
     *
     * @param inputStream 输入流
     * @param charset     编码
     * @return 载入的对象
     * @throws IOException 载入出现异常
     */
    Object load(InputStream inputStream, Charset charset) throws IOException;
    
    /**
     * 载入文件
     * <p>
     * 任何时候都不会关闭输入流。
     *
     * @param inputStream 输入流
     * @return 载入的对象
     * @throws IOException 载入出现异常
     */
    Object load(InputStream inputStream) throws IOException;
    
    /**
     * 反序列化字符串
     *
     * @param string 字符串
     * @return 反序列化得到的对象
     */
    Object load(String string);
    
    /**
     * 载入文件
     *
     * @param fileClass 文件类
     * @param file      文件
     * @param charset   编码
     * @param <T>       文件类
     * @return 载入的文件
     * @throws IOException 载入出现异常
     */
    <T> T load(Class<T> fileClass, File file, Charset charset) throws IOException;
    
    /**
     * 载入文件
     *
     * @param fileClass 文件类
     * @param file      文件
     * @param <T>       文件类
     * @return 载入的文件
     * @throws IOException 载入出现异常
     */
    <T> T load(Class<T> fileClass, File file) throws IOException;
    
    /**
     * 载入文件
     *
     * @param fileClass   文件类
     * @param inputStream 输入流
     * @param charset     编码
     * @param <T>         文件类
     * @return 载入的文件
     * @throws IOException 载入出现异常
     */
    <T> T load(Class<T> fileClass, InputStream inputStream, Charset charset) throws IOException;
    
    /**
     * 载入文件
     *
     * @param fileClass   文件类
     * @param inputStream 输入流
     * @param <T>         文件类
     * @return 载入的文件
     * @throws IOException 载入出现异常
     */
    <T> T load(Class<T> fileClass, InputStream inputStream) throws IOException;
    
    /**
     * 载入文件
     *
     * @param fileClass 文件类
     * @param string    字符串
     * @param <T>       文件类
     * @return 载入的文件
     */
    <T> T load(Class<T> fileClass, String string);
    
    /**
     * 从文件中载入数据，载入失败时用默认构造器构造
     *
     * @param fileClass 文件类
     * @param file      文件
     * @param charset   编码
     * @param supplier  默认构造器
     * @param <T>       文件类
     * @return 载入的文件
     */
    <T> T loadOrElseGet(Class<T> fileClass, File file, Charset charset, Supplier<T> supplier);
    
    /**
     * 从文件中载入数据，载入失败时用默认构造器构造
     *
     * @param fileClass 文件类
     * @param file      文件
     * @param supplier  默认构造器
     * @param <T>       文件类
     * @return 载入的文件
     */
    <T> T loadOrElseGet(Class<T> fileClass, File file, Supplier<T> supplier);
    
    /**
     * 从文件中载入数据，载入失败时使用默认值
     *
     * @param fileClass    文件类
     * @param file         文件
     * @param charset      编码
     * @param defaultValue 默认值
     * @param <T>          文件类
     * @return 载入的文件
     */
    <T> T loadOrElse(Class<T> fileClass, File file, Charset charset, T defaultValue);
    
    /**
     * 从文件中载入数据，载入失败时使用默认值
     *
     * @param fileClass    文件类
     * @param file         文件
     * @param defaultValue 默认值
     * @param <T>          文件类
     * @return 载入的文件
     */
    <T> T loadOrElse(Class<T> fileClass, File file, T defaultValue);
    
    /**
     * 从输入流中载入数据，载入失败时用默认构造器构造
     * <p>
     * 任何情况下都不会关闭输入流。
     *
     * @param fileClass   文件类
     * @param inputStream 输入流
     * @param charset     编码
     * @param supplier    默认构造器
     * @param <T>         文件类
     * @return 载入的文件
     */
    <T> T loadOrElseGet(Class<T> fileClass, InputStream inputStream, Charset charset, Supplier<T> supplier);
    
    /**
     * 从输入流中载入数据，载入失败时用默认构造器构造
     * <p>
     * 任何情况下都不会关闭输入流。
     *
     * @param fileClass   文件类
     * @param inputStream 输入流
     * @param supplier    默认构造器
     * @param <T>         文件类
     * @return 载入的文件
     */
    <T> T loadOrElseGet(Class<T> fileClass, InputStream inputStream, Supplier<T> supplier);
    
    /**
     * 从字符串中载入数据，载入失败时用默认构造器构造
     * <p>
     * 任何情况下都不会关闭输入流。
     *
     * @param fileClass 文件类
     * @param string    字符串
     * @param supplier  默认构造器
     * @param <T>       文件类
     * @return 载入的文件
     */
    <T> T loadOrElseGet(Class<T> fileClass, String string, Supplier<T> supplier);
    
    /**
     * 从输入流中载入数据，载入失败时返回默认值
     * <p>
     * 任何情况下都不会关闭输入流。
     *
     * @param fileClass    文件类
     * @param inputStream  输入流
     * @param charset      编码
     * @param defaultValue 默认值
     * @param <T>          文件类
     * @return 载入的文件
     */
    <T> T loadOrElse(Class<T> fileClass, InputStream inputStream, Charset charset, T defaultValue);
    
    /**
     * 从输入流中载入数据，载入失败时返回默认值
     * <p>
     * 任何情况下都不会关闭输入流。
     *
     * @param fileClass    文件类
     * @param inputStream  输入流
     * @param defaultValue 默认值
     * @param <T>          文件类
     * @return 载入的文件
     */
    <T> T loadOrElse(Class<T> fileClass, InputStream inputStream, T defaultValue);
    
    /**
     * 从输入流中载入数据，载入失败时返回默认值
     * <p>
     * 任何情况下都不会关闭输入流。
     *
     * @param fileClass    文件类
     * @param string       字符串
     * @param defaultValue 默认值
     * @param <T>          文件类
     * @return 载入的文件
     */
    <T> T loadOrElse(Class<T> fileClass, String string, T defaultValue);
    
    /**
     * 序列化某个对象
     *
     * @param object 对象
     * @return 系列化后的字符串
     */
    String dump(Object object);
    
    /**
     * 序列化某个对象到输出流
     *
     * @param object       对象
     * @param outputStream 输出流
     * @param charset      编码
     * @throws IOException 输出时出现错误
     */
    void dump(Object object, OutputStream outputStream, Charset charset) throws IOException;
    
    /**
     * 序列化某个对象到输出流
     *
     * @param object       对象
     * @param outputStream 输出流
     * @throws IOException 输出时出现错误
     */
    void dump(Object object, OutputStream outputStream) throws IOException;
    
    /**
     * 序列化某个对象到文件
     *
     * @param object 对象
     * @param file   文件
     * @throws IOException 输出时出现错误
     */
    void dump(Object object, File file) throws IOException;
    
    /**
     * 序列化某个对象到文件
     *
     * @param object  对象
     * @param file    文件
     * @param charset 编码
     * @throws IOException 输出时出现错误
     */
    void dump(Object object, File file, Charset charset) throws IOException;
    
    /**
     * 获取序列化器设置
     *
     * @return 序列化器设置
     */
    FileServiceConfiguration getConfiguration();
}