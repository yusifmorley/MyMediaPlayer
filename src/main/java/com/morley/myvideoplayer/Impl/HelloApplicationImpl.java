package com.morley.myvideoplayer.Impl;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;


public class HelloApplicationImpl extends Application {
     private   MediaView mediaView;
     private final ObservableList<MediaPlayer> mediaPlayers=FXCollections.observableArrayList();
     private int poistion;
     private final MediaConfigurationImpl mediaConfiguration;
     private Duration allDuration;
     private Boolean play=true;

   public HelloApplicationImpl() throws IOException {
           this.mediaConfiguration=new MediaConfigurationImpl();//返回播放上次位置
           //文件位置
           this.poistion=mediaConfiguration.read()-48; //由char 变为int
   }

   @Override
    public void start(Stage stage) throws IOException {
        FileArrayCreaterImpl fileArrayCreater=new FileArrayCreaterImpl();
        int i=0;
          for(Path f:fileArrayCreater.createFileObjectArray()){
              //System.out.println(i++); //计数媒体个数
              //初始化媒体 对象
            mediaPlayers.add( new MediaPlayer(new Media(f.toUri().toString())));
          }
        //当前 要播放的媒体对象  也就是上一次 位置
        mediaView=new MediaView(mediaPlayers.get(this.poistion));

        Slider slhorizon=new Slider(); //进度条
        slhorizon.setMax(100);
        slhorizon.setShowTickLabels(true);
        slhorizon.setShowTickMarks(true);
        Label timelabel=new Label();

        // 时间 初始化
        mediaPlayers.get(poistion).setOnReady(new Runnable() {
            @Override
            public void run() {
                allDuration=mediaPlayers.get(poistion).getStopTime();
            }
        });
        //监听器
        mediaPlayers.get(poistion).currentTimeProperty().addListener(ov->{
            timelabel.setText(OnEndOfMediaRunnable.formatTime(mediaPlayers.get(poistion).getCurrentTime(),allDuration));
        });

        Button back =new Button("<<");
        Button forward=new Button(">>");
        Button nextButton=new Button("next");
        Button pauseButton=new Button("pause");
        BorderPane borderPane=new BorderPane();
        HBox hBox=new HBox(10);
        hBox.setAlignment(Pos.CENTER);
        VBox vBox=new VBox();
        vBox.getChildren().addAll(slhorizon,hBox);
        hBox.getChildren().addAll(timelabel,nextButton,pauseButton,back,forward);
        borderPane.setCenter(this.mediaView);
        borderPane.setBottom(vBox);

        nextButton.setOnAction(e->{
            if(!play){   //开启媒体
                mediaPlayers.get(poistion).play();
                play=true;
            }
            //点击事件 一旦点击 立即发生
          mediaPlayers.get(poistion).seek(Duration.INDEFINITE);//媒体进度为结束
          mediaPlayers.get(poistion).setOnEndOfMedia(new OnEndOfMediaRunnable(
                  poistion,mediaView,mediaPlayers,allDuration,slhorizon,timelabel));
        });

        pauseButton.setOnAction(e->{
            if (play){
                mediaPlayers.get(poistion).pause();
                play=false;
            }
            else {
                mediaPlayers.get(poistion).play();
                play=true;
            }
        });
        back.setOnAction(e->{
            mediaPlayers.get(poistion).seek(mediaPlayers.get(poistion).getCurrentTime().subtract(mediaPlayers.get(poistion).getCurrentTime().divide(5)));
        });

        forward.setOnAction(e->{
            allDuration=mediaPlayers.get(poistion).getStopTime();
            mediaPlayers.get(poistion).seek(mediaPlayers.get(poistion).getCurrentTime().add(mediaPlayers.get(poistion).getCurrentTime().divide(5)));
        });

        Scene scene=new Scene(borderPane,650,500);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                String str="last poistion: "+poistion;
                try {
                    mediaConfiguration.write(str);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

       mediaPlayers.get(poistion).currentTimeProperty().addListener(ov->{
            Duration currentTime = mediaPlayers.get(poistion).getCurrentTime();
            slhorizon.setValue(currentTime.toMillis()/allDuration.toMillis() * 100);
        });
        slhorizon.valueProperty().addListener(ov->{
            if(slhorizon.isValueChanging()) {     //加入Slider正在改变的判定，否则由于update线程的存在，mediaPlayer会不停地回绕
                mediaPlayers.get(poistion).seek(allDuration.multiply(slhorizon.getValue() / 100.0));
            }
        });
        //视频播放结束
        mediaPlayers.get(poistion).setOnEndOfMedia(new OnEndOfMediaRunnable(
                poistion,mediaView,mediaPlayers,allDuration,slhorizon,timelabel));

        mediaPlayers.get(poistion).setAutoPlay(true);
    }
    public static void main(String[] args) throws IOException {
         launch(args);

    }
}