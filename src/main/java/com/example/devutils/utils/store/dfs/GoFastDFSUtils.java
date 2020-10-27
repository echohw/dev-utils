package com.example.devutils.utils.store.dfs;

import com.example.devutils.constant.MediaTypeConsts;
import com.example.devutils.utils.http.OkHttpUtils;
import com.example.devutils.utils.io.PathUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.MultipartBody.Part;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by AMe on 2020-06-23 18:43.
 */
public class GoFastDFSUtils {

    private static final String serverUrl = "http://xxx:8080/group1/upload";

    private static final OkHttpUtils okHttpUtils = new OkHttpUtils();

    public static String uploadFile(String filePath) throws IOException {
        String fileName = Optional.ofNullable(Paths.get(filePath)).filter(PathUtils::isFile).map(Path::getFileName).map(Path::toString).orElseThrow(() -> new IllegalArgumentException("错误的文件路径"));
        List<Part> parts = Arrays.asList(
            Part.createFormData("file", fileName, RequestBody.create(MediaType.parse(MediaTypeConsts.MULTIPART_FORM_DATA), new File(filePath))),
            Part.createFormData("output", "json")
        );
        RequestBody requestBody = okHttpUtils.buildMultipartBody(MultipartBody.FORM, parts);
        Response response = okHttpUtils.sendPostRequest(serverUrl, null, null, requestBody);
        return okHttpUtils.getRespText(response);
    }

}
