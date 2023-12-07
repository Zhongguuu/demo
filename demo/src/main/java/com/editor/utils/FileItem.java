package com.editor.utils;

import java.util.TreeMap;

public class FileItem {
    private String name;

    //创建时间
    private long createTime;

    //是否同步
    private boolean isSync;

    //是否是文件夹
    private boolean isFolder;

    //路径
    private String path;

    private String author;

    private TreeMap<String,Comment> comments;

    public FileItem(String name, long createTime, String path) {
        this.name = name;
        this.createTime = createTime;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public boolean isSync() {
        return isSync;
    }

    public boolean isFolder() {
        return isFolder;
    }

    public String getPath() {
        return path;
    }

    public void setSync(boolean sync) {
        isSync = sync;
    }

    public void setFolder(boolean folder) {
        isFolder = folder;
    }

    @Override
    public String toString() {
        return name+" sync:"+isSync;
    }
}
