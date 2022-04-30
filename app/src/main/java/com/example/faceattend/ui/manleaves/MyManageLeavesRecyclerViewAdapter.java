package com.example.faceattend.ui.manleaves;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.faceattend.LeaveDetails;
import com.example.faceattend.models.LeaveModel;
import com.example.faceattend.ui.manleaves.placeholder.PlaceholderContent.PlaceholderItem;
import com.example.faceattend.databinding.FragmentLeaveBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyManageLeavesRecyclerViewAdapter extends RecyclerView.Adapter<MyManageLeavesRecyclerViewAdapter.ViewHolder> {

    private final List<LeaveModel> mValues;
    private Context context;
    private String idToken;
    private boolean approved;
    public MyManageLeavesRecyclerViewAdapter(Context applicationContext,List<LeaveModel> items,String idToken,boolean approved) {
        mValues = items;
        this.idToken = idToken;
        this.approved = approved;
        this.context=applicationContext;
    }
    private String monthArray[] = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentLeaveBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null,date2 =null;
        try {
            date = formatter.parse(holder.mItem.getStartDate());
            date2 = formatter.parse(holder.mItem.getEndDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.mIdView.setText(date.getDate()+" "+monthArray[date.getMonth()]+" - " +date2.getDate()+" "+monthArray[date2.getMonth()]);
        holder.mContentView.setText(mValues.get(position).getLeaveBy());
        Date finalDate = date;
        Date finalDate1 = date2;
        holder.mItemHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, LeaveDetails.class);
                i.putExtra("startDate",finalDate.getDate()+" "+monthArray[finalDate.getMonth()]);
                i.putExtra("endDate", finalDate1.getDate()+" "+monthArray[finalDate1.getMonth()]);
                i.putExtra("msg",mValues.get(holder.getAdapterPosition()).getMsg());
                i.putExtra("status",mValues.get(holder.getAdapterPosition()).getApproved());
                if(mValues.get(holder.getAdapterPosition()).getApproved()!=0) {
                    i.putExtra("approvalTime", mValues.get(holder.getAdapterPosition()).getApprovalTime().toString());
                }
                i.putExtra("orgOwner",mValues.get(holder.getAdapterPosition()).getLeaveBy());
                i.putExtra("approve",!approved);
                i.putExtra("showBy",true);
                i.putExtra("idToken",idToken);
                i.putExtra("pubID",mValues.get(holder.getAdapterPosition()).getPubID());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public LinearLayout mItemHolder;
        public LeaveModel mItem;

        public ViewHolder(FragmentLeaveBinding binding) {
            super(binding.getRoot());
            mIdView = binding.startDate;
            mContentView = binding.status;
            mItemHolder = binding.itemHolder;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}