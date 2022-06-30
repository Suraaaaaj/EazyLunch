package com.myteam.eazylunch;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.acl.Owner;
import java.util.HashMap;
import java.util.Map;


public class ResDetails extends AppCompatActivity {

    Button nextbtn;
    TextView resNameTV,tableCountTV,ownerNameTV;
    String resName, ownerName, tableCount;
    FirebaseAuth auth;
    FirebaseFirestore dbroot;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_details);
        getWindow().setStatusBarColor(ContextCompat.getColor(ResDetails.this,R.color.primary));

        nextbtn = (Button)findViewById(R.id.enterToDashboard);
        resNameTV = (TextView)findViewById(R.id.resName);
        tableCountTV = (TextView)findViewById(R.id.tableCount);
        ownerNameTV = (TextView)findViewById(R.id.ownerName);


        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbroot = FirebaseFirestore.getInstance();
                auth = FirebaseAuth.getInstance();
                if(TextUtils.isEmpty(resNameTV.getText())){
                    Toast.makeText(ResDetails.this,"Restaurant name is required!",Toast.LENGTH_LONG).show();
                }else if(TextUtils.isEmpty(ownerNameTV.getText())){
                    Toast.makeText(ResDetails.this,"Owner name is required!",Toast.LENGTH_LONG).show();
                }else if(TextUtils.isEmpty(tableCountTV.getText())){
                    Toast.makeText(ResDetails.this,"Restaurant Name is required!",Toast.LENGTH_LONG).show();
                }else {
                    sendDataToFireStore(resNameTV,ownerNameTV,tableCountTV);
                }
            }
        });
    }

    private void sendDataToFireStore(TextView resName, TextView ownerName, TextView tableCount) {
        Map<String,String> items = new HashMap<>();
        items.put("Restaurant name",resName.getText().toString());
        items.put("Owner name",ownerName.getText().toString());
        items.put("Table count",tableCount.getText().toString());
        items.put("phone",auth.getCurrentUser().getPhoneNumber());

        dbroot.collection("restaurants")
                .document(auth.getCurrentUser().getPhoneNumber())
                .collection("ResDetails")
                .document(ownerName.getText().toString()+"ResDetails")
                .set(items)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ResDetails.this,"Data added",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ResDetails.this,OrderActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(ResDetails.this,"Something went wrong! Please try again after some time",Toast.LENGTH_LONG).show();
            }
        });

    }


}