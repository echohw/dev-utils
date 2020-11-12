package com.example.devutils.utils;

import com.example.devutils.constant.CharsetConsts;
import com.example.devutils.utils.io.StreamUtils;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.StringTokenizer;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by AMe on 2020-06-16 16:28.
 */
public class ProcessInvokeUtils {

    private static final Logger logger = LoggerFactory.getLogger(ProcessInvokeUtils.class);
    private static final String CMD_SELECT_WINDOWS = "explorer /select,";

    public static Runtime getRuntime() {
        return Runtime.getRuntime();
    }

    public static Desktop getDesktop() {
        return Desktop.getDesktop();
    }

    public static void invoke(String cmd) throws IOException, InterruptedException {
        invoke(cmd, getDefaultProcessConsumer(CharsetConsts.UTF_8));
    }

    public static void invoke(String cmd, Consumer<Process> resConsumer) throws IOException, InterruptedException {
        invoke(cmd, null, null, resConsumer);
    }

    public static void invoke(String cmd, String[] envp, File dir, Consumer<Process> resConsumer) throws IOException, InterruptedException {
        invoke(getRuntime(), cmd, envp, dir, resConsumer);
    }

    public static void invoke(Runtime runtime, String cmd, String[] envp, File dir, Consumer<Process> resConsumer) throws IOException, InterruptedException {
        if (cmd.length() == 0) throw new IllegalArgumentException("Empty command");
        StringTokenizer st = new StringTokenizer(cmd);
        String[] cmds = new String[st.countTokens()];
        for (int i = 0; st.hasMoreTokens(); i++) {
            cmds[i] = st.nextToken();
        }
        invoke(runtime, cmds, envp, dir, resConsumer);
    }

    public static void invoke(Runtime runtime, String[] cmds, String[] envp, File dir, Consumer<Process> resConsumer) throws IOException, InterruptedException {
        Process process = runtime.exec(cmds, envp, dir);
        if (resConsumer != null) resConsumer.accept(process);
        process.waitFor();
        process.destroy();
    }

    public static void open(File path) throws IOException {
        getDesktop().open(path);
    }

    public static void browse(URI uri) throws IOException {
        getDesktop().browse(uri);
    }

    public static void select(String path) throws IOException, InterruptedException {
        invoke(CMD_SELECT_WINDOWS + path, null);
    }

    public static Consumer<Process> getDefaultProcessConsumer(Charset charset) {
        return process -> {
            try (
                InputStream inputStream = process.getInputStream();
                InputStream errorStream = process.getErrorStream();
            ) {
                String out = StreamUtils.readString(inputStream, charset);
                String err = StreamUtils.readString(errorStream, charset);
                logger.info("OutputStream: {}", out);
                logger.info("ErrorStream: {}", err);
            } catch (Exception ex) {
                logger.error("", ex);
            }
        };
    }
}
