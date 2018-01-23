package com.example.aaronbrecher.booklisting;

import android.support.v7.app.AppCompatActivity;
import android.preference.PreferenceFragment;
import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class BookPreferenceFragment extends PreferenceFragment{

    }
}
