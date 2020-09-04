package com.example.iburger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {

    Animation fadeout_animation , fadein_animation;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        imageView = (ImageView) findViewById(R.id.logo);
//        fadein_animation = AnimationUtils.loadAnimation(this , R.anim.logo_fade_in_anim);
        fadeout_animation = AnimationUtils.loadAnimation(this , R.anim.logo_fade_out_anim);

                imageView.setAnimation(fadeout_animation);
        /*---------- move to login ------------*/
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getApplicationContext(),Login.class));
                        finishAffinity();
                    }
                },4000);
    }
}
