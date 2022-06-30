package com.myteam.eazylunch;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddWaiters extends AppCompatActivity {
    EditText phoneBox;
    EditText waiterName;
    Button cancelbtn;
    Button savebtn;
    FirebaseFirestore dbroot;
    FirebaseAuth auth;
    ProgressDialog dialog;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_waiters);
        getWindow().setStatusBarColor(ContextCompat.getColor(AddWaiters.this,R.color.primary));
        ImageView mainIcon = findViewById(R.id.mainIcon);
        ImageView backIcon = findViewById(R.id.backIcon);
        cancelbtn = findViewById(R.id.cancelbtn);
        savebtn = findViewById(R.id.savebtn);
        phoneBox = findViewById(R.id.phoneBox);
        waiterName = findViewById(R.id.waiterName);
        mainIcon.setVisibility(View.GONE);
        backIcon.setVisibility(View.VISIBLE);

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddWaiters.this,WaiterActivity.class));
            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddWaiters.this,WaiterActivity.class));
            }
        });
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(waiterName.getText())){
                    Toast.makeText(AddWaiters.this,"Waiter name is required!",Toast.LENGTH_LONG).show();
                }else if(TextUtils.isEmpty(phoneBox.getText())){
                    Toast.makeText(AddWaiters.this,"Waiter's phone no. is required",Toast.LENGTH_LONG).show();
                }else {
                    addWaiter();
                }
            }
        });

    }

    private void addWaiter() {
        int max= 999999;
        int min =111111;
        int WaiterID = (int)(Math.random()*(max-min+1)+min);
        Map<String,String> items = new HashMap<>();
        items.put("waiterName",waiterName.getText().toString());
        items.put("waiterNo","+91"+phoneBox.getText().toString());
        items.put("waiterID",String.valueOf(WaiterID));

        auth = FirebaseAuth.getInstance();
        dbroot = FirebaseFirestore.getInstance();
        dbroot.collection("restaurants")
                .document(auth.getCurrentUser().getPhoneNumber())
                .collection("waiters")
                .document("+91"+phoneBox.getText().toString())
                .set(items)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddWaiters.this,"Waiter added",Toast.LENGTH_LONG).show();
                        waiterName.setText("");
                        phoneBox.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddWaiters.this,e.toString(),Toast.LENGTH_LONG).show();
                    }
                });
    }
}