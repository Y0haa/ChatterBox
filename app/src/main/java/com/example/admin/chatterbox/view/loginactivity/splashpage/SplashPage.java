package com.example.admin.chatterbox.view.loginactivity.splashpage;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

import com.example.admin.chatterbox.R;
import com.example.admin.chatterbox.view.loginactivity.mainlogin.MainLoginActivity;
import com.example.admin.chatterbox.view.mainactivity.MainActivity;

public class SplashPage extends AppCompatActivity {

    private static int SPLASH_TIME_OUT=10000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_page);

        reproduceVideo();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run(){
                Intent homeIntent = new Intent(SplashPage.this, MainLoginActivity.class);
                startActivity(homeIntent);
                finish();
            }

        }, SPLASH_TIME_OUT);
    }



    private void reproduceVideo() {
        VideoView mVideoView;

        mVideoView = (VideoView) findViewById(R.id.sBgVideoView);

        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.video2);

        mVideoView.setVideoURI(uri);
        mVideoView.start();

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });
    }
}
