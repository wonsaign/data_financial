package com.deal.datafinancial.tools;

import com.google.common.base.Charsets;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
public class FileMove2 {


    public static File toFile = new File("F:\\AllJavaCodes2.txt");

    public static String src = "F:\\aaaa1";

    public static String tar = "F:\\xxxx";

    public static void main(String[] args) throws IOException {
        //getJavaFiles(src);
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
                String fName = f.getName();
                String fileSe = "/*\n" +
                        " * File:  " + fName+ "  \n" +
                        " * 本程序为北京植物医生生物科技有限公司《植物医生小植商城 V1.0》\n" +
                        " * 的一部分。\n" +
                        " * 禁止复制，更改源码和逆向工程和以及用于未经书面授权的任何商业用途。\n" +
                        " * Copyright 2021-2022, 北京植物医生生物科技有限公司，所有版权保留。\n" +
                        " */";
                Files.asCharSink(toFile , Charsets.UTF_8, FileWriteMode.APPEND).write("\n");
                Files.asCharSink(toFile , Charsets.UTF_8, FileWriteMode.APPEND).write(fileSe);
                Files.asCharSink(toFile , Charsets.UTF_8, FileWriteMode.APPEND).write("\n");
                for (String line : readLines) {
                    Files.asCharSink(toFile , Charsets.UTF_8, FileWriteMode.APPEND).write(line+"\n");
                }
                //break;
            }
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
