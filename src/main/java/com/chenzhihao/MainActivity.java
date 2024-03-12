package com.chenzhihao;

import static com.chenzhihao.Hamming.getSimilarity;
import static com.chenzhihao.IOUtils.append;
import static com.chenzhihao.IOUtils.read;
import static com.chenzhihao.SimHash.getSimHash;

public class MainActivity {
    public static void main(String[] args) {
        MainActivity.run(args);
    }
    public static void run(String[] args){
        if(args==null||args.length!=3){
            System.out.println("need three parameters");
            return;
        }
        //分别输入所用及保存文件地址
        String originalPath = args[0];
        String copyPath = args[1];
        String resultFilePath = args[2];
        // 从输入的路径名读取对应的文件，将文件的内容转化为对应的字符串
        String original =read(originalPath);
        String copy = read(copyPath);
        // 由字符串得出对应的 simHash值
        int[] simHash = getSimHash(original);
        int[] simHash1 = getSimHash(copy);
        // 由 simHash值求出相似度
        double similarity = getSimilarity(simHash, simHash1);
        System.out.println(similarity*100+"%");
        append(resultFilePath,String.format("%.2f",similarity));
    }
}
