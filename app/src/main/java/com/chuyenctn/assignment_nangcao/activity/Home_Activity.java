package com.chuyenctn.assignment_nangcao.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.chuyenctn.assignment_nangcao.Map_Activity;
import com.chuyenctn.assignment_nangcao.R;

public class Home_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void start_Activity_Map(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 99);
        } else {
            startActivity(new Intent(getApplicationContext(), Map_Activity.class));
        }
    }

    public void start_Activity_News(View view) {
        startActivity(new Intent(getApplicationContext(), News_Activity.class));
    }

    public void start_Activity_Social(View view) {

    }

    public void start_Activity_Course(View view) {
    }
}
