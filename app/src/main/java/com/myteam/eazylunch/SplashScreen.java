package com.myteam.eazylunch;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {
    FirebaseAuth auth;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setStatusBarColor(ContextCompat.getColor(SplashScreen.this,R.color.primary));
        auth = FirebaseAuth.getInstance();
        System.out.println("hi");
        System.out.println(auth.getCurrentUser());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(auth.getCurrentUser()==null){
                    Intent intent = new Intent(SplashScreen.this,SignIn.class);
                    startActivity(intent);
                    finish();
                }else{
                    startActivity(new Intent(SplashScreen.this,WaiterActivity.class));
                    finish();
                }
            }
        },1000);
    }
}