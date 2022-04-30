package com.example.faceattend.ui.manleaves;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.faceattend.GETApi;
import com.example.faceattend.MyLeaves;
import com.example.faceattend.R;
import com.example.faceattend.ServiceGenerator;
import com.example.faceattend.models.GetLeavesModel;
import com.example.faceattend.models.LeaveModel;
import com.example.faceattend.ui.manleaves.placeholder.PlaceholderContent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A fragment representing a list of Items.
 */
public class ManageLeavesFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    public  static LeaveModel [] leavearr;
    public static List<LeaveModel> leaveList;
    public boolean approvedLeaves;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public List<LeaveModel> requestedLeaves;
    public ManageLeavesFragment(boolean approvedLeaves) {
        this.approvedLeaves = approvedLeaves;
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ManageLeavesFragment newInstance(int columnCount) {
        ManageLeavesFragment fragment = new ManageLeavesFragment(false);
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_leave_list, container, false);
            Intent i = getActivity().getIntent();
            String idToken = i.getStringExtra("idToken");
            GETApi service =
                    ServiceGenerator.createService(GETApi.class);
            Call<GetLeavesModel> call= service.getLeavesAsOwner("Bearer "+idToken);
            call.enqueue(new Callback<GetLeavesModel>() {
                @Override
                public void onResponse(Call<GetLeavesModel> call,
                                       retrofit2.Response<GetLeavesModel> response) {
                    //Log.d("MyLeaves Response",response.body().getError());
                    leavearr=response.body().getLeaves();

                    if(leavearr !=null) {
                        requestedLeaves = new ArrayList<>();
                        for(LeaveModel x: leavearr){
                            if(x.getApproved()==1 && approvedLeaves){
                                requestedLeaves.add(x);
                            }
                            else if(x.getApproved()==0&& !approvedLeaves){
                                requestedLeaves.add(x);
                            }
                        }
//                        requestedLeaves = Arrays.asList(leavearr);
                        View view2 = view.findViewById(R.id.list);
                        ProgressBar p = view.findViewById(R.id.progressBar5);
                        p.setVisibility(View.GONE);
                        if (view2 instanceof RecyclerView) {
                            Context context = view2.getContext();
                            RecyclerView recyclerView = (RecyclerView) view2;
                            if (mColumnCount <= 1) {
                                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                            } else {
                                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
                            }
                            recyclerView.setAdapter(new MyManageLeavesRecyclerViewAdapter(getActivity(),requestedLeaves,idToken,approvedLeaves));
                        }
                    }else{

                    }
                    //leave.add(leaveList[0].getStartDate());
                }
                @Override
                public void onFailure(Call<GetLeavesModel> call, Throwable t) {
                    Log.e("Upload error:", t.getMessage());
                }
            });
        // Set the adapter
        return view;
    }
}