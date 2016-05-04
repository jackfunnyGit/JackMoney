package com.asus.jack_tsai.jackmoney;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class SecondActivity extends AppCompatActivity implements BlankFragment1.OnFragmentInteractionListener,BlankFragment2.OnFragmentInteractionListener,BlankFragment3.OnFragmentInteractionListener,BackupFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initToolbar();
        initTabFragment(savedInstanceState);


    }
    private void initToolbar() {


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("SlidingTabs Demo2");
    }

    private void initTabFragment(Bundle savedInstanceState){
        if (savedInstanceState == null) {
            TabFragment tabFragment = new TabFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content_fragment, tabFragment)
                    .commit();
        }
    }
    public void onFragmentInteraction(Uri uri){
        //callback fucntion

    }


}
