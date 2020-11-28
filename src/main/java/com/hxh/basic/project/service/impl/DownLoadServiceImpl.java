package com.hxh.basic.project.service.impl;

import ch.qos.logback.core.util.FileUtil;
import com.hxh.basic.project.common.MyThread;
import com.hxh.basic.project.service.IDownLoadService;
import com.hxh.basic.project.utils.http.FileUtils;
import com.hxh.basic.project.utils.http.HttpHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class DownLoadServiceImpl implements IDownLoadService {
    @Override
    public Object dwonFileByUrl(String url, String xcx_location) {

        String fileName = UUID.randomUUID() + ".mp4";
        try {
            HttpHelper.downLoadFromUrl(url, fileName, xcx_location);
            ThreadPoolExecutor publicInstance = MyThread.getPublicInstance();
            publicInstance.execute(() -> {
                try {
                    FileUtils.getInstance().delteFilesByPath(xcx_location);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }
}
