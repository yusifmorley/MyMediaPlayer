package com.morley.myvideoplayer.Impl;

import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import static com.morley.myvideoplayer.Impl.HelloApplicationImpl.poistion;

public class OnEndOfMediaRunnable implements Runnable{

    MediaView mediaView;
    ObservableList<MediaPlayer> mediaPlayers;
    Slider slhorizon;
    Duration allDuration;
    Label timelabel;
    public OnEndOfMediaRunnable( MediaView mediaView, ObservableList<MediaPlayer> mediaPlayers,Duration allDuration ,Slider slhorizon, Label timelabel){

       this.mediaView=mediaView;
       this.mediaPlayers=mediaPlayers;
       this.allDuration=allDuration;
       this.slhorizon=slhorizon;
       this.timelabel=timelabel;
    }

    @Override
    public void run() {
        poistion++;
        if (poistion>=mediaPlayers.size()-1)poistion=0;
        mediaView.setMediaPlayer(mediaPlayers.get(poistion));
        mediaPlayers.get(poistion).setAutoPlay(true);
        allDuration=mediaPlayers.get(poistion).getStopTime();
        //监听事件
        mediaPlayers.get(poistion).currentTimeProperty().addListener(ov->{
            Duration currentTime = mediaPlayers.get(poistion).getCurrentTime();
            slhorizon.setValue(currentTime.toMillis()/allDuration.toMillis() * 100);
        });

        slhorizon.valueProperty().addListener(ov->{
            if(slhorizon.isValueChanging()) {     //加入Slider正在改变的判定，否则由于update线程的存在，mediaPlayer会不停地回绕
                mediaPlayers.get(poistion).seek(allDuration.multiply(slhorizon.getValue() / 100.0));
            }
        });

        mediaPlayers.get(poistion).currentTimeProperty().addListener(ov->{
            timelabel.setText(formatTime(mediaPlayers.get(poistion).getCurrentTime(),allDuration));
        });

    }

    public static String formatTime(Duration elapsed,Duration duration){
        //将两个Duartion参数转化为 hh：mm：ss的形式后输出
        int intElapsed = (int)Math.floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        int elapsedMinutes = (intElapsed - elapsedHours *60 *60)/ 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60 - elapsedMinutes * 60;
        if(duration.greaterThan(Duration.ZERO)){
            int intDuration = (int)Math.floor(duration.toSeconds());
            int durationHours = intDuration / (60 * 60);
            int durationMinutes = (intDuration - durationHours *60 * 60) / 60;
            int durationSeconds = intDuration - durationHours * 60 * 60 - durationMinutes * 60;

            if(durationHours > 0){
                return String.format("%02d:%02d:%02d / %02d:%02d:%02d",elapsedHours,elapsedMinutes,elapsedSeconds,durationHours,durationMinutes,durationSeconds);
            }else{
                return String.format("%02d:%02d / %02d:%02d",elapsedMinutes,elapsedSeconds,durationMinutes,durationSeconds);
            }
        }else{
            if(elapsedHours > 0){
                return String.format("%02d:%02d:%02d / %02d:%02d:%02d",elapsedHours,elapsedMinutes,elapsedSeconds);
            }else{
                return String.format("%02d:%02d / %02d:%02d",elapsedMinutes,elapsedSeconds);
            }
        }
    }
}
