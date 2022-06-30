package com.myteam.eazylunch;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.myteam.eazylunch.databinding.ActivityOtpBinding;
import com.myteam.eazylunch.databinding.ActivityWaiterBinding;

import java.util.ArrayList;
import static android.content.ContentValues.TAG;

public class WaiterActivity extends AppCompatActivity {
    ActivityWaiterBinding binding;
    Dialog myDialog;
    FirebaseAuth auth;
    TextView addWaiters;
    ProgressDialog dialog;
    FirebaseFirestore dbroot;

    TextView addWaiter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWaiterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        dbroot = FirebaseFirestore.getInstance();
        getWindow().setStatusBarColor(ContextCompat.getColor(WaiterActivity.this,R.color.primary));
        myDialog = new Dialog(this);
        dialog = new ProgressDialog(WaiterActivity.this);
        addWaiters = (TextView)findViewById(R.id.addWaiters);
        addWaiter = findViewById(R.id.addWaiter);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        //----------AppBar-------------//

        ImageView mainIcon = findViewById(R.id.mainIcon);
        ImageView backIcon = findViewById(R.id.backIcon);
        mainIcon.setVisibility(View.VISIBLE);
        backIcon.setVisibility(View.GONE);

        // --------Start ProgressDialog-------//

        dialog.setCancelable(false);
        dialog.setMessage("Getting waiter details");
        dialog.show();

        //--------- Get User Data ---------//
        LinearLayout ifNoWaiters;
        LinearLayout ifWaiters;
        ifNoWaiters = findViewById(R.id.ifNoWaiters);
        ifWaiters = findViewById(R.id.ifWaiters);
        ArrayList<WaiterClass> waitersArray = new ArrayList<>();
        WaiterAdapter adapter = new WaiterAdapter(this,waitersArray);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        dbroot.collection("restaurants")
                .document(auth.getCurrentUser().getPhoneNumber())
                .collection("waiters")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                WaiterClass waiterClass = documentSnapshot.toObject(WaiterClass.class);
                                waitersArray.add(waiterClass);
                                recyclerView.setLayoutManager(linearLayoutManager);
                                recyclerView.setAdapter(adapter);

                                //If Waiter Count == 0
                                if(waitersArray.isEmpty()){
                                    ifWaiters.setVisibility(View.GONE);
                                    ifNoWaiters.setVisibility(View.VISIBLE);
                                }
                                //If Waiter Count !=0
                                else{
                                    ifWaiters.setVisibility(View.VISIBLE);
                                    ifNoWaiters.setVisibility(View.GONE);
                                }

                            }
                            dialog.dismiss();
                        }else{
                            ifWaiters.setVisibility(View.GONE);
                            ifNoWaiters.setVisibility(View.VISIBLE);
                            dialog.dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                ifWaiters.setVisibility(View.GONE);
                ifNoWaiters.setVisibility(View.VISIBLE);
                dialog.dismiss();
            }
        });



        //--------Dialog Close----------//

        // Logged In User Details Dialog
        mainIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);

            }
        });

        // AddWaiterActivity
        addWaiters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WaiterActivity.this,AddWaiters.class));
            }
        });

        //AddWaiterActivity
        addWaiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WaiterActivity.this,AddWaiters.class));
            }
        });


        //---------------Bottom Navigation Bar Starts ----------------//

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.waiters);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.orders:
                        startActivity(new Intent(getApplicationContext(), OrderActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.foodmenu:
                        startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.waiters:
                        return false;
                }
                return true;
            }
        });

        //---------------Bottom Navigation Bar Ends ------------------//
    }

    //---------------PopUp ------------------------------------//
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
                startActivity(new Intent(WaiterActivity.this,SignIn.class));
                finish();
            }
        });
        myDialog.show();
    }

}