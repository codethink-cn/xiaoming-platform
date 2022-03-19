package cn.codethink.xiaoming.platform;

import cn.codethink.common.util.Numbers;
import cn.codethink.common.util.Strings;
import cn.codethink.xiaoming.Bot;
import cn.codethink.xiaoming.configuration.BotConfiguration;
import cn.codethink.xiaoming.platform.bot.BotAccount;
import cn.codethink.xiaoming.platform.bot.BotAccountManager;
import cn.codethink.xiaoming.platform.bot.MiraiBotAccount;
import cn.codethink.xiaoming.platform.configuration.PlatformConfiguration;
import cn.codethink.xiaoming.platform.console.JlineConsoleService;
import cn.codethink.xiaoming.platform.logger.PlatformLoggerConfiguration;
import cn.codethink.xiaoming.platform.util.Logo;
import cn.codethink.xiaoming.platform.util.SystemProperties;
import cn.codethink.xiaoming.platform.util.Tips;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 平台主类
 *
 * @author Chuanwise
 */
public class MiraiPlatformLauncher {
    
    /**
     * 工作目录
     */
    protected static final File WORKING_DIRECTORY = SystemProperties.PLATFORM_WORKING_DIRECTORY
        .getOrElseGet(() -> new File(System.getProperty("user.dir")));
    
    /**
     * 日志文件名属性名
     */
    protected static final String LOG_FILE_PROPERTY = "xiaoming.platform.log";
    

    
    static {
        // create logger directory
        final File logsDirectory = new File(WORKING_DIRECTORY, "logs");
        final DateFormat logFileDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final String date = logFileDateFormat.format(System.currentTimeMillis());
        
        // check if there is same name
        final String extension = ".log";
        final String logFileName;
        if (logsDirectory.isDirectory()) {
            int index = 1;
            for (; index < Integer.MAX_VALUE; index++) {
                final File file = new File(logsDirectory, date + '-' + index + extension);
                if (!file.isFile()) {
                    break;
                }
            }
            logFileName = date + "-" + index;
        } else {
            logFileName = date;
        }
        System.setProperty(LOG_FILE_PROPERTY, logFileName);
    }
    
    private static void printlnHead() {
        System.out.println(
            Ansi.ansi()
            .fgBrightCyan()
            .a(Logo.ASCII_LOGO)
        );
        
        // developers info
        final String groupInfo = Ansi.ansi()
            .reset()
            .a("group: ")
            .a(Platform.GROUP)
            .reset()
            .toString();
        final String groupInfoSpaces = Strings.repeat(" ", (Logo.ASCII_WIDTH - groupInfo.length()) / 2);
        System.out.println(groupInfoSpaces + groupInfo);
    
        final String versionInfo = Ansi.ansi()
            .fgBrightYellow()
            .a("platform-version: ")
            .a(Platform.VERSION)
            .a("-")
            .a(Platform.VERSION_BRANCH)
            .a(" : ")
            .a("bot-version: ")
            .a(Bot.VERSION)
            .a("-")
            .a(Bot.VERSION_BRANCH)
            .reset()
            .toString();
        
        final String versionInfoSpaces = Strings.repeat(" ", (Logo.ASCII_WIDTH - versionInfo.length()) / 2);
        System.out.println(versionInfoSpaces + versionInfo);
        
        // tips
        // select a tips
        final String tip = Tips.select();

        final String tipInfo = Ansi.ansi()
            .fgCyan()
            .a("TIP")
            .fgBrightBlack()
            .a(": ")
            .fgBrightCyan()
            .a(tip)
            .reset()
            .toString();
        final String tipInfoSpaces = Strings.repeat(" ", (Logo.ASCII_WIDTH - tipInfo.length()) / 2);
        System.out.println();
        System.out.println(tipInfoSpaces + tipInfo);
    
        System.out.println();
    }
    
