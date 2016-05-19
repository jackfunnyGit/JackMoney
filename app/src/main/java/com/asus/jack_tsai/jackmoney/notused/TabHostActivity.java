package com.asus.jack_tsai.jackmoney.notused;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.asus.jack_tsai.jackmoney.HomeMoneyCalendarFragment;
import com.asus.jack_tsai.jackmoney.HomeMoneyViewFragment;
import com.asus.jack_tsai.jackmoney.R;
import com.asus.jack_tsai.jackmoney.HomeMoneyActivity;

public class TabHostActivity extends AppCompatActivity implements HomeMoneyCalendarFragment.OnFragmentInteractionListener,HomeMoneyViewFragment.OnFragmentInteractionListener {
    private int mImages[] = {
            R.drawable.tab_home,
            R.drawable.tab_statistics,
            R.drawable.tab_setting

    };
    private String mFragmentTags[] = {
            "Fragment_Tag1",
            "Fragment_Tag2",
            "Fragment_Tag3"

    };

    private int mTags_text[] = {
            R.string.tab_home,
            R.string.tab_statistics,
            R.string.tab_setting

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("SlidingTabs Demo");

        FragmentTabHost mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        mTabHost.getTabWidget().setDividerDrawable(null);//erase breakline
        for (int i = 0; i < mImages.length; i++) {
            // Tab按钮添加文字和图片
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mFragmentTags[i]).setIndicator(getImageView(i));
            // 添加Fragment
            if (i==0)mTabHost.addTab(tabSpec, HomeMoneyCalendarFragment.class, null);
           else mTabHost.addTab(tabSpec, HomeMoneyViewFragment.class, null);
            // 设置Tab按钮的背景
            mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.color.pedo_actionbar_bkg);
        }

       /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    // 获得图片资源
    private View getImageView(int index) {
        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.view_tab_indicator, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.tab_iv_image);
        imageView.setImageResource(mImages[index]);
        TextView textView = (TextView) view.findViewById(R.id.tabtext);
        textView.setText(mTags_text[index]);
        return view;
    }

    public void onFragmentInteraction(Uri uri){
    //callback fucntion

    }
    public void OpenCamera(View view){
       Log.e("jackfunny", "OpenCamera ....");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);


    }
    public void JumpToSecond(View view){

        Intent intent = new Intent(this,HomeMoneyActivity.class);
        startActivity(intent);
    }
    /*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap mbmp = (Bitmap) data.getExtras().get("data");
            HomeMoneyCalendarFragment blank1 = (HomeMoneyCalendarFragment)getSupportFragmentManager().findFragmentByTag("Fragment_Tag2");
            ImageView img =(ImageView ) blank1.getView().findViewById(R.id.img);
            img.setImageBitmap(mbmp);

        }
    }*/
}
