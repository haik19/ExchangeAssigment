package com.vahan.exchangeratesassigment.view.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.vahan.exchangeratesassigment.R;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity_layout);
        String orgId = getIntent().getStringExtra(DetailsFragment.ORG_ID_KEY);
        String bankeName = getIntent().getStringExtra(DetailsFragment.BANK_NAME_KEY);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = DetailsFragment.newInstance(orgId, bankeName);
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }
}
