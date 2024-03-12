package com.chenzhihao;

import cn.hutool.core.io.FileUtil;
import org.junit.Test;

import java.io.File;

public class MainActivityTest {
    @Test
    public void test(){
        File file = FileUtil.file("D:\\code\\java\\duplicate_checking_czh\\src\\main\\resources");
        System.out.println(file.isDirectory());
        String[] names = file.list();
        for (String name : names) {
            if (!name.equals("orig.txt")){
                if(name.startsWith("orig")){
                    String ansPath="D:\\code\\java\\duplicate_checking_czh\\src\\main\\resources\\ansTest.txt";
                    String ori="D:\\code\\java\\duplicate_checking_czh\\src\\main\\resources\\orig.txt";
                    String[]s={ori,name,ansPath};
                    MainActivity.run(s);
                }
            }
        }
    }
}
