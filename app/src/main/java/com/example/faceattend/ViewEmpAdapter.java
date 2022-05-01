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
import com.example.faceattend.models.UserDetailsModel;
import com.example.faceattend.ui.my_profile.MyProfileFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ViewEmpAdapter extends RecyclerView.Adapter<ViewEmpAdapter.MyviewHolder>{
    private List<UserDetailsModel> empList;
    private Context context;
    private String idToken;
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
    public void setToken(String idToken){
        this.idToken=idToken;
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
        String pubID=empList.get(position).getPubID();
        holder.empName.setText(empName);
        if(empList.get(position).getPriv()==1){
            holder.post.setText("Manager");
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,ManEmployee.class);
                i.putExtra("idToken",idToken);
                i.putExtra("pubID",pubID);
                Fragment myFragment = new MyProfileFragment();
                AppCompatActivity activity = (AppCompatActivity) context;
                //activity.getSupportFragmentManager().beginTransaction().replace(R.id.recyclerView, myFragment).addToBackStack(null).commit();
                //activity.getSupportFragmentManager().beginTransaction().replace(myFragment.getId(),myFragment).addToBackStack(null).commit();
//                i.putExtra("openMyProfile",true);
//                overridePendingTransition(0, 0);
//                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(i);
            }
        });

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
        TextView post;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            empName= itemView.findViewById(R.id.empName);
            post=itemView.findViewById(R.id.post);

        }
    }
}
