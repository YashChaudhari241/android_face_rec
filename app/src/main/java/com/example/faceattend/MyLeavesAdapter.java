package com.example.faceattend;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faceattend.models.GetLeavesModel;
import com.example.faceattend.models.LeaveModel;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyLeavesAdapter extends RecyclerView.Adapter<MyLeavesAdapter.MyviewHolder> {
    private List<LeaveModel> leaveList;
    private Context context;

    public MyLeavesAdapter() {
    }

    public MyLeavesAdapter(Context applicationContext, List<LeaveModel> leaveList) {
        this.context=applicationContext;
        this.leaveList=leaveList;
    }
    public void setLeaveList(List<LeaveModel> leaveList){
        this.leaveList=leaveList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyLeavesAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.listlayout,parent,false);
        MyviewHolder viewHolder= new MyviewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyLeavesAdapter.MyviewHolder holder, int position) {
        String leavedate=leaveList.get(position).getStartDate();
        leavedate=leavedate.substring(0,10);
        if(leaveList.get(position).getApproved()==0){
            holder.status.setText("Not Approved");
        }
        else{
            holder.status.setText("Approved");
        }
        holder.startDate.setText("Leave at "+leavedate);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context,"hey",Toast.LENGTH_LONG).show();
                Intent i=new Intent(context,LeaveDetails.class);
                i.putExtra("startDate",leaveList.get(holder.getAdapterPosition()).getStartDate());
                i.putExtra("endDate",leaveList.get(holder.getAdapterPosition()).getEndDate());
                i.putExtra("msg",leaveList.get(holder.getAdapterPosition()).getMsg());
                i.putExtra("status",leaveList.get(holder.getAdapterPosition()).getApproved());
                if(leaveList.get(holder.getAdapterPosition()).getApproved()!=0) {
                    i.putExtra("approvalTime", leaveList.get(holder.getAdapterPosition()).getApprovalTime().toString());
                }
                i.putExtra("orgOwner",leaveList.get(holder.getAdapterPosition()).getOrgOwner());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return leaveList.size();
//        if (leaveList != null)
//            return leaveList.size();
//        else
//            return 0;

    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView startDate;
        TextView status;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            startDate= itemView.findViewById(R.id.startDate);
            status=itemView.findViewById(R.id.status);

        }
    }
}
