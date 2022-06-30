package com.myteam.eazylunch;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import com.google.firebase.auth.FirebaseUser;
import com.myteam.eazylunch.databinding.ActivitySignInBinding;


public class SignIn extends AppCompatActivity {

    FirebaseUser user;
    ActivitySignInBinding binding;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(SignIn.this,R.color.primary));
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.getotpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = "+91"+ binding.phoneBox.getText().toString();
                Intent intent = new Intent(SignIn.this,OtpActivity.class);
                intent.putExtra("phoneNumber",mobile);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onStart(){
        super.onStart();
        if(user!=null){
            startActivity(new Intent(SignIn.this,OrderActivity.class));
        }
    }
}