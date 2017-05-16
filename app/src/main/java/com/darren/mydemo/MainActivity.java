package com.darren.mydemo;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.app.FragmentManager;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.darren.mydemo.fragment.JokeFragment;
import com.darren.mydemo.fragment.NewsFragment;
import com.darren.mydemo.fragment.PersonFragment;
import com.darren.mydemo.fragment.PicFragment;
import com.darren.mydemo.fragment.PictureFragment;
import com.darren.mydemo.utils.BottomNavigationViewEx;


public class MainActivity extends AppCompatActivity {
    private NewsFragment newsFragment;
    private JokeFragment jokeFragment;
    private PictureFragment pictureFragment;
    private PicFragment picFragment;
    private PersonFragment personFragment;
    private Toolbar toolbar;
    private TextView tv_app_title;
    private FragmentManager fm;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //toolbar.setTitle("");
        setSupportActionBar(toolbar);
        initView();
    }

    private void initView() {
        BottomNavigationViewEx navigation = (BottomNavigationViewEx) findViewById(R.id.navigation);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_app_title = (TextView) findViewById(R.id.tv_app_title);
        navigation.enableAnimation(false);
        navigation.enableShiftingMode(false);
        navigation.enableItemShiftingMode(false);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fm = getFragmentManager();
        fragmentTransaction = fm.beginTransaction();
        newsFragment = new NewsFragment();
        tv_app_title.setText(R.string.news);
        fragmentTransaction.add(R.id.content, newsFragment);
        fragmentTransaction.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_news:
                    newsFragment = new NewsFragment();
                    tv_app_title.setText(R.string.news);
                    replace(newsFragment);
                    return true;
                case R.id.navigation_joke:
                    jokeFragment = new JokeFragment();
                    tv_app_title.setText(R.string.jokes);
                    replace(jokeFragment);
                    return true;
                case R.id.navigation_picture:
//                    pictureFragment = new PictureFragment();
//                    replace(pictureFragment);
                    picFragment = new PicFragment();
                    tv_app_title.setText(R.string.pictures);
                    replace(picFragment);
                    return true;
                case R.id.navigation_person:
                    personFragment = new PersonFragment();
                    tv_app_title.setText(R.string.myself);
                    replace(personFragment);
                    return true;
            }
            return false;
        }
    };

    private void replace(Fragment fragment) {
        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment);
        fragmentTransaction.commit();
    }
}
