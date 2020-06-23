package com.example.devutils.utils.store.dfs;

import java.io.IOException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FastDFS操作工具类，基于org.csource.fastdfs:fastdfs:1.2
 */
public class FastDFSUtils {

    private static final Logger logger = LoggerFactory.getLogger(FastDFSUtils.class);

    private static final String CONFIG_FILE = "fastdfs_client.properties";

    static {
        logger.info("加载FastDFS的配置文件: {}", CONFIG_FILE);
        String filePath = FastDFSUtils.class.getClassLoader().getResource(CONFIG_FILE).getPath();
        try {
            ClientGlobal.init(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static StorageClient1 getStorageClient() throws IOException {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
        return new StorageClient1(trackerServer, storageServer);
    }

    public static String uploadFile(String filePath, String fileExtName) throws Exception {
        return uploadFile(filePath, fileExtName, null);
    }

    public static String uploadFile(String filePath, String fileExtName, NameValuePair[] nameValuePairs) throws Exception {
        StorageClient1 storageClient = getStorageClient();
        return storageClient.upload_file1(filePath, fileExtName, nameValuePairs);
    }

    public static String uploadFile(byte[] content, String fileExtName) throws Exception {
        return uploadFile(content, fileExtName, null);
    }

    public static String uploadFile(byte[] content, String fileExtName, NameValuePair[] nameValuePair) throws Exception {
        StorageClient1 storageClient = getStorageClient();
        return storageClient.upload_file1(content, fileExtName, nameValuePair);
    }

    public static byte[] downloadFile(String fileId) throws Exception {
        StorageClient1 storageClient = getStorageClient();
        return storageClient.download_file1(fileId);
    }

    public static boolean downloadFile(String fileId, String destPath) throws Exception {
        StorageClient1 storageClient = getStorageClient();
        return storageClient.download_file1(fileId, destPath) == 0;
    }

    public static boolean deleteFile(String fileId) throws Exception {
        StorageClient1 storageClient = getStorageClient();
        return storageClient.delete_file1(fileId) == 0;
    }

}

