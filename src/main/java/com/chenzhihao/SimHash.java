package com.chenzhihao;

import com.hankcs.hanlp.HanLP;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SimHash {
    public static String getHash(String str) {
        try {
            // 这里使用了SHA-256获得hash值
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            return new BigInteger(1, messageDigest.digest(str.getBytes(StandardCharsets.UTF_8))).toString(2);
        } catch (Exception e) {
            e.printStackTrace();
            return str;
        }
    }
    public static int[] getSimHash(String str){
        // 文本长度太短时HanLp无法取得关键字
        try{
            if(str.length() < 200) throw new Exception("text length is too short!");
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        // 用数组表示特征向量,取256位,从 0 1 2 位开始表示从高位到低位
        int[] vector = new int[256];
        // 分词 基于外部依赖hankcs包
        // 取出所有关键词
        List<String> keywordList = HanLP.extractKeyword(str, str.length());
        // hash
        int size = keywordList.size();
        AtomicInteger i = new AtomicInteger();//以i做外层循环
        keywordList.forEach((keyword)->{
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
                    vector[j]+=(10 - (i.get() / (size / 10)));
                }else {
                    vector[j] -= (10 - (i.get() / (size / 10)));
                }
            }
            i.getAndIncrement();
        });
        for (int j = 0; j < vector.length; j++) {
            if(vector[j]<=0){
                vector[j]=0;
            }else vector[j]=1;
        }
        return vector;
    }
}