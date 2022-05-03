package com.example.faceattend;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faceattend.models.LeaveModel;
import com.example.faceattend.models.PresentModel;
import com.example.faceattend.models.UserDetailsModel;
import com.example.faceattend.ui.my_profile.MyProfileFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MyviewHolder>{
    private List<PresentModel> presentList;
    private Context context;
    private String idToken;
    private String monthArray[] = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};


    public ReportAdapter(Context applicationContext, List<PresentModel> presentList) {
        this.context=applicationContext;
        this.presentList=presentList;
    }
    public void setEmpList(List<PresentModel> leaveList){
        this.presentList=leaveList;
        notifyDataSetChanged();
    }
    public void setToken(String idToken){
        this.idToken=idToken;
    }
    @NonNull
    @Override
    public ReportAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.presentitemlayout,parent,false);
        MyviewHolder viewHolder= new MyviewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReportAdapter.MyviewHolder holder, int position) {

            String empName = "" + presentList.get(position).getName();

            String pubID = presentList.get(position).getPubID();

            holder.empName.setText(empName);

            if (presentList.get(position).isPresent()) {
                holder.present.setText("Present");
                String arrivalTime = presentList.get(position).getStartTime().toString();
                holder.arrivedAt.setText("Arrived At: " + arrivalTime.substring(0,8));
            } else {
                holder.present.setText("Not Present");
                holder.arrivedAt.setVisibility(View.GONE);
            }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,ManEmployee.class);
                i.putExtra("idToken",idToken);
                //i.putExtra("empName",empName);
                //i.putExtra("pubID",pubID);
                Fragment myFragment = new MyProfileFragment();
                AppCompatActivity activity = (AppCompatActivity) context;
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        //return presentList.size();
        if (presentList != null)
            return presentList.size();
        else
            return 0;

    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView empName;
        TextView present;
        TextView arrivedAt;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            empName= itemView.findViewById(R.id.empName);
            present=itemView.findViewById(R.id.present);
            arrivedAt=itemView.findViewById(R.id.arrived_at);

        }
    }
}

