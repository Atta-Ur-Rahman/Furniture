package com.example.atta.furnitureapplication.view.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.atta.furnitureapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreen extends AppCompatActivity {

    @BindView(R.id.tv_splash)TextView tvSplash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        flipX();
        flipY();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
            }
        }, 3000);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    private void flipX() {
        ObjectAnimator flip2 = ObjectAnimator.ofFloat(tvSplash, "rotationX", 0f, 360f);
        ;
        flip2.setDuration(2000);
        flip2.start();
    }


    private void flipY() {
        ObjectAnimator flip2 = ObjectAnimator.ofFloat(tvSplash, "rotationY", 0f, 360f);
        flip2.setDuration(2000);
        flip2.start();
    }

}
