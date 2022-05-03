package com.example.faceattend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.faceattend.models.AttendanceDao;
import com.example.faceattend.models.OwnedOrgsDao;
import com.example.faceattend.models.UserDao;
import com.example.faceattend.ui.home.HomeFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.faceattend.databinding.ActivityDashboardBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityDashboardBinding binding;
    String idToken;
    static DashboardActivity INSTANCE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarDashboard.toolbar);
        /*binding.appBarDashboard.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "faceattend-database").allowMainThreadQueries().fallbackToDestructiveMigration()
                .build();
        AttendanceDao adao = db.attendanceDao();
        OwnedOrgsDao odao = db.ownedOrgsDao();
        UserDao userDao = db.userDao();
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
       TextView empName = (TextView) hView.findViewById(R.id.empName);
       TextView empEmail=(TextView) hView.findViewById(R.id.empEmail);
       empName.setText(mUser.getDisplayName());
       empEmail.setText(mUser.getEmail());

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        Intent i = getIntent();
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_settings, R.id.nav_profile, R.id.logout)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_dashboard);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                //it's possible to do more actions on several items, if there is a large amount of items I prefer switch(){case} instead of if()
                if (id == R.id.logout) {
                    FirebaseAuth.getInstance().signOut();
                    userDao.deleteAll();
                    adao.deleteAll();
                    odao.deleteAll();
                    startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                    finish();
                }
                //This is for maintaining the behavior of the Navigation view
                NavigationUI.onNavDestinationSelected(item, navController);
                //This is for closing the drawer after acting on it
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }

        });
    }

    public static DashboardActivity getActivityInstance() {
        return INSTANCE;
    }

    public String getIdToken() {
        return this.idToken;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_dashboard);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}