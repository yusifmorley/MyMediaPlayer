package com.morley.myvideoplayer.Impl;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MediaConfigurationImpl {
//记忆功能
    private final  String pa="src/main/resources/MediaConfiguration.txt";


public char read() throws IOException { //返回位置

        BufferedReader bu=  Files.newBufferedReader(Paths.get(pa));
        String s="";
        StringBuilder stringBuilder=new StringBuilder();
        while ((s=bu.readLine())!=null){
         stringBuilder.append(s);
        }
        s=stringBuilder.toString();
   return s.charAt(s.length()-1);
}



public void write(String str) throws IOException {
    FileWriter fw=new FileWriter(pa,true);
    fw.write("\n"+str+"\n");          //写入位置
    fw.flush();
    fw.close();
}

    public static void main(String[] args) throws IOException {
  MediaConfigurationImpl mediaConfiguration=new MediaConfigurationImpl();
        char read = mediaConfiguration.read();
        System.out.println(read);

    }

}
