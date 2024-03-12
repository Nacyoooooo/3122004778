package com.chenzhihao;


import org.junit.Test;

public class SimHashTest {
    private String filePath="D:\\code\\java\\duplicate_checking_czh\\src\\main\\resources\\orig.txt";
    @Test
    public void getHashTest(){
        String c=SimHash.getHash(IOUtils.read(filePath));
        System.out.println(c);
        System.out.println(c.length());
    }
    @Test
    public void getSimHashTest(){
        int[] simHash = SimHash.getSimHash(IOUtils.read(filePath));
    }
}
