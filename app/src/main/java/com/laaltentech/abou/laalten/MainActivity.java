package com.laaltentech.abou.laalten;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import static android.content.ContentValues.TAG;
import static com.laaltentech.abou.laalten.BluetoothService.MY_PREFS_NAME;

public class MainActivity extends AppCompatActivity {

    SharedPreferences pref;

    private TextView mTextMessage;
    public static final String BROADCAST_ACTION = "broadcast";

    //
     final Fragment fragment1 = new FragmentHome();
     final Fragment fragment2 = new FragmentTheme();
     final Fragment fragment3 = new FragmentAbout();
     FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;

    int theme = 0;
    int music = 0;

    //

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = new FragmentHome();
                    break;
                case R.id.navigation_dashboard:
                    selectedFragment = new FragmentTheme();
                    break;
                case R.id.navigation_notifications:
                    selectedFragment = new FragmentAbout();
                    break;
            }


            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,selectedFragment).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fm.beginTransaction().add(R.id.fragmentContainer, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.fragmentContainer, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.fragmentContainer,fragment1, "1").commit();

        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

//        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new FragmentHome()).commit();
        startService(new Intent(MainActivity.this, BluetoothService.class));
    }

}
