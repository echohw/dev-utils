package com.example.devutils.utils.io;

import com.example.devutils.dep.Charsets;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
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
 * Created by AMe on 2020-06-10 01:14.
 */
public class FileAttributeUtils {

    public static List<AclEntry> getAccessControlList(Path filePath) throws IOException {
        AclFileAttributeView aclView = getAclView(filePath);
        return aclView.getAcl();
    }

    public static void setAccessControlList(Path filePath, List<AclEntry> aclEntryList) throws IOException {
        AclFileAttributeView aclView = getAclView(filePath);
        aclView.setAcl(aclEntryList);
    }

    public long getSize(Path filePath) throws IOException {
        return Files.size(filePath);
    }

    public static FileTime getCreateTime(Path filePath) throws IOException {
        BasicFileAttributeView basicView = getBasicView(filePath);
        return basicView.readAttributes().creationTime();
    }

    public static FileTime getLastModifiedTime(Path filePath) throws IOException {
        BasicFileAttributeView basicView = getBasicView(filePath);
        return basicView.readAttributes().lastModifiedTime();
    }

    public static FileTime getLastAccessTime(Path filePath) throws IOException {
        BasicFileAttributeView basicView = getBasicView(filePath);
        return basicView.readAttributes().lastAccessTime();
    }

    public static boolean isReadOnly(Path filePath) throws IOException {
        DosFileAttributeView dosView = getDosView(filePath);
        return dosView.readAttributes().isReadOnly();
    }

    public static boolean isHidden(Path filePath) throws IOException {
        DosFileAttributeView dosView = getDosView(filePath);
        return dosView.readAttributes().isHidden();
    }

    public static boolean isArchive(Path filePath) throws IOException {
        DosFileAttributeView dosView = getDosView(filePath);
        return dosView.readAttributes().isArchive();
    }

    public static boolean isSystem(Path filePath) throws IOException {
        DosFileAttributeView dosView = getDosView(filePath);
        return dosView.readAttributes().isSystem();
    }

    public static void setReadOnly(Path filePath, boolean readOnly) throws IOException {
        DosFileAttributeView dosView = getDosView(filePath);
        dosView.setReadOnly(readOnly);
    }

    public static void setHidden(Path filePath, boolean hidden) throws IOException {
        DosFileAttributeView dosView = getDosView(filePath);
        dosView.setHidden(hidden);
    }

    public static void setArchive(Path filePath, boolean archive) throws IOException {
        DosFileAttributeView dosView = getDosView(filePath);
        dosView.setArchive(archive);
    }

    public static void setSystem(Path filePath, boolean system) throws IOException {
        DosFileAttributeView dosView = getDosView(filePath);
        dosView.setSystem(system);
    }

    public static UserPrincipal getOwner(Path filePath) throws IOException {
        FileOwnerAttributeView ownerView = getOwnerView(filePath);
        return ownerView.getOwner();
    }

    public static void setOwner(Path filePath, UserPrincipal userPrincipal) throws IOException {
        FileOwnerAttributeView ownerView = getOwnerView(filePath);
        ownerView.setOwner(userPrincipal);
    }

    public static String getAttrValue(Path filePath, String attrName) throws IOException {
        UserDefinedFileAttributeView userDefinedView = getUserDefinedView(filePath);
        ByteBuffer byteBuffer = ByteBuffer.allocate(userDefinedView.size(attrName));
        userDefinedView.read(attrName, byteBuffer);
        byteBuffer.flip();
        return Charsets.UTF8_C.decode(byteBuffer).toString();
    }

    public static int setAttr(Path filePath, String attrName, String attrValue) throws IOException {
        UserDefinedFileAttributeView userDefinedView = getUserDefinedView(filePath);
        return userDefinedView.write(attrName, Charsets.UTF8_C.encode(attrValue));
    }

    public static void deleteAttr(Path filePath, String attrName) throws IOException {
        UserDefinedFileAttributeView userDefinedView = getUserDefinedView(filePath);
        userDefinedView.delete(attrName);
    }

    public static AclFileAttributeView getAclView(Path filePath) {
        return Files.getFileAttributeView(filePath, AclFileAttributeView.class);
    }

    public static BasicFileAttributeView getBasicView(Path filePath) {
        return Files.getFileAttributeView(filePath, BasicFileAttributeView.class);
    }

    public static DosFileAttributeView getDosView(Path filePath) {
        return Files.getFileAttributeView(filePath, DosFileAttributeView.class);
    }

    public static FileOwnerAttributeView getOwnerView(Path filePath) {
        return Files.getFileAttributeView(filePath, FileOwnerAttributeView.class);
    }

    public static PosixFileAttributeView getPosixView(Path filePath) {
        return Files.getFileAttributeView(filePath, PosixFileAttributeView.class);
    }

    public static UserDefinedFileAttributeView getUserDefinedView(Path filePath) {
        return Files.getFileAttributeView(filePath, UserDefinedFileAttributeView.class);
    }

}
