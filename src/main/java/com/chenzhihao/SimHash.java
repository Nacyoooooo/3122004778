package com.chenzhihao;

import cn.hutool.core.lang.Assert;
import com.hankcs.hanlp.HanLP;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.function.Supplier;

import static java.security.MessageDigest.getInstance;
import static com.hankcs.hanlp.HanLP.extractKeyword;
import static java.nio.charset.StandardCharsets.UTF_8;

public class SimHash {
    public static String getHash(String str) {
        try {
            // 这里使用了SHA-256获得hash值
            return new BigInteger(1, getInstance("SHA-256").digest(str.getBytes(UTF_8))).toString(2);
        } catch (Exception e) {
            return str;
        }
    }
    public static int[] getSimHash(String str){
        // 文本长度太短时HanLp无法取得关键字
        Assert.isFalse(str.length() < 200,"text length is too short!");

        // 用数组表示特征向量,取256位,从 0 1 2 位开始表示从高位到低位
        int[] vector = new int[256];

        // 分词 基于外部依赖hankcs包
        // 取出所有关键词
        List<String> keywordList = extractKeyword(str, str.length());

        // hash
        int size = keywordList.size();
        AtomicInteger times = new AtomicInteger();
        keywordList.forEach(
                keyword->{
                    // 获取hash值
                    String keywordHash = getHash(keyword);
                    char[] Words = keywordHash.toCharArray();
                    // hash值可能少于256位，在低位以0补齐
                    if(keywordHash.length()<256){
                        char[] newWord=new char[256];
                        Arrays.fill(newWord,'0');
                        System.arraycopy(Words,0,newWord,0,Words.length);
                        Words=newWord;
                    }
                    // 加权与合并
                    for (int j = 0; j < Words.length; j++) {
                        if(Words[j]=='1'){
                            vector[j]+=(10 - (times.get() / (size / 10)));
                        }else {
                            vector[j] -= (10 - (times.get() / (size / 10)));
                        }
                    }
                    times.getAndIncrement();
                });
        for (int j = 0; j < vector.length; j++) {
            vector[j]=vector[j]<=0?0:1;
        }
        return vector;
    }
}
