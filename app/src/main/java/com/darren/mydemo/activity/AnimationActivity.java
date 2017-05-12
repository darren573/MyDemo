package com.darren.mydemo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.darren.mydemo.MainActivity;
import com.darren.mydemo.R;

import java.util.Timer;
import java.util.TimerTask;

public class AnimationActivity extends AppCompatActivity {
    private ImageView iv_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        iv_view = (ImageView) findViewById(R.id.iv_view);

        Animation animation = AnimationUtils.loadAnimation(this,
                R.anim.scale_item);
        iv_view.startAnimation(animation);
        final Intent intent = new Intent(this, MainActivity.class);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivity(intent);
            }
        };
        timer.schedule(task, 1000 * 3);//3秒之后跳转
    }
}
