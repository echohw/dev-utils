package com.example.devutils.utils.office;

import com.example.devutils.utils.http.OkHttpUtils;
import com.example.devutils.utils.io.PathUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by AMe on 2020-07-07 04:34.
 */
public class ImageUtils {

    public static byte[] readLocalImage(String path) throws IOException {
        return Files.readAllBytes(Paths.get(path));
    }

    public static byte[] readNetImage(String url) throws IOException {
        return OkHttpUtils.getRespBytes(new OkHttpUtils().sendGetRequest(url, null, null));
    }

    public static byte[] readImage(String path) throws IOException {
        if (path.startsWith("http")) {
            return readNetImage(path);
        } else if (PathUtils.exists(PathUtils.getPath(path))) {
            return readLocalImage(path);
        }
        return new byte[0];
    }

}
