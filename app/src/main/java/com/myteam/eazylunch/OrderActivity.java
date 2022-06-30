package com.myteam.eazylunch;
import com.myteam.eazylunch.MenuActivity;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {
    FirebaseAuth auth;
    Dialog myDialog;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        getWindow().setStatusBarColor(ContextCompat.getColor(OrderActivity.this,R.color.primary));
        myDialog = new Dialog(this);
        ImageView mainIcon = findViewById(R.id.mainIcon);
        ImageView backIcon = findViewById(R.id.backIcon);
        mainIcon.setVisibility(View.VISIBLE);
        backIcon.setVisibility(View.GONE);
        mainIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth = FirebaseAuth.getInstance();
                showPopup(v);
            }
        });


        //------------Bottom Navigation Bar Starts ---------------------//

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.orders);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.orders:
                        return false;
                    case R.id.foodmenu:
                        startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.waiters:
                        startActivity(new Intent(getApplicationContext(), WaiterActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                }
                return true;
            }
        });

        //------------Bottom Navigation Bar Ends -----------------------//


    }
    public void showPopup(View v){
        ImageView close;
        Button logout;
        TextView phoneNumber;
        myDialog.setContentView(R.layout.my_pop_up);
        close = (ImageView)myDialog.findViewById(R.id.close);
        logout = (Button) myDialog.findViewById(R.id.logout);
        phoneNumber = (TextView)myDialog.findViewById(R.id.PhoneNumberInPopUp);
        phoneNumber.setText(auth.getCurrentUser().getPhoneNumber());
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(OrderActivity.this,SignIn.class));
                finish();
            }
        });
        myDialog.show();
    }

}