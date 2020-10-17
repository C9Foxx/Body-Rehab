package com.example.bodyrehab.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.bodyrehab.R;
import com.example.bodyrehab.fragment.HomeFragment;
import com.example.bodyrehab.fragment.PlaylistFragment;
import com.example.bodyrehab.fragment.ProfileFragment;
import com.example.bodyrehab.fragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AppActivity extends AppCompatActivity {

    private HomeFragment homeFragment = new HomeFragment();
    private PlaylistFragment playlistFragment = new PlaylistFragment();
    private SearchFragment searchFragment = new SearchFragment();
    private ProfileFragment profileFragment = new ProfileFragment();

    private BottomNavigationView menu;

    private static final String TAG = "Body";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        menu = findViewById(R.id.bottomNavigationMenu);
        setFragment(searchFragment);
        menu.setSelectedItemId(R.id.menu_home);

        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.isChecked()){
                    return true;
                }
                else{
                    switch (item.getItemId()){
                        case R.id.menu_playlist:
                            Toast.makeText(getApplicationContext(),"Playlist", Toast.LENGTH_SHORT);
                            Log.w(TAG, "Playlist");
                            setFragment(playlistFragment);
                            return true;
                        case R.id.menu_search:
                            Toast.makeText(getApplicationContext(),"Search", Toast.LENGTH_SHORT);
                            Log.w(TAG, "Search");
                            setFragment(searchFragment);
                            return true;
                        case R.id.menu_profile:
                            Toast.makeText(getApplicationContext(),"Profile", Toast.LENGTH_SHORT);
                            Log.w(TAG, "Profile");
                            setFragment(profileFragment);
                            return true;
                        default:
                            Toast.makeText(getApplicationContext(),"Home", Toast.LENGTH_SHORT);
                            Log.w(TAG, "Home");
                            setFragment(homeFragment);
                            return true;
                    }
                }
            }
        });

    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_frame, fragment).commit();
    }
}