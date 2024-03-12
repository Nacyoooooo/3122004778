package com.chenzhihao;

import java.util.Arrays;

public class Hamming {
    public static int getHammingDistance(int[] simHash1, int[] simHash2) {

        if (simHash1.length != simHash2.length) {
            // 出错，返回-1
            return -1;
        } else {
            int distance = 0;
            for (int i = 0; i < simHash1.length; i++) {
                // 每一位进行比较
                if (simHash1[i] != simHash2[i]) {
                    distance++;
                }
            }
            return distance;
        }
    }
    public static double getSimilarity(int[] simHash1, int[] simHash2) {
        // 通过 simHash1 和 simHash2 获得它们的海明距离
        // 通过海明距离计算出相似度，并返回
        return 0.01 * (100 - (double) (getHammingDistance(simHash1, simHash2) * 100) / 128);
    }
}
