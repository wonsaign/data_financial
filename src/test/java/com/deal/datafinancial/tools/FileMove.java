package com.deal.datafinancial.tools;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.UUID;

/**
 * All rights Reserved, Designed By www.drplant.com.cn
 *
 * @ProjectName: dc-service-sales
 * @Package: com.zeusas.cloud.dc.java
 * @ClassName: FileMove
 * @Description: []
 * @Author: [wang sai]
 * @Date: 2022/9/9 10:48 上午
 * @Version: V1.0
 * @Copyright: 2019 www.drplant.com.cn Inc. All rights reserved.
 **/
public class FileMove {


    public static File toFile = new File("F:\\AllJavaCodes.txt");

    public static String src = "F:\\aaaa";

    public static String tar = "F:\\bbbb";

    public static void main(String[] args) throws IOException {
        // getJavaFiles(src);
        appendToFile();
    }

    // 将所有的java代码全部移动到统一的一个文件
    public static void appendToFile() throws IOException{
        File file = new File(tar);
        File[] files = file.listFiles();
        for(File f : files){
            System.out.println(f);
            boolean isFile = f.isFile();
            if(isFile){

                List<String> readLines = Files.readLines(f, Charsets.UTF_8);
                //System.out.println(JSON.toJSON(readLines));
                for (String line : readLines) {
                    Files.asCharSink(toFile , Charsets.UTF_8, FileWriteMode.APPEND).write(line+"\n");
                }
                //break;
            }

            Files.asCharSink(toFile , Charsets.UTF_8, FileWriteMode.APPEND).write("\n");
            Files.asCharSink(toFile , Charsets.UTF_8, FileWriteMode.APPEND).write("\n");
            Files.asCharSink(toFile , Charsets.UTF_8, FileWriteMode.APPEND).write("\n");
            Files.asCharSink(toFile , Charsets.UTF_8, FileWriteMode.APPEND).write("\n");
        }
    }


    // 所有的java文件移动到某个目录
    public static void getJavaFiles(String path) throws IOException {
        File file = new File(path);		//获取其file对象
        File[] fs = file.listFiles();	//遍历path下的文件和目录，放在File数组中
        for(File f:fs){					//遍历File[]数组
            if(!f.isDirectory()){
                if(f.getPath().contains("src\\test")){
                    continue;
                }
                if(f.getName().endsWith(".java")){
                    System.out.println(f);
                    String newPath = tar + File.separator + f.getName();
                    System.err.println(newPath);
                    Files.copy(f, new File(newPath));
                }
            }else {
                getJavaFiles(f.getPath());
            }
        }
    }
}
