package com.example.devutils.utils;

import com.example.devutils.dep.Charsets;
import com.example.devutils.utils.io.StreamUtils;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
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
        invoke(cmd, process -> {
            try (
                InputStream inputStream = process.getInputStream();
                InputStream errorStream = process.getErrorStream();
            ) {
                String out = StreamUtils.readAsString(inputStream, Charsets.UTF_8);
                String err = StreamUtils.readAsString(errorStream, Charsets.UTF_8);
                logger.info("OutputStream: {}", out);
                logger.info("ErrorStream: {}", err);
            } catch (IOException ex) {
                logger.error(ex.getMessage());
            }
        });
    }

    public static void invoke(String cmd, Consumer<Process> resConsumer) throws IOException, InterruptedException {
        invoke(getRuntime(), cmd, resConsumer);
    }

    public static void invoke(Runtime runtime, String cmd, Consumer<Process> resConsumer) throws IOException, InterruptedException {
        Process process = runtime.exec(cmd);
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

}
