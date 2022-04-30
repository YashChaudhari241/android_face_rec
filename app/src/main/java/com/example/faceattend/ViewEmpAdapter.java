package com.example.faceattend;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faceattend.models.LeaveModel;
import com.example.faceattend.models.UserDetailsModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ViewEmpAdapter extends RecyclerView.Adapter<ViewEmpAdapter.MyviewHolder>{
    private List<UserDetailsModel> empList;
    private Context context;
    private String monthArray[] = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    public ViewEmpAdapter() {
    }

    public ViewEmpAdapter(Context applicationContext, List<UserDetailsModel> leaveList) {
        this.context=applicationContext;
        this.empList=leaveList;
    }
    public void setEmpList(List<UserDetailsModel> leaveList){
        this.empList=leaveList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewEmpAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.empitemlayout,parent,false);
        MyviewHolder viewHolder= new MyviewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewEmpAdapter.MyviewHolder holder, int position) {
        String empName=""+empList.get(position).getEmpName();
        holder.empName.setText(empName);

    }

    @Override
    public int getItemCount() {
        return empList.size();
//        if (leaveList != null)
//            return leaveList.size();
//        else
//            return 0;

    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView empName;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            empName= itemView.findViewById(R.id.empName);

        }
    }
}
