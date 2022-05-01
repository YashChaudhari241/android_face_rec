package com.example.faceattend.ui.home;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Environment;
import android.text.format.DateFormat;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.example.faceattend.Analysis;
import com.example.faceattend.AppDatabase;
import com.example.faceattend.CreateOrg;
import com.example.faceattend.GETApi;
import com.example.faceattend.AttendanceHistory;
import com.example.faceattend.JoinOrgActivity;
import com.example.faceattend.ManLeavesActivity;
import com.example.faceattend.OrgDetailsActivity;
import com.example.faceattend.ViewEmp;
import com.example.faceattend.models.MarkAttendModel;
import com.example.faceattend.MyLeaves;
import com.example.faceattend.R;
import com.example.faceattend.RequestLeave;
import com.example.faceattend.SelectOfficeLocation;
import com.example.faceattend.ServiceGenerator;
import com.example.faceattend.databinding.FragmentAdminHomeBinding;
import com.example.faceattend.databinding.FragmentHomeBinding;
import com.example.faceattend.models.OrgDetails;
import com.example.faceattend.models.OwnedOrgsDao;
import com.example.faceattend.models.UserDao;
import com.example.faceattend.models.UserObject;
import com.example.faceattend.ui.manleaves.ManageLeavesFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;

public class HomeFragment extends Fragment {

    private static final int PRIORITY_HIGH_ACCURACY = 100;
    private HomeViewModel homeViewModel;
    private int priv;
    AppDatabase db;
    private FragmentHomeBinding binding;
    private FragmentAdminHomeBinding bindingAdm;
    final int CAMERA_PIC_REQUEST=1337;
    TextView openCam;
    private FusedLocationProviderClient fusedLocationClient;
    private String selectedUniqueStr = null;
    boolean ownsOrg = false;
    // Allows class to cancel the location request if it exits the activity.
    // Typically, you use one cancellation source per lifecycle.
    private final CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();
    String idToken, hasOrg;
    TextView dayInt,month,empName,empEmail;

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    String day,monthString;
    String locx, locy;
    private final int REQUEST_LOCATION_PERMISSION = 1;
    private View root;
    public void setHasOrg(String hasOrg){
        this.hasOrg = hasOrg;
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        //View v = inflater.inflate(R.layout.fragment_home, container, false);

        Intent i = requireActivity().getIntent();
        if (i.getIntExtra("priv",1) == 0){
            priv = 0;
            binding = FragmentHomeBinding.inflate(inflater, container, false);
            root = binding.getRoot();
        }
        else{
            priv = 1;
            bindingAdm = FragmentAdminHomeBinding.inflate(inflater, container, false);
            root = bindingAdm.getRoot();
        }
        empEmail=root.findViewById(R.id.empName);
        empEmail=root.findViewById(R.id.empEmail);
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        requestCurrentLocation();

        db = Room.databaseBuilder(getActivity().getApplicationContext(),
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
//        NavigationView navigationView = binding.navView;
//        View hView = navigationView.getHeaderView(0);
//        TextView nav_user = (TextView) hView.findViewById(R.id.empName);
//        nav_user.setText(mUser.getDisplayName());
//        empName.setText(mUser.getDisplayName());
//        empEmail.setText(mUser.getEmail());
        //hasOrg = i.getStringExtra("orgName");
        UserDao userDao = db.userDao();
        List<UserObject> users = userDao.getAll();

        if(!users.isEmpty())
            hasOrg = users.get(0).orgName;
        if (priv == 0) {
            setListeners();
        }
        else{
            String stringarr[];
            String uniqarr[];
            OwnedOrgsDao odao = db.ownedOrgsDao();
            List<OrgDetails> savedOrgs = odao.getAll();
            if( savedOrgs.isEmpty()){
                ownsOrg = false;
                stringarr=new String[1];
                stringarr[0] = "No Org";
                uniqarr=new String[1];
                uniqarr[0] = null;
            }
            else {
                stringarr = new String[savedOrgs.size()];
                uniqarr = new String[savedOrgs.size()];
                int index = 0;
                for (OrgDetails s : savedOrgs) {
                    if (s.isSelected()) {
                        selectedUniqueStr = s.getUniqueString();
                    }
                    stringarr[index] = s.getOrgName();
                    uniqarr[index] = s.getUniqueString();
                    index++;
                }
                if (selectedUniqueStr == null && !savedOrgs.isEmpty()) {
                    selectedUniqueStr = savedOrgs.get(0).getUniqueString();
                    odao.selectOrg(true, selectedUniqueStr);
                }
                ownsOrg = true;
            }

            Spinner spinner = (Spinner)root.findViewById(R.id.spinner);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item,stringarr);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(ownsOrg){
                            selectedUniqueStr = uniqarr[i];
                            Log.v("set","set as"+selectedUniqueStr);
//                            setAdmListeners();
                        }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            setAdmListeners();
        }
//        final TextView textView = binding.textHome;
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
        setTime();

        return root;
    }
    public void setTime()
    {
        dayInt=root.findViewById(R.id.apr);
        month=root.findViewById(R.id.apr2);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        Date date1 = cal.getTime();
        String todaysdate = format.format(date1);
        Date date = null;
        try {
            date = format.parse(todaysdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        day= (String) DateFormat.format("dd",date);
        monthString = (String) DateFormat.format("MMM",date);
        dayInt.setText(day);
        month.setText(monthString.toUpperCase(Locale.ROOT));
        Log.d("Date1", day);
        Log.d("Date1", monthString);

    }


    @Override
    public void onResume() {
        super.onResume();
        UserDao userDao = db.userDao();
        List<UserObject> users = userDao.getAll();
        if(!users.isEmpty())
            hasOrg = users.get(0).orgName;
        if(priv==0)
            setListeners();
        else
            setAdmListeners();
    }

    private void openFallbackAct(Class a){

        if(hasOrg == null && priv ==0){
            a = JoinOrgActivity.class;
            Intent i=new Intent(getActivity(),a);
            i.putExtra("idToken",idToken);
            startActivity(i);
        }
        else if(priv ==1){
            if(!ownsOrg){
                a = CreateOrg.class;
            }
            Intent i=new Intent(getActivity(),a);
            i.putExtra("idToken",idToken);
            i.putExtra("uniqueStr",selectedUniqueStr);
            startActivity(i);
        }
        else if(priv==0){
            Intent i=new Intent(getActivity(),a);
            i.putExtra("idToken",idToken);
            i.putExtra("uniqueStr",selectedUniqueStr);
            startActivity(i);

        }

    }
    public void setAdmListeners(){
        LinearLayout create_org = root.findViewById(R.id.createorgrect);
        create_org.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                openFallbackAct(CreateOrg.class);
            }
        });
        LinearLayout get_emp = root.findViewById(R.id.viewemprect);
        get_emp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                openFallbackAct(ViewEmp.class);
            }
        });
        LinearLayout org_det = root.findViewById(R.id.orgdetailsrect);
        org_det.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                openFallbackAct(OrgDetailsActivity.class);
            }
        });
        LinearLayout man_leaves = root.findViewById(R.id.manageLeavesRect);
        man_leaves.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                openFallbackAct(ManLeavesActivity.class);
            }
        });
    }
    public void setListeners(){

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
//        LinearLayout open_map = (LinearLayout) root.findViewById(R.id.MyLeavesRect2);
        LinearLayout open_joinorg = (LinearLayout) root.findViewById(R.id.requestLeaveRect2);
        open_joinorg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i_joinorg=new Intent(getActivity(), JoinOrgActivity.class);
                startActivity(i_joinorg);
            }
        });
