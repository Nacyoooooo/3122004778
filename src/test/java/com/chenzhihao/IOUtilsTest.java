package com.chenzhihao;

import com.chenzhihao.IOUtils;
import org.junit.Test;

public class IOUtilsTest {
    @Test
    public void readTxtTest(){
        System.out.println(IOUtils.read("D:\\code\\java\\duplicate_checking_czh\\src\\main\\resources\\orig.txt"));
    }
    @Test
    public void writeTest(){
        String filePath="D:\\code\\java\\duplicate_checking_czh\\src\\main\\resources\\test.txt";
        String content="test";
        IOUtils.write(filePath,content);
        System.out.println(IOUtils.read(filePath).equals(content));
    }
    @Test
    public void appendTest(){
        String filePath="D:\\code\\java\\duplicate_checking_czh\\src\\main\\resources\\test.txt";
        String content="test";
        IOUtils.append(filePath,content);
    }
}
