package com.learn.thread.pattern.chapter_22;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Document {

    /**
     * 如果文档发生改变， changed 会被设置为true
     */
    private boolean changed = false;

    /**
     * 一次需要保存的内容， 可以将其理解为内容缓存
     */
    private List<String> content = new ArrayList<>();

    private final FileWriter writer;

    /**
     * 自动保存文档的线程
     */
    private static AutoSaveThread autoSaveThread;

    // 构造函数需要传入文档保存的路径和文档名称
    private Document(String documentPath, String documentName) throws IOException {
        this.writer = new FileWriter(new File(documentPath, documentName));
    }

    // 静态方法， 主要用于创建文档， 顺便启动自动保存文档的线程
    public static Document create(String documentPath, String documentName) throws IOException {
        Document document = new Document(documentPath, documentName);
        autoSaveThread = new AutoSaveThread(document);
        autoSaveThread.start();
        return document;
    }

    // 文档的编辑，其实就是往content 队列中提交字符串
    public void edit(String content) {
        synchronized (this) {
            this.content.add(content);
            this.changed = true;
        }
    }

    // 文档关闭的时候首先中断自动保存线程，然后关闭writer 释放资源
    public void close() throws IOException {
        autoSaveThread.interrupt();
        writer.close();
    }

    public void save() throws IOException {
        synchronized (this) {
            // balking， 如果文档已经被保存了， 则直接返回
            if (!changed) {
                return;
            }

            System.out.println(Thread.currentThread() + " execute the save action");
            for (String cacheLine: content) {
                this.writer.write(cacheLine);
                this.writer.write("\r\n");
            }
            this.writer.flush();
            // 将changed 修改为false ，表明此刻再没有新的内容编辑
            this.changed = false;
            this.content.clear();
        }
    }
}
