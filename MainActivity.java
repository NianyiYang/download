package com.yny.download;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yny.downloadlibrary.DownloadProgressListener;
import com.yny.downloadlibrary.FileDownloader;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnDownload = (Button) findViewById(R.id.btn_download);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileDownloader downloader = new FileDownloader(MainActivity.this,
                        "http://speed.myzone.cn/pc_elive_1.1.rar/",
                        new File(FileUtil.SDCardPath() + "/Test/"), 5);
                try {
                    downloader.download(new DownloadProgressListener() {
                        @Override
                        public void onDownloadSize(int fileSize, int downloadedSize, int percent, int time) {
                            Toast.makeText(MainActivity.this, "total size:" + fileSize + " downloaded size:" + downloadedSize + " percent:" + percent, Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
