package com.solvedunsolved.plantsymbiosis.Activities;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import androidx.appcompat.app.AppCompatActivity;
import com.solvedunsolved.plantsymbiosis.R;

import maes.tech.intentanim.CustomIntent;

public class splashscreen extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(splashscreen.this, MainActivity.class));
                finish();
            }
        }, 1500);
    }
}
