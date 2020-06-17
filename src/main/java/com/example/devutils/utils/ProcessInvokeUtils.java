package com.example.devutils.utils;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Created by AMe on 2020-06-16 16:28.
 */
public class ProcessInvokeUtils {

    public static Runtime getRuntime() {
        return Runtime.getRuntime();
    }

    public static void invoke(String cmd, Consumer<Process> consumer) throws IOException, InterruptedException {
        invoke(getRuntime(), cmd, consumer);
    }

    public static void invoke(Runtime runtime, String cmd, Consumer<Process> consumer) throws IOException, InterruptedException {
        Process process = runtime.exec(cmd);
        consumer.accept(process);
        process.waitFor();
        process.destroy();
    }

}
