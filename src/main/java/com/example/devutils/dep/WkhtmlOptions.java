package com.example.devutils.dep;

import com.example.devutils.utils.text.StringUtils;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

/**
 * Created by AMe on 2020-06-16 20:19.
 */
public class WkhtmlOptions {

    private String programDir;
    private String pdfProgram = "wkhtmltopdf";
    private String imgProgram = "wkhtmltoimage";

    private Map<String, Object> optionsMap = new LinkedHashMap<>();

    private String htmlPath;
    private String destPath;

    public WkhtmlOptions programDir(String programDir) {
        this.programDir = programDir;
        return this;
    }

    public WkhtmlOptions pdfProgram(String pdfProgram) {
        this.pdfProgram = pdfProgram;
        return this;
    }

    public WkhtmlOptions imgProgram(String imgProgram) {
        this.imgProgram = imgProgram;
        return this;
    }

    public WkhtmlOptions htmlPath(String htmlPath) {
        this.htmlPath = htmlPath;
        return this;
    }

    public WkhtmlOptions destPath(String destPath) {
        this.destPath = destPath;
        return this;
    }

    public WkhtmlOptions opt(String name) {
        return opt(name, null);
    }

    public WkhtmlOptions opt(String name, Object value) {
        optionsMap.put(name, value);
        return this;
    }

    public String toPdfExecCmd() {
        return toExecCmd(pdfProgram);
    }

    public String toImgExecCmd() {
        return toExecCmd(imgProgram);
    }

    private String toExecCmd(String execProgram) {
        StringBuilder stringBuilder = new StringBuilder();
        if (StringUtils.isBlank(programDir)) {
            stringBuilder.append(execProgram);
        } else {
            stringBuilder.append(programDir)
                .append(!programDir.endsWith("/") && !execProgram.startsWith("/") ? (stringBuilder.append("/") != null ? execProgram : execProgram) : execProgram);
        }
        stringBuilder.append(" ").append(toOptions());
        stringBuilder.append(" ").append(htmlPath).append(" ").append(destPath);
        return stringBuilder.toString();
    }

    public String toOptions() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Entry<String, Object> entry : optionsMap.entrySet()) {
            Optional.ofNullable(entry.getKey()).ifPresent(key -> stringBuilder.append(" ").append(key));
            Optional.ofNullable(entry.getValue()).ifPresent(value -> stringBuilder.append(" ").append(value));
        }
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return toPdfExecCmd();
    }

}
