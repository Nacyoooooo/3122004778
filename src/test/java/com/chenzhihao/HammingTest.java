package com.chenzhihao;

import org.junit.Test;
import com.chenzhihao.SimHash;

import static com.chenzhihao.IOUtils.read;
import static com.chenzhihao.SimHash.getSimHash;


public class HammingTest {
    String file1="D:\\code\\java\\duplicate_checking_czh\\src\\main\\resources\\orig.txt";
    String file2="D:\\code\\java\\duplicate_checking_czh\\src\\main\\resources\\orig_0.8_add.txt";
    @Test
    public void getHammingDistanceTest(){
        int hammingDistance = Hamming.getHammingDistance(getSimHash(read(file1)), getSimHash(read(file2)));
        System.out.println(hammingDistance);
    }
    @Test
    public void getSimilarityTest(){
        double hammingDistance = Hamming.getSimilarity(getSimHash(read(file1)), getSimHash(read(file2)));
        System.out.println(hammingDistance);
    }
}
