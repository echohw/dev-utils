package com.example.devutils.utils.io;

import com.example.devutils.utils.http.OkHttpUtils;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import okhttp3.ResponseBody;

/**
 * Created by AMe on 2020-07-23 13:16.
 */
public class FileReader {

    public static InputStream readLocalFile(String path) throws FileNotFoundException {
        return new BufferedInputStream(new FileInputStream(new File(path)));
    }

    public static byte[] readLocalFileAsBytes(String path) throws IOException {
        return Files.readAllBytes(Paths.get(path));
    }

    public static InputStream readRemoteFile(String url) throws IOException {
        ResponseBody respBody = new OkHttpUtils().sendGetRequest(url, Collections.emptyMap(), Collections.emptyMap()).body();
        if (respBody != null) {
            return respBody.byteStream();
        } else {
            return new ByteArrayInputStream(new byte[0]);
        }
    }

    public static byte[] readRemoteFileAsBytes(String url) throws IOException {
        return OkHttpUtils.getRespBytes(new OkHttpUtils().sendGetRequest(url, Collections.emptyMap(), Collections.emptyMap()));
    }

    public static InputStream readFile(String path) throws IOException {
        if (path.startsWith("http")) {
            return readRemoteFile(path);
        } else if (PathUtils.exists(PathUtils.getPath(path))) {
            return readLocalFile(path);
        }
        return new ByteArrayInputStream(new byte[0]);
    }

    public static byte[] readFileAsBytes(String path) throws IOException {
        if (path.startsWith("http")) {
            return readRemoteFileAsBytes(path);
        } else if (PathUtils.exists(PathUtils.getPath(path))) {
            return readLocalFileAsBytes(path);
        }
        return new byte[0];
    }

}