    public static void main(String[] args) {
        // setup colorful console
        AnsiConsole.systemInstall();
    
        // installing log
        PlatformLoggerConfiguration.install();
    
        final Logger logger = LoggerFactory.getLogger("launcher");
        printlnHead();
        
        final PlatformConfiguration platformConfiguration = new PlatformConfiguration();
        platformConfiguration.setWorkingDirectory(WORKING_DIRECTORY);
        if (!WORKING_DIRECTORY.isDirectory() && !WORKING_DIRECTORY.mkdirs()) {
            logger.error("can not create working directory: " + WORKING_DIRECTORY.getAbsolutePath());
            logger.error("无法创建工作目录");
            return;
        }
        
        // prepare platform
        final MiraiPlatform platform = new MiraiPlatform(platformConfiguration);
        
        // load bots
        final File implementsDirectory = new File(WORKING_DIRECTORY, "implements");
        if (!implementsDirectory.isDirectory() && !implementsDirectory.mkdirs()) {
            logger.error("can not create implements directory: " + implementsDirectory.getAbsolutePath());
            logger.error("无法创建实现文件夹");
            return;
        }
    
        // if there is not a file called bots.yml
        // then it's the first user
        final File botsFile = new File(implementsDirectory, "bots.yml");
        final BotAccountManager botAccountManager;
        if (!botsFile.isFile()) {
            logger.warn("can not found bots.yml file");
            logger.warn("无法找到账号文件，你可能是第一次使用本软件，赶快开始第一次配置吧！");
    
            // DO NOT CLOSE!
            final Scanner scanner = new Scanner(System.in);
            final long qq;
            System.out.print("请输入机器人的 QQ：");
            while (true) {
                final String line = scanner.nextLine();
                final Optional<Long> optionalLong = Numbers.parseLong(line);
                if (optionalLong.isPresent()) {
                    qq = optionalLong.get();
                    break;
                } else {
                    System.out.print("「" + line + "」似乎不是一个合理的 QQ，重新再输入一下吧：");
                }
            }
    
            final Console console = System.console();
            final char[] passwords;
            if (Objects.nonNull(console)) {
                System.out.print("请输入机器人的密码（输入时看不到显示密码，这是一种保护机制，看起来就像是无法输入。没关系，正常输入即可）：");
                passwords = console.readPassword();
            } else {
                System.out.print("请输入机器人的密码（你使用的是虚拟控制台，将会回显密码）：");
                passwords = scanner.nextLine().toCharArray();
            }
    
            final byte[] md5;
            try {
                final MessageDigest digest = MessageDigest.getInstance("MD5");
                md5 = digest.digest(new String(passwords).getBytes(StandardCharsets.UTF_8));
            } catch (NoSuchAlgorithmException e) {
                logger.error("缺少 MD5 算法，无法加密密码");
                return;
            }
    
            botAccountManager = new BotAccountManager();
            botAccountManager.getBotAccounts().add(new MiraiBotAccount(qq, md5));
            botAccountManager.setFile(botsFile);
            
            // save file
            try {
                botsFile.createNewFile();
    
                try (OutputStream outputStream = new FileOutputStream(botsFile)) {
                    outputStream.write(new Yaml().dumpAsMap(botAccountManager).getBytes());
                }
            } catch (IOException exception) {
                logger.error("无法保存机器人账户文件");
                return;
            }
        } else {
            
            try (InputStream inputStream = new FileInputStream(botsFile)) {
                final Yaml yaml = new Yaml(new CustomClassLoaderConstructor(MiraiPlatformLauncher.class.getClassLoader()));
                botAccountManager = yaml.loadAs(inputStream, BotAccountManager.class);
            } catch (IOException exception) {
                logger.error("无法读取配置文件");
                return;
            }
        }
    
        final List<BotAccount> botAccounts = botAccountManager.getBotAccounts();
        if (botAccounts.isEmpty()) {
            logger.error("账户信息文件为空！已删除空文件，下次启动程序时将会进行初始化操作");
            botsFile.delete();
            return;
        }
    
        // load bot
        for (BotAccount botAccount : botAccounts) {
            final Bot bot = botAccount.toBot();
            final BotConfiguration botConfiguration = bot.getBotConfiguration();
            
            botConfiguration.setHideImplementBotLog(true);
            
            // flush bot configuration
            platformConfiguration.addBot(bot);
        }
        
        // start platform
        try {
            if (!platform.start()) {
                return;
            }
        } catch (Throwable throwable) {
            logger.error("启动机器人时出现异常", throwable);
            return;
        }
        
        try {
            // enable console
            final JlineConsoleService consoleService = new JlineConsoleService(platform, "> ");
            platform.setConsoleService(consoleService);
            consoleService.open();
        } catch (IOException e) {
            logger.error("无法启动控制台输入", e);
        }
    }
}
