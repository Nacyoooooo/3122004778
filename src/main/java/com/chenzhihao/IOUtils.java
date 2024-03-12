package com.chenzhihao;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;

public class IOUtils {
    public static String read(String filePath){
        FileReader fileReader=new FileReader(filePath);
        return fileReader.readString();
    }
    public static void write(String filePath,String content){
        FileWriter fileWriter = new FileWriter(filePath);
        fileWriter.write(content);
    }
    public static void append(String filePath,String content){
        FileWriter fileWriter = new FileWriter(filePath);
        fileWriter.append(content);
    }
}
