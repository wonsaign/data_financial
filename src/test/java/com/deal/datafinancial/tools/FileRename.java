package com.deal.datafinancial.tools;

import java.io.File;
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
public class FileRename {

    public static void main(String[] args) {
        String pre = "/Volumes/Extreme SSD/files/vlogs/";
        String name = "1";
        String path = pre + name;
        File file = new File(path);
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if(f.getName().startsWith("._")){
                continue;
            }
            String last = f.getName().substring(f.getName().lastIndexOf("."));
            if(!f.isDirectory()){
                File nf = new File(path + "/" + UUID.randomUUID() + last);
                f.renameTo(nf);
            }
        }
    }
}
