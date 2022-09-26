package com.morley.myvideoplayer.Impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Objects;

import com.morley.myvideoplayer.FileArrayCreater;
import javafx.collections.ObservableList;
import javafx.scene.media.MediaPlayer;


//排序 实现compare接口
public class FileArrayCreaterImpl implements FileArrayCreater {
    //返回 dot之前的数字
    public int getseq(String str){
        return  Integer.parseInt(str.substring(0,str.indexOf('.')));
    }


    public LinkedList<Path> createFileObjectArray() throws IOException {
       LinkedList<Path> files=new LinkedList<>();
        Files.walkFileTree(Paths.get("src/main/resources/video/Telegram"),
                new SimpleFileVisitor<Path>(){
                    @Override
                    public FileVisitResult visitFile(Path p, BasicFileAttributes attrs)
                            throws IOException
                    {
                        files.add(p);
                        return FileVisitResult.CONTINUE;
                    }
                });


         return  files;
    }

    public static void main(String[] args) throws IOException {
        FileArrayCreaterImpl fileArrayCreater=new FileArrayCreaterImpl();
        for (Path p:fileArrayCreater.createFileObjectArray()
             ) {

            System.out.println(p.toString());
        }
    }

}
