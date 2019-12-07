package com.example.android.accelerometerplay;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class SubActivity extends Activity {
    private int talkcounter = 0 ;
    private TextView talk_message;
    private TextView touch_screen;
    private Handler mHandler = new Handler();
    private ScheduledExecutorService mScheduledExecutor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setScreenTalk();
        setScreenMain();
    }

    private void setScreenMain(){
        setContentView(R.layout.activity_sub);
        touch_screen = (TextView) findViewById(R.id.touch_screen);
        startButton();
        Button startButton = findViewById(R.id.touch_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setScreenTalk();
            }
        });
    }

    private void startButton() {
        touch_screen = (TextView) findViewById(R.id.touch_screen);
        mScheduledExecutor = Executors.newScheduledThreadPool(2);

        mScheduledExecutor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        touch_screen.setVisibility(View.VISIBLE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            animateAlpha();
                        }
                    }
                });
            }

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            private void animateAlpha() {

                List<Animator> animatorList = new ArrayList<Animator>();
                ObjectAnimator animeFadeIn = ObjectAnimator.ofFloat(touch_screen,
                                                        "alpha",
                                                             0f, 1f);
                animeFadeIn.setDuration(1500);

                ObjectAnimator animeFadeOut = ObjectAnimator.ofFloat(touch_screen,
                                                        "alpha",
                                                             1f, 0f);
                animeFadeOut.setDuration(1000);

                animatorList.add(animeFadeIn);
                animatorList.add(animeFadeOut);

                final AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playSequentially(animatorList);
                animatorSet.start();
            }
        }, 0, 2500, TimeUnit.MILLISECONDS);
    }



    private void setScreenTalk(){
        setContentView(R.layout.activity_talk);
        talk_message = (TextView)this.findViewById(R.id.text);
        talk_message.setTextColor(Color.WHITE);
        final ImageView onayamiman = this.findViewById(R.id.onayamiman);
        onayamiman.setImageResource(R.drawable.onayami);
        final ImageView muscle = this.findViewById(R.id.muscle);

        Button actionButtonNext = findViewById(R.id.tsugihe);

        actionButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                talkcounter++;
                switch (talkcounter){
                    case 1:
                        onayamiman.setAlpha(100);
                        muscle.setAlpha(255);
                        muscle.setImageResource(R.drawable.muscle);
                        talk_message.setText(R.string.b_talk1);
                        break;
                    case 2:
                        onayamiman.setAlpha(255);
                        muscle.setAlpha(100);
                        talk_message.setText(R.string.a_talk2);
                        break;
                    case 3:
                        onayamiman.setAlpha(100);
                        muscle.setAlpha(255);
                        talk_message.setText(R.string.b_talk2);
                        break;
                    case 4:
                        onayamiman.setAlpha(255);
                        muscle.setAlpha(100);
                        talk_message.setText(R.string.a_talk3);
                        break;
                    case 5:
                        onayamiman.setAlpha(100);
                        muscle.setAlpha(255);
                        talk_message.setText(R.string.b_talk3);
                        break;
                    case 6:
                        setContentView(R.layout.activity_sub2);
                        talk_message = (TextView)findViewById(R.id.text);
                        talk_message.setTextColor(Color.WHITE);

                        Button confirmButton = findViewById(R.id.button_sub1);
                        confirmButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setContentView(R.layout.activity_talk);
                                talk_message = (TextView)findViewById(R.id.text);
                                talk_message.setTextColor(Color.WHITE);
                                talk_message.setText(R.string.b_talk_after_select1);
                                final ImageView onayamiman = findViewById(R.id.onayamiman);
                                onayamiman.setImageResource(R.drawable.onayami);
                                final ImageView muscle = findViewById(R.id.muscle);
                                muscle.setImageResource(R.drawable.muscle);
                                onayamiman.setAlpha(100);
                                muscle.setAlpha(255);
                                Button actionButtonNext = findViewById(R.id.tsugihe);
                                actionButtonNext.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        talkcounter = 0;
                                        setReadyScreen();
                                    }
                                });
                            }
                        });
                        Button returnButton = findViewById(R.id.button_sub2);
                        returnButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setContentView(R.layout.activity_talk);
                                talk_message = (TextView)findViewById(R.id.text);
                                talk_message.setTextColor(Color.WHITE);
                                talk_message.setText(R.string.b_talk_after_select2);
                                final ImageView onayamiman = findViewById(R.id.onayamiman);
                                final ImageView muscle = findViewById(R.id.muscle);
                                Button actionButtonNext = findViewById(R.id.tsugihe);
                                actionButtonNext.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        talkcounter = 0;
                                        setScreenTalk();
                                    }
                                });
                            }
                        });
                        break;
                }
            }
        });
    }

    private void setDecisionScreen(){
        setContentView(R.layout.activity_sub2);
        Button confirmButton = findViewById(R.id.button_sub1);
        talk_message = (TextView)this.findViewById(R.id.text);
        talk_message.setTextColor(Color.WHITE);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setReadyScreen();
                talkcounter = 0;
            }
        });

        Button returnButton = findViewById(R.id.button_sub2);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setScreenTalk();
                talkcounter = 0;
            }
        });
    }

    private void setReadyScreen(){
        setContentView(R.layout.activity_sub3);
        final ImageView imageView2  = this.findViewById(R.id.enemy_view);
        final ImageView actionView1 = this.findViewById(R.id.action_view1);
        final ImageView actionView2 = this.findViewById(R.id.action_view2);
        final ImageView actionView3 = this.findViewById(R.id.action_view3);
        imageView2.setImageResource(R.drawable.enemy015a);
        actionView1.setImageResource(R.drawable.human_default1);
        actionView2.setImageResource(R.drawable.squwat);
        actionView3.setImageResource(R.drawable.swing);

        Button actionButton1 = findViewById(R.id.button_sub1);
        actionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "1";
                setScreenPreDescription(msg);
            }
        });
        Button actionButton2 = findViewById(R.id.button_sub2);
        actionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "2";
                setScreenPreDescription(msg);
            }
        });
        Button actionButton3 = findViewById(R.id.button_sub3);
        actionButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "3";
                setScreenPreDescription(msg);
             }
        });
    }

    private void setScreenPreDescription(final String _msg){
        setContentView(R.layout.activity_predescription);

        final ImageView imageView_pre  = this.findViewById(R.id.enemy_view);
        final ImageView actionView_pre = this.findViewById(R.id.action_description);
        final TextView actionName      = this.findViewById(R.id.action_description_text);
        switch (Integer.valueOf(_msg)) {
            case 1:
                actionName.setText(R.string.activity1);
                actionView_pre.setImageResource(R.drawable.human_default1);
                break;
            case 2:
                actionName.setText(R.string.activity2);
                actionView_pre.setImageResource(R.drawable.squwat);
                break;
            case 3:
                actionName.setText(R.string.activity3);
                actionView_pre.setImageResource(R.drawable.swing);
                break;
        }
        Button returnButton = findViewById(R.id.button_next);
        final Intent intent = new Intent(getApplication(),
                                            AccelerometerPlayActivity.class);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                intent.putExtra("act", _msg);
                startActivity(intent);
            }
        });
    }

}