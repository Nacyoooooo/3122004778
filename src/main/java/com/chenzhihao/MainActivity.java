package com.chenzhihao;

import cn.hutool.core.lang.Assert;

import static com.chenzhihao.Hamming.getSimilarity;
import static com.chenzhihao.IOUtils.append;
import static com.chenzhihao.IOUtils.read;
import static com.chenzhihao.SimHash.getSimHash;

public class MainActivity {
    public static void main(String[] args) {
        run(args);
    }
    public static void run(String[] args){
        //断言匹配
        Assert.isTrue((args!=null) && (args.length==3),"need three parameters!");
        //分别输入所用及保存文件地址
        // 从输入的路径名读取对应的文件，将文件的内容转化为对应的字符串
        // 由字符串得出对应的 simHash值
        // 由 simHash值求出相似度
        double similarity = getSimilarity(getSimHash(read(args[0])), getSimHash(read(args[1])));
        System.out.println(similarity*100+"%");
        append(args[2],String.format("%.2f",similarity));
    }
}