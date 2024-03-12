package com.chenzhihao;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;

public class IOUtils {
    public static String read(String filePath){
        return new FileReader(filePath).readString();
    }
    public static void write(String filePath,String content){
        new FileWriter(filePath).write(content);
    }
    public static void append(String filePath,String content){
        new FileWriter(filePath).append(content+"\n");
    }
}
