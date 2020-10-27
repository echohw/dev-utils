package com.example.devutils.utils.io;

import com.example.devutils.constant.CharsetConsts;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.nio.file.attribute.UserPrincipal;
import java.util.List;

/**
 * 文件属性操作工具类
 * Created by AMe on 2020-06-10 01:14.
 */
public class FileAttributeUtils {

    public static List<AclEntry> getAccessControlList(Path filePath) throws IOException {
        AclFileAttributeView aclView = getAclFileAttributeView(filePath);
        return aclView.getAcl();
    }

    public static void setAccessControlList(Path filePath, List<AclEntry> aclEntryList) throws IOException {
        AclFileAttributeView aclView = getAclFileAttributeView(filePath);
        aclView.setAcl(aclEntryList);
    }

    public static long getSize(Path filePath) throws IOException {
        return Files.size(filePath);
    }

    public static FileTime getCreateTime(Path filePath) throws IOException {
        BasicFileAttributeView basicView = getBasicFileAttributeView(filePath);
        return basicView.readAttributes().creationTime();
    }

    public static FileTime getLastModifiedTime(Path filePath) throws IOException {
        BasicFileAttributeView basicView = getBasicFileAttributeView(filePath);
        return basicView.readAttributes().lastModifiedTime();
    }

    public static FileTime getLastAccessTime(Path filePath) throws IOException {
        BasicFileAttributeView basicView = getBasicFileAttributeView(filePath);
        return basicView.readAttributes().lastAccessTime();
    }

    public static boolean isReadOnly(Path filePath) throws IOException {
        DosFileAttributeView dosView = getDosFileAttributeView(filePath);
        return dosView.readAttributes().isReadOnly();
    }

    public static boolean isHidden(Path filePath) throws IOException {
        DosFileAttributeView dosView = getDosFileAttributeView(filePath);
        return dosView.readAttributes().isHidden();
    }

    public static boolean isArchive(Path filePath) throws IOException {
        DosFileAttributeView dosView = getDosFileAttributeView(filePath);
        return dosView.readAttributes().isArchive();
    }

    public static boolean isSystem(Path filePath) throws IOException {
        DosFileAttributeView dosView = getDosFileAttributeView(filePath);
        return dosView.readAttributes().isSystem();
    }

    public static void setReadOnly(Path filePath, boolean readOnly) throws IOException {
        DosFileAttributeView dosView = getDosFileAttributeView(filePath);
        dosView.setReadOnly(readOnly);
    }

    public static void setHidden(Path filePath, boolean hidden) throws IOException {
        DosFileAttributeView dosView = getDosFileAttributeView(filePath);
        dosView.setHidden(hidden);
    }

    public static void setArchive(Path filePath, boolean archive) throws IOException {
        DosFileAttributeView dosView = getDosFileAttributeView(filePath);
        dosView.setArchive(archive);
    }

    public static void setSystem(Path filePath, boolean system) throws IOException {
        DosFileAttributeView dosView = getDosFileAttributeView(filePath);
        dosView.setSystem(system);
    }

    public static UserPrincipal getOwner(Path filePath) throws IOException {
        FileOwnerAttributeView ownerView = getFileOwnerAttributeView(filePath);
        return ownerView.getOwner();
    }

    public static void setOwner(Path filePath, UserPrincipal userPrincipal) throws IOException {
        FileOwnerAttributeView ownerView = getFileOwnerAttributeView(filePath);
        ownerView.setOwner(userPrincipal);
    }

    public static String getAttribute(Path filePath, String attrName) throws IOException {
        UserDefinedFileAttributeView userDefinedView = getUserDefinedFileAttributeView(filePath);
        ByteBuffer byteBuffer = ByteBuffer.allocate(userDefinedView.size(attrName));
        userDefinedView.read(attrName, byteBuffer);
        byteBuffer.flip();
        return CharsetConsts.UTF_8.decode(byteBuffer).toString();
    }

    public static int setAttribute(Path filePath, String attrName, String attrValue) throws IOException {
        UserDefinedFileAttributeView userDefinedView = getUserDefinedFileAttributeView(filePath);
        return userDefinedView.write(attrName, CharsetConsts.UTF_8.encode(attrValue));
    }

    public static void deleteAttribute(Path filePath, String attrName) throws IOException {
        UserDefinedFileAttributeView userDefinedView = getUserDefinedFileAttributeView(filePath);
        userDefinedView.delete(attrName);
    }

    public static AclFileAttributeView getAclFileAttributeView(Path filePath) {
        return Files.getFileAttributeView(filePath, AclFileAttributeView.class);
    }

    public static BasicFileAttributeView getBasicFileAttributeView(Path filePath) {
        return Files.getFileAttributeView(filePath, BasicFileAttributeView.class);
    }

    public static DosFileAttributeView getDosFileAttributeView(Path filePath) {
        return Files.getFileAttributeView(filePath, DosFileAttributeView.class);
    }

    public static FileOwnerAttributeView getFileOwnerAttributeView(Path filePath) {
        return Files.getFileAttributeView(filePath, FileOwnerAttributeView.class);
    }

    public static PosixFileAttributeView getPosixFileAttributeView(Path filePath) {
        return Files.getFileAttributeView(filePath, PosixFileAttributeView.class);
    }

    public static UserDefinedFileAttributeView getUserDefinedFileAttributeView(Path filePath) {
        return Files.getFileAttributeView(filePath, UserDefinedFileAttributeView.class);
    }
}
