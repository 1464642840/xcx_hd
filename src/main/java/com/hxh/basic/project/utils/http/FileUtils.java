package com.hxh.basic.project.utils.http;

import ch.qos.logback.core.util.FileUtil;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FileUtils {
    private static FileUtils fileUtil= new FileUtils();
    private FileUtils(){};

   public static FileUtils getInstance() {
        return fileUtil;
    }



    public void delteFilesByPath(String tempLdatePath){
        File file = new File(tempLdatePath);
        if(file.exists()&&file.isDirectory()){
            File[] files = file.listFiles();
            //删除其余的文件,保存10个
            if(files.length>10){
                List<File> fileList = Arrays.asList(files);
                Collections.sort(fileList, new Comparator<File>() {
                    @Override
                    public int compare(File o1, File o2) {
                        long ll = o1.lastModified();
                        long ll2 = o2.lastModified();
                        return  ll==ll2?0:ll>ll2?-1:1;
                    }
                });

                for (int i = 10; i <fileList.size() ; i++) {
                    fileList.get(i).delete();
                }

            }


        }
    }



}
