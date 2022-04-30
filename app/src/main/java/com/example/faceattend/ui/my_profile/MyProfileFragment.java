package com.example.faceattend.ui.my_profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.example.faceattend.AppDatabase;
import com.example.faceattend.DashboardActivity;
import com.example.faceattend.GETApi;
import com.example.faceattend.JoinOrgActivity;
import com.example.faceattend.R;
import com.example.faceattend.ServiceGenerator;
import com.example.faceattend.databinding.FragmentSlideshowBinding;
import com.example.faceattend.models.GetOrgModel;
import com.example.faceattend.models.InitUserModel;
import com.example.faceattend.models.UserDao;
import com.example.faceattend.models.UserDetailsModel;
import com.example.faceattend.models.UserObject;
import com.example.faceattend.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MyProfileFragment extends Fragment {

    private MyProfileViewModel slideshowViewModel;
    private FragmentSlideshowBinding binding;
    public boolean hasOrg = false;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(MyProfileViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSlideshow;
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        TextView name = binding.welcomeText4;
        TextView type = binding.textView2;
        TextView emailT = binding.emailText;
        TextView leaveButton = binding.managerButton3;
        TextView deleteButton = binding.cancelButton2;
        AppDatabase db = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "faceattend-database").allowMainThreadQueries().fallbackToDestructiveMigration()
                .build();
        UserDao userDao = db.userDao();
        List<UserObject> users = userDao.getAll();
        if(!users.isEmpty()){
            Log.v("users",users.toString());
            if(users.get(0).orgName!=null){
                hasOrg = true;
                leaveButton.setVisibility(View.VISIBLE);
            }
        }
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mUser.getDisplayName()!=null && mUser.getDisplayName()!="")
            name.setText(mUser.getDisplayName());
        else
            name.setText("Anonymous");
        Intent i = getActivity().getIntent();
        if(i.getIntExtra("priv",0) == 0){
            type.setText("Employee");
        }
        else{
            type.setText("Org Admin");
        }
        TextView phoneT = binding.emailText2;
        phoneT.setText("Not Provided");
        emailT.setText(mUser.getEmail());
        GETApi service =
                ServiceGenerator.createService(GETApi.class);
        ProgressBar progressBar = binding.progressBar3;
        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.GONE);
                leaveButton.setVisibility(View.GONE);
                mUser.getIdToken(false)
                        .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                            public void onComplete(@NonNull Task<GetTokenResult> task) {
                                if (task.isSuccessful()) {
                                    Call<InitUserModel> call = service.leaveOrg("Bearer " + task.getResult().getToken());
                                    call.enqueue(new Callback<InitUserModel>() {
                                        @Override
                                        public void onResponse(Call<InitUserModel> call,
                                                               retrofit2.Response<InitUserModel> response) {
                                            InitUserModel res = response.body();
                                            progressBar.setVisibility(View.GONE);
                                            deleteButton.setVisibility(View.VISIBLE);
                                            if (res.getResult()) {
                                                userDao.updateOrg(null,null);
                                                Toast.makeText(getActivity(), "Left Org Successfully", Toast.LENGTH_SHORT).show();
                                            } else {
                                                leaveButton.setVisibility(View.VISIBLE);
                                                Toast.makeText(getActivity(), res.getError(), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<InitUserModel> call, Throwable t) {
                                            Log.e("Upload error:", t.getMessage());
                                        }
                                    });
                                }
                            }
                        });
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.GONE);
                leaveButton.setVisibility(View.GONE);
                mUser.getIdToken(false)
                        .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                            public void onComplete(@NonNull Task<GetTokenResult> task) {
                                if (task.isSuccessful()) {
                                    Call<InitUserModel> call = service.deleteAccount("Bearer " + task.getResult().getToken());
                                    call.enqueue(new Callback<InitUserModel>() {
                                        @Override
                                        public void onResponse(Call<InitUserModel> call,
                                                               retrofit2.Response<InitUserModel> response) {
                                            InitUserModel res = response.body();
                                            progressBar.setVisibility(View.GONE);
                                            deleteButton.setVisibility(View.VISIBLE);
                                            if (res.getResult()) {
                                                Toast.makeText(getActivity(), "Account Deleted Successfully", Toast.LENGTH_SHORT).show();
                                                userDao.deleteAll();
                                                //TODO DELETE FIREBASE ACCOUNT
                                            } else {
                                                leaveButton.setVisibility(View.VISIBLE);
                                                Toast.makeText(getActivity(), res.getError(), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<InitUserModel> call, Throwable t) {
                                            Log.e("Upload error:", t.getMessage());
                                        }
                                    });
                                }
                            }
                        });
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}