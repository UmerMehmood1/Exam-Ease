package com.picsart.studio;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.picsart.studio.DBHelper.DummyDataGenerator;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
}