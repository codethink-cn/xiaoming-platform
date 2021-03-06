package cn.codethink.xiaoming.platform.console;

import cn.chuanwise.commandlib.CommandLib;
import cn.chuanwise.commandlib.adapter.JLineCommandLibCompleter;
import cn.chuanwise.commandlib.context.DispatchContext;
import cn.codethink.common.util.Preconditions;
import cn.codethink.xiaoming.logger.Logger;
import cn.codethink.xiaoming.platform.AbstractPlatformObject;
import cn.codethink.xiaoming.platform.Platform;
import cn.codethink.xiaoming.platform.command.CommandManager;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @see cn.codethink.xiaoming.platform.console.ConsoleService
 * @author Chuanwise
 */
@Data
@SuppressWarnings("all")
public class JlineConsoleService
    extends AbstractPlatformObject
    implements ConsoleService {
    
    protected Console console;
    
    @Override
    public Console getConsole() {
        if (Objects.isNull(console)) {
            console = new Console(platform);
        }
        return console;
    }
    
    @SuppressWarnings("all")
    protected final ExecutorService executorThread = Executors.newSingleThreadExecutor();
    
    private Terminal terminal;
    private LineReader reader;
    
    private final PrintStream elderSystemOut;
    private final PrintStream elderSystemErr;
    
    private final PrintStream newerSystemOut;
    private final PrintStream newerSystemErr;
    
    protected volatile Thread consoleInputThread;
    
    @Getter
    @Setter
    private String prompt;
    
    public JlineConsoleService(Platform platform, String prompt) throws IOException {
        super(platform);
        
        Preconditions.namedArgumentNonNull(prompt, "prompt");
        
        this.prompt = prompt;
    
        elderSystemOut = System.out;
        elderSystemErr = System.err;
    
        final CommandLib commandLib = platform.getCommandManager().getCommandLib();
    
        terminal = TerminalBuilder.builder()
            .jansi(true)
            .system(true)
            .name("?????????????????????")
            .build();
    
        reader = LineReaderBuilder.builder()
            .terminal(terminal)
            .appName("?????????????????????")
            .completer(new JLineCommandLibCompleter(commandLib))
            .build();
    
        newerSystemOut = new PrintStream(new JlineConsoleOutputStream(reader), true);
        newerSystemErr = new PrintStream(new JlineConsoleOutputStream(reader), true);
    }
    
    public JlineConsoleService(Platform platform) throws IOException {
        this(platform, "> ");
    }
    
    /** ????????????????????? */
    @Override
    @SuppressWarnings("all")
    public void open() throws IOException {
        final CommandManager commandManager = platform.getCommandManager();
        final CommandLib commandLib = commandManager.getCommandLib();
    
        // execute console command thread
        consoleInputThread = new Thread(() -> {
            try {
                hookSystemOut();
                
                while (platform.isStarted()) {
                    // read each line
                    final String line = reader.readLine(prompt);
                    
                    if (!line.isEmpty()) {
                        // ????????????????????????????????????????????????
                        executorThread.execute(() -> {
                            final Console console = getConsole();
                            
                            final Logger logger = console.getLogger();
                            logger.info("??????????????????" + line);
                            
                            // ?????????????????????
                            final boolean handled = commandLib.execute(new DispatchContext(commandLib, console, line));
                            
                            if (!handled) {
                                logger.warn("??????????????????????????????");
                            }
                        });
                    }
                }
            } catch (UserInterruptException | EndOfFileException ignored) {
            } finally {
                executorThread.shutdown();
                
                restoreSystemOut();
            }
        });
        
        consoleInputThread.setName("console input");
        consoleInputThread.start();
    }
    
    private void hookSystemOut() {
        System.setOut(newerSystemOut);
        System.setErr(newerSystemErr);
    }
    
    private void restoreSystemOut() {
        System.setOut(elderSystemOut);
        System.setErr(elderSystemErr);
    }
    
    /** ????????????????????? */
    @Override
    public void close() {
        // ????????????????????????????????????
        try {
            consoleInputThread.interrupt();
            consoleInputThread.join(TimeUnit.SECONDS.toMillis(3));
        } catch (InterruptedException ignored) {
        } finally {
            consoleInputThread = null;
        }
        
        executorThread.shutdown();
    }
}