//        open_map.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//                openFallbackAct(SelectOfficeLocation.class);
//            }
//        });
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

        // Create org test


        Button btn = (Button) root.findViewById(R.id.button_map);

        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent i_map=new Intent(getActivity(), SelectOfficeLocation.class);
                startActivity(i_map);
            }
        });
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
        Call<MarkAttendModel> call = service.markAttendance("Bearer "+idToken,body,locx,locy,true);
        Log.d("call", String.valueOf(call));
        call.enqueue(new Callback<MarkAttendModel>() {
            @Override
            public void onResponse(Call<MarkAttendModel> call,
                                   retrofit2.Response<MarkAttendModel> response) {
                //Log.v("Upload", response.body().getDist());
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
        bindingAdm = null;
    }


    private void requestCurrentLocation() {
        Log.d("x", "requestCurrentLocation()");
        // Request permission
        if (ActivityCompat.checkSelfPermission(
                getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {

            // Main code
            Task<Location> currentLocationTask = fusedLocationClient.getCurrentLocation(
                    PRIORITY_HIGH_ACCURACY,
                    cancellationTokenSource.getToken()
            );

            currentLocationTask.addOnCompleteListener((new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {

                    String result = "";

                    if (task.isSuccessful()) {
                        // Task completed successfully
                        Location location = task.getResult();
                        result = "Location (success): " +
                                location.getLatitude() +
                                ", " +
                                location.getLongitude();
                        locx=String.valueOf(location.getLatitude());
                        locy=String.valueOf(location.getLongitude());
                    } else {
                        // Task failed with an exception
                        Exception exception = task.getException();
                        result = "Exception thrown: " + exception;
                    }

                    Log.d("x", "getCurrentLocation() result: " + result);
                }
            }));
        } else {
            // TODO: Request fine location permission
            Log.d("x", "Request fine location permission.");
        }
    }
}