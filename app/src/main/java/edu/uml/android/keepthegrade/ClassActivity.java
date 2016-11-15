package edu.uml.android.keepthegrade;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

/**
 * Created by Tim on 11/15/2016.
 */

public class ClassActivity extends AppCompatActivity {

    // variables I might need
    private DatabaseUtils dbUtils;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    private String mActivityTitle;
    private DrawerLayout mDrawerLayout;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        dbUtils = new DatabaseUtils(this);

        // TODO: add a class list id
        // mDrawerList = (ListView)findViewById(R.id.class_list);
        // mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
    }

    /*private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Choose a class...");
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }*/
}

/*
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Ashley on 11/15/2016.
 */
    /*
public class ClassActivity extends AppCompatActivity {

    CategoryAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl("http://chart.apis.google.com/chart?\n" +
                "chs=200x200\n" +
                "&chdlp=b\n" +
                "&chtt=Uberman\n" +
                "&chdl=Asleep|Awake\n" +
                "&chd=t:1,11,1,11,1,11,1,11,1,11,1,11\n" +
                "&cht=p\n" +
                "&chco=586F8E,7D858F,586F8E,7D858F,586F8E,7D858F,586F8E,7D858F,586F8E,7D858F,586F8E,7D858F");

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        adapter = new CategoryAdapter(this, getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Find the tab layout that shows the tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        // Connect the tab layout with the view pager. This will
        //   1. Update the tab layout when the view pager is swiped
        //   2. Update the view pager when a tab is selected
        //   3. Set the tab layout's tab names with the view pager's adapter's titles
        //      by calling onPageTitle()
        tabLayout.setupWithViewPager(viewPager);
    }
}
*/
