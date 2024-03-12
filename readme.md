# 论文查重

| 这个作业属于哪个课程 | [软件工程2024](https://edu.cnblogs.com/campus/gdgy/SoftwareEngineering2024) |
| -------------------- | ------------------------------------------------------------ |
| 这个作业要求在哪里   | [论文查重](https://edu.cnblogs.com/campus/gdgy/SoftwareEngineering2024/homework/13136) |
| 这个作业的目标       | 学习如何作为软件工程师开发项目                               |

[仓库地址:Nacyoooooo](https://github.com/Nacyoooooo/3122004778)

👇👇👇👇👇代码克隆👇👇👇👇

```git
$ https://github.com/Nacyoooooo/3122004778.git
```

## 开发环境

| **开发工具** | **IntelliJ IDEA 2023.2.1**                                |
| ------------ | --------------------------------------------------------- |
| **编程语言** | **java**                                                  |
| **运行环境** | **Java(TM) SE Runtime Environment (build 1.8.0_381-b09)** |
| **构建工具** | **maven**                                                 |
| **编译环境** | **java version "1.8.0_381"**                              |

## 程序依赖

| **依赖名称**    | **作用**               | **版本号**         |
| --------------- | ---------------------- | ------------------ |
| **hanlp**       | **长文本分割**         | **portable-1.8.3** |
| **testng**      | **单元测试（编译时）** | **RELEASE**        |
| **hutool-core** | **IO流操作**           | **5.8.18**         |
| **junit**       | **单元测试（运行时）** | **4.13.1**         |



## **需求分析**

**设计一个可以通过命令行参数传递原文文件地址、抄袭文件地址、答案文件地址，计算抄袭文件相对于原文文件的重复率，并将结果以保留小数点后两位的形式输出到答案文件。**

## **计算模块接口的设计与实现过程**

$$
main(String[] args)->run(String[] args)->read(String filePath)->getSimHash(String str)
->getSimilarity(int[] simHash1, int[] simHash2)->append(String filePath,String content)
$$

#### **对中文字段进行分词**

**使用hanlp，对中文字符串进行精确模式分词 , 取出所有关键词。**

```java
List<String> keywordList = extractKeyword(str, str.length());
```

**选用SHA-256算法提高精度**

```java
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
```

**对文本求取其SimHash值**

```java
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
            if(vector[j]<=0){
                vector[j]=0;
            }else vector[j]=1;
        }
        return vector;
    }
```

**通过SimHash求取文本的海明距离**

```java
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
```

**求取文本相似度**

```java
public static double getSimilarity(int[] simHash1, int[] simHash2) {
    // 通过 simHash1 和 simHash2 获得它们的海明距离
    // 通过海明距离计算出相似度，并返回
    return 0.01 * (100 - (double) (getHammingDistance(simHash1, simHash2) * 100) / 128);
}
```

**由主类启动**

```java
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
```

**其他工具类IOUtils基于开源框架[Hutool](https://www.hutool.cn/)实现**

```java
public class IOUtils {
    public static String read(String filePath){
        return new FileReader(filePath).readString();
    }
    public static void write(String filePath,String content){
        new FileWriter(filePath).write(content);
    }
    public static void append(String filePath,String content){
        new FileWriter(filePath).append(content+"\n");
    }
}
```

### **计算模块接口部分的性能**

**![image-20240312224307128](C:\Users\86159\AppData\Roaming\Typora\typora-user-images\image-20240312224307128.png)**

**![image-20240312224338938](C:\Users\86159\AppData\Roaming\Typora\typora-user-images\image-20240312224338938.png)**

## **计算模块部分异常处理说明**

```java
//断言匹配确保输入的有三个路径
Assert.isTrue((args!=null) && (args.length==3),"need three parameters!");
```

```java
// 文本长度太短时HanLp无法取得关键字
Assert.isFalse(str.length() < 200,"text length is too short!");
```

## **测试单元**

```java
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
```

```java
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
```

## **PSP表格**

| **PSP**                                     |    **Personal Software Process Stages**     | **预估耗时（分钟）** | **实际耗时（分钟）** |
| :------------------------------------------ | :-----------------------------------------: | -------------------: | -------------------: |
| **Planning**                                |                  **计划**                   |                      |                      |
| **· Estimate**                              |       **· 估计这个任务需要多少时间**        |                **5** |               **10** |
| **Development**                             |                  **开发**                   |                      |                      |
| **· Analysis**                              |       **· 需求分析 (包括学习新技术)**       |               **30** |              **180** |
| **· Design Spec**                           |             **· 生成设计文档**              |               **60** |               **60** |
| **· Design Review**                         |               **· 设计复审**                |               **30** |               **45** |
| **· Coding Standard**                       | **· 代码规范 (为目前的开发制定合适的规范)** |               **20** |               **10** |
| **· Design**                                |               **· 具体设计**                |               **20** |               **20** |
| **· Coding**                                |               **· 具体编码**                |               **60** |               **75** |
| **· Code Review**                           |               **· 代码复审**                |               **30** |               **20** |
| **· Test**                                  | **· 测试（自我测试，修改代码，提交修改）**  |               **60** |               **75** |
| **Reporting**                               |                  **报告**                   |                      |                      |
| **· Test Repor**                            |               **· 测试报告**                |               **60** |               **60** |
| **· Size Measurement**                      |              **· 计算工作量**               |               **60** |               **60** |
| **· Postmortem & Process Improvement Plan** |     **· 事后总结, 并提出过程改进计划**      |               **60** |               **60** |
|                                             |                 **· 合计**                  |              **495** |              **675** |