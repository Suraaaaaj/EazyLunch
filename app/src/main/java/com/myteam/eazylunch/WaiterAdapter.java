package com.myteam.eazylunch;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class WaiterAdapter extends RecyclerView.Adapter<WaiterAdapter.WaiterViewHolder>{
    Context context;
    ArrayList<WaiterClass> waiters;
    public WaiterAdapter(Context context, ArrayList<WaiterClass> waiters){
        this.context = context;
        this.waiters = waiters;
    }

    @NonNull
    @Override
    public WaiterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.waiter_row,parent,false);
        return new WaiterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WaiterViewHolder holder, int position) {
        WaiterClass waiterClass = waiters.get(position);
        holder.waiterName.setText(waiterClass.getWaiterName());
        holder.waiterPhone.setText(waiterClass.getWaiterNo());
        holder.waiterID.setText(waiterClass.getWaiterID());
    }

    @Override
    public int getItemCount() {
        return waiters.size();
    }

    public class WaiterViewHolder extends RecyclerView.ViewHolder{

        TextView waiterPhone,waiterName,waiterID;
        Button deleteBtn;
        FirebaseFirestore dbroot;
        FirebaseAuth auth;

        public WaiterViewHolder(@NonNull View itemView) {
            super(itemView);
            waiterName = itemView.findViewById(R.id.waiterName);
            waiterPhone= itemView.findViewById(R.id.waiterPhone);
            waiterID= itemView.findViewById(R.id.waiterID);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            dbroot = FirebaseFirestore.getInstance();
            auth = FirebaseAuth.getInstance();

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  dbroot.collection("restaurants")
                          .document(auth.getCurrentUser().getPhoneNumber())
                          .collection("waiters")
                          .document(waiterPhone.getText().toString())
                          .delete()
                          .addOnSuccessListener(new OnSuccessListener<Void>() {
                              @Override
                              public void onSuccess(Void aVoid) {
                                  Toast.makeText(context,"Waiter deleted",Toast.LENGTH_LONG).show();
                                  context.startActivity(new Intent(context.getApplicationContext(),WaiterActivity.class));
                              }
                          })
                          .addOnFailureListener(new OnFailureListener() {
                              @Override
                              public void onFailure(@NonNull Exception e) {
                                  Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show();
                              }
                          });
                }
            });
        }
    }

}

