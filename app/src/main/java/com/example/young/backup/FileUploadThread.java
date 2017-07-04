package com.example.young.backup;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;
import android.util.Log;

/**
 * 文件下载类
 *
 * Created by Young on 2017/7/3.
 */
public class FileUploadThread extends Thread {

    private static final String TAG = FileUploadThread.class.getSimpleName();

    /** 当前上传是否完成 */
    private boolean UpIsCompleted = false;
    /** 当前上传文件长度 */
    private int uploadLength = 0;
    /** 文件上传路径 */
    private File file;
    /** 文件上传路径 */
    private URL uploadUrl;
    /** 当前上传线程ID */
    private int threadId;
    /** 线程上传数据长度 */
    private int blockSize;

    /**
     *
     * @param url:文件上传地址
     * @param file:文件上传路径
     * @param blocksize:上传数据长度
     * @param threadId:线程ID
     */
    public FileUploadThread(URL uploadUrl, File file, int blocksize,
                              int threadId) {
        this.uploadUrl = uploadUrl;
        this.file = file;
        this.threadId = threadId;
        this.blockSize = blocksize;
    }

    @Override
    public void run() {

        BufferedInputStream bis = null;
        RandomAccessFile raf = null;

        try {
            URLConnection conn = uploadUrl.openConnection();
            conn.setAllowUserInteraction(true);

            int startPos = blockSize * (threadId - 1);//开始位置
            int endPos = blockSize * threadId - 1;//结束位置

            //设置当前线程下载的起点、终点
            conn.setRequestProperty("Range", "bytes=" + startPos + "-" + endPos);
            System.out.println(Thread.currentThread().getName() + "  bytes="
                    + startPos + "-" + endPos);

            byte[] buffer = new byte[1024];
            bis = new BufferedInputStream(conn.getInputStream());

            raf = new RandomAccessFile(file, "rwd");
            raf.seek(startPos);
            int len;
            while ((len = bis.read(buffer, 0, 1024)) != -1) {
                raf.write(buffer, 0, len);
                uploadUrl += len;
            }
            UpIsCompleted = true;
            Log.d(TAG, "current thread task has finished,all size:"
                    + uploadLength);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 线程文件是否上传完毕
     */
    public boolean UpIsCompleted() {
        return UpIsCompleted;
    }

    /**
     * 线程下载文件长度
     */
    public int getUploadLength() {
        return uploadLength;
    }

}
