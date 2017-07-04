package com.example.young.backup;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author young
 * @2017-6-30
 */
public class Upload extends Activity implements OnClickListener {

    private static final String TAG = Upload.class.getSimpleName();

    /** 显示上传进度TextView */
    private TextView mMessageView;
    /** 显示上传进度ProgressBar */
    private ProgressBar mProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        findViewById(R.id.upload_btn).setOnClickListener(this);
        mMessageView = (TextView) findViewById(R.id.upload_message);
        mProgressbar = (ProgressBar) findViewById(R.id.upload_progress);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.upload_btn) {
            doUpload();
        }
    }

    /**
     * 使用Handler更新UI界面信息
     */
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            mProgressbar.setProgress(msg.getData().getInt("size"));

            float temp = (float) mProgressbar.getProgress()
                    / (float) mProgressbar.getMax();

            int progress = (int) (temp * 100);
            if (progress == 100) {
                Toast.makeText(Upload.this, "上传完成！", Toast.LENGTH_LONG).show();
            }
            mMessageView.setText("上传进度:" + progress + " %");

        }
    };

    /**
     * 上传准备工作，获取SD卡路径、开启线程
     */
    private void doUpload() {
        // 获取SD卡路径
        String path = Environment.getExternalStorageDirectory()
                + "/amosdownload/";
        File file = new File(path);
        // 如果SD卡目录不存在创建
        if (!file.exists()) {
            file.mkdir();
        }
        // 设置progressBar初始化
        mProgressbar.setProgress(0);

        // 简单起见，我先把URL和文件名称写死，其实这些都可以通过HttpHeader获取到
        String uploadUrl = "http://gdown.baidu.com/data/wisegame/91319a5a1dfae322/baidu_16785426.apk";
        String fileName = "baidu_16785426.apk";
        int threadNum = 5;
        String filepath = path + fileName;
        Log.d(TAG, "upload file  path:" + filepath);
        uploadTask task = new uploadTask(uploadUrl, threadNum, filepath);
        task.start();
    }

    /**
     * 多线程文件上传
     *
     * 6-30
     */
    class uploadTask extends Thread {
        private String uploadUrl;// 上传链接地址
        private int threadNum;// 开启的线程数
        private String filePath;// 保存文件路径地址
        private int blockSize;// 每一个线程的上传量

        public uploadTask(String uploadUrl, int threadNum, String fileptah) {
            this.uploadUrl = uploadUrl;
            this.threadNum = threadNum;
            this.filePath = fileptah;
        }

        @Override
        public void run() {

            FileUploadThread[] threads = new FileUploadThread[threadNum];
            try {
                URL url = new URL(uploadUrl);
                Log.d(TAG, "download file http path:" + uploadUrl);
                URLConnection conn = url.openConnection();
                // 读取下载文件总大小
                int fileSize = conn.getContentLength();
                if (fileSize <= 0) {
                    System.out.println("读取文件失败");
                    return;
                }
                // 设置ProgressBar最大的长度为文件Size
                mProgressbar.setMax(fileSize);

                // 计算每条线程下载的数据长度
                blockSize = (fileSize % threadNum) == 0 ? fileSize / threadNum
                        : fileSize / threadNum + 1;

                Log.d(TAG, "fileSize:" + fileSize + "  blockSize:"+blockSize);

                File file = new File(filePath);
                for (int i = 0; i < threads.length; i++) {
                    // 启动线程，分别下载每个线程需要下载的部分
                    threads[i] = new FileUploadThread(url, file, blockSize,
                            (i + 1));
                    threads[i].setName("Thread:" + i);
                    threads[i].start();
                }

                boolean isfinished = false;
                int uploadedAllSize = 0;
                while (!isfinished) {
                    isfinished = true;
                    // 当前所有线程下载总量
                    uploadedAllSize = 0;
                    for (int i = 0; i < threads.length; i++) {
                        uploadedAllSize += threads[i].getUploadLength();
                        if (!threads[i].UpIsCompleted()) {
                            isfinished = false;
                        }
                    }
                    // 通知handler去更新视图组件
                    Message msg = new Message();
                    msg.getData().putInt("size", uploadedAllSize);
                    mHandler.sendMessage(msg);
                    // Log.d(TAG, "current uploadSize:" + downloadedAllSize);
                    Thread.sleep(1000);// 休息1秒后再读取下载进度
                }
                Log.d(TAG, " all of uploadSize:" + uploadedAllSize);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

}
