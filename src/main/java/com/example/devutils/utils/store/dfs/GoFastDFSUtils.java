package com.example.devutils.utils.store.dfs;

import com.example.devutils.dep.MediaTypes;
import com.example.devutils.utils.http.OkHttpUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
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

    public static String uploadFile(String filePath) throws IOException {
        String fileName = Paths.get(filePath).getFileName().toString();
        List<Part> parts = Arrays.asList(
            Part.createFormData("file", fileName, RequestBody.create(MediaType.parse(MediaTypes.MULTIPART_FORM_DATA), new File(filePath))),
            Part.createFormData("output", "json")
        );
        RequestBody requestBody = OkHttpUtils.buildMultipartBody(MultipartBody.FORM, parts);
        Response response = OkHttpUtils.sendPostRequest(serverUrl, null, null, requestBody);
        return OkHttpUtils.getRespText(response);
    }

}
