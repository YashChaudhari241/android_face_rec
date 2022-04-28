package com.example.faceattend.ui.home;
import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Environment;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.example.faceattend.Analysis;
import com.example.faceattend.AppDatabase;
import com.example.faceattend.GETApi;
import com.example.faceattend.AttendanceHistory;
import com.example.faceattend.JoinOrgActivity;
import com.example.faceattend.models.MarkAttendModel;
import com.example.faceattend.MyLeaves;
import com.example.faceattend.R;
import com.example.faceattend.RequestLeave;
import com.example.faceattend.SelectOfficeLocation;
import com.example.faceattend.ServiceGenerator;
import com.example.faceattend.databinding.FragmentHomeBinding;
import com.example.faceattend.models.UserDao;
import com.example.faceattend.models.UserObject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.example.faceattend.models.UserDao;
import com.example.faceattend.models.UserObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;

public class HomeFragment extends Fragment implements LocationListener {

    private HomeViewModel homeViewModel;

    private FragmentHomeBinding binding;
    final int CAMERA_PIC_REQUEST=1337;
    TextView openCam;
    String idToken,hasOrg;

    public void setHasOrg(String hasOrg){
        this.hasOrg = hasOrg;
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //View v = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        AppDatabase db = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "faceattend-database").allowMainThreadQueries().fallbackToDestructiveMigration()
                .build();
        //Couldn't find any other method to get token from other activities
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            idToken = task.getResult().getToken();
                            // Send token to your backend via HTTPS
                            // ...
                        } else {
                            // Handle error -> task.getException();
                        }
                    }
                });
        Intent i = requireActivity().getIntent();
        //hasOrg = i.getStringExtra("orgName");
        UserDao userDao = db.userDao();
        List<UserObject> users = userDao.getAll();
        if(!users.isEmpty())
            hasOrg = users.get(0).orgName;


        final TextView textView = binding.textHome;
        openCam=root.findViewById(R.id.BSelectImage2);

        openCam.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Button","clicked!");
                if(hasOrg!=null) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                }
                else{
                    startActivity(new Intent(getActivity(), JoinOrgActivity.class));
                }
            }
        });

        //Linking History, Analysis , Request Leaves, My Leaves tiles.

        LinearLayout open_history = (LinearLayout) root.findViewById(R.id.historyRect);

        open_history.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(hasOrg!=null) {
                    Intent i_hist = new Intent(getActivity(), AttendanceHistory.class);
                    i_hist.putExtra("idToken", idToken);
                    startActivity(i_hist);
                }
                else{
                    startActivity(new Intent(getActivity(), JoinOrgActivity.class));
                }

            }
        });

        LinearLayout open_analysis = (LinearLayout) root.findViewById(R.id.analysisRect);

        open_analysis.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                openFallbackAct(Analysis.class);
            }
        });

        LinearLayout open_request = (LinearLayout) root.findViewById(R.id.requestLeaveRect);
        LinearLayout open_map = (LinearLayout) root.findViewById(R.id.MyLeavesRect2);
        LinearLayout open_joinorg = (LinearLayout) root.findViewById(R.id.requestLeaveRect2);
        open_joinorg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i_joinorg=new Intent(getActivity(), JoinOrgActivity.class);
                startActivity(i_joinorg);
            }
        });
        open_map.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                openFallbackAct(SelectOfficeLocation.class);
            }
        });
        open_request.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                openFallbackAct(RequestLeave.class);
            }
        });

        LinearLayout open_myLeaves = (LinearLayout) root.findViewById(R.id.MyLeavesRect);

        open_myLeaves.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                openFallbackAct(MyLeaves.class);
            }
        });

        // Linking complete

        // Map testing

        Button btn = (Button) root.findViewById(R.id.button_map);

        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent i_map=new Intent(getActivity(), SelectOfficeLocation.class);
                startActivity(i_map);
            }
        });

//        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//            if (requestCode == CAMERA_PIC_REQUEST) {
//                Bitmap image = (Bitmap) data.getExtras().get("data");
//                //ImageView imageview = (ImageView) findViewById(R.id.ImageView01); //sets imageview as the bitmap
//                //imageview.setImageBitmap(image);
//            }
//        }

//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }
    private void openFallbackAct(Class a){
        if(hasOrg == null){
            startActivity(new Intent(getActivity(),JoinOrgActivity.class));
        }else{
            if(a==RequestLeave.class){
                Intent i=new Intent(getActivity(),a);
                i.putExtra("idToken",idToken);
                startActivity(i);
            }
            else
                startActivity(new Intent(getActivity(),a));

        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {

                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                // convert byte array to Bitmap

                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
                        byteArray.length);

                File destination = new File(Environment.getExternalStorageDirectory(),"temp.jpg");
                FileOutputStream fo;
                try {
                    fo = new FileOutputStream(destination);
                    fo.write(byteArray);
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                uploadFile(destination);
                //imageView.setImageBitmap(bitmap);

            }
        }
    }
    private void uploadFile(File file) {
        // create upload service client
        GETApi service =
                ServiceGenerator.createService(GETApi.class);
        //File f = new File(this.getCacheDir(), file);



        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        //File file = File(RealPathUtils.getRealPathFromURI_API19(MainActivity.this, fileUri))
        //File file1 = new File(getRealPathFromURI(fileUri));
        //File file1 = FileUtils;
        //showFileChooser();
        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("pic", "jb", requestFile);
//        RequestBody locx=RequestBody.create(MediaType.parse("part/form-data"),"51");
//        RequestBody locy=RequestBody.create(MediaType.parse("part/form-data"),"51");
//        RequestBody entryExit=RequestBody.create(MediaType.parse("multipart/form-data"),"true");




        // add another part within the multipart request
        //String descriptionString = "hello, this is description speaking";
        //RequestBody description =
        //RequestBody.create(
        //okhttp3.MultipartBody.FORM, descriptionString);
        //Call<ResponseBody> call = service.saveFace("matt","xyz1", body);
        Call<MarkAttendModel> call = service.markAttendance("Bearer "+idToken,body,true);
        Log.d("call", String.valueOf(call));
        call.enqueue(new Callback<MarkAttendModel>() {
            @Override
            public void onResponse(Call<MarkAttendModel> call,
                                   retrofit2.Response<MarkAttendModel> response) {
                Log.v("Upload", response.body().getDist());
                Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_LONG).show();

//                try {
//                    //JSONObject jsonObject = new JSONObject(response.toString());
//                    Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }


            }

            @Override
            public void onFailure(Call<MarkAttendModel> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }
    private String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    public void OpenHistory()
    {
        System.out.println("History opened!!!!");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @Override
    public void onLocationChanged(Location location) {
//        txtLat = (TextView) findViewById(R.id.textview1);
//        txtLat.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {

    }

    @Override
    public void onFlushComplete(int requestCode) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

}