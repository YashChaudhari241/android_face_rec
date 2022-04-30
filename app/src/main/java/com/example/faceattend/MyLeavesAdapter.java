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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MyLeavesAdapter extends RecyclerView.Adapter<MyLeavesAdapter.MyviewHolder> {
    private List<LeaveModel> leaveList;
    private Context context;
    private String monthArray[] = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
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
        String leavedate2=leaveList.get(position).getEndDate();
        leavedate=leavedate.substring(0,10);
        leavedate2 = leavedate2.substring(0,10);
        if(leaveList.get(position).getApproved()==0){

            holder.status.setText("Not Approved");
        }
        else{
            holder.status.setTextColor(0xFF5879EC);
            holder.status.setText("Approved");
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null,date2 =null;
        try {
            date = formatter.parse(leavedate);
            date2 = formatter.parse(leavedate2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.startDate.setText(date.getDate()+" "+monthArray[date.getMonth()]+" - " +date2.getDate()+" "+monthArray[date2.getMonth()]);
        Date finalDate = date;
        Date finalDate1 = date2;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context,"hey",Toast.LENGTH_LONG).show();
                Intent i=new Intent(context,LeaveDetails.class);
                i.putExtra("startDate", finalDate.getDate()+" "+monthArray[finalDate.getMonth()]);
                i.putExtra("endDate", finalDate1.getDate()+" "+monthArray[finalDate1.getMonth()]);
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
