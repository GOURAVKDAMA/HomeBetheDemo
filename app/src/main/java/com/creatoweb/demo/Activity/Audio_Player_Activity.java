package com.creatoweb.demo.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.creatoweb.demo.R;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Audio_Player_Activity extends AppCompatActivity
{
    ImageView iv_pause,iv_play,iv_current_fav,iv_cross;
    MediaPlayer mediaPlayer;
    double startTime = 0;
    Handler myHandler = new Handler();;
    TextView tv_current_audio_times,tv_current_audio_name,tv_app_name;
    static int oneTimeOnly = 0;
    String url,name;
    Typeface MontReg;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio__player_);

        Intent intent =getIntent();
        url=intent.getStringExtra("url");
        name=intent.getStringExtra("name");

        mediaPlayer = new MediaPlayer();
        tv_app_name = (TextView) findViewById(R.id.tv_app_name);
        iv_cross = (ImageView) findViewById(R.id.iv_cross);
        iv_current_fav = (ImageView) findViewById(R.id.iv_current_fav);
        iv_pause = (ImageView) findViewById(R.id.iv_pause);
        iv_play = (ImageView) findViewById(R.id.iv_play);
        tv_current_audio_times = (TextView)findViewById(R.id.tv_current_audio_times);
        tv_current_audio_name = (TextView)findViewById(R.id.tv_current_audio_name);
        tv_current_audio_name.setText(name);
        try
        {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        iv_pause.setEnabled(false);

        iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Playing sound",Toast.LENGTH_SHORT).show();
                mediaPlayer.start();
                startTime = mediaPlayer.getCurrentPosition();
                if (oneTimeOnly == 0) {
                    oneTimeOnly = 1;
                }

                tv_current_audio_times.setText(String.format("%d:%d",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                        startTime)))
                );

                myHandler.postDelayed(UpdateSongTime,100);
                iv_pause.setEnabled(true);
                iv_play.setEnabled(false);
            }
        });

        iv_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Pausing sound",Toast.LENGTH_SHORT).show();
                mediaPlayer.pause();
                iv_pause.setEnabled(false);
                iv_play.setEnabled(true);
            }
        });
        MontReg = Typeface.createFromAsset(getApplication().getAssets(), "Montserrat-Regular.otf");
        tv_app_name.setTypeface(MontReg);
        tv_current_audio_name.setTypeface(MontReg);
        tv_current_audio_times.setTypeface(MontReg);
        iv_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                mediaPlayer.stop();
            }
        });
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            tv_current_audio_times.setText(String.format("%d:%d",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            myHandler.postDelayed(this, 100);
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mediaPlayer.stop();;
    }
}
