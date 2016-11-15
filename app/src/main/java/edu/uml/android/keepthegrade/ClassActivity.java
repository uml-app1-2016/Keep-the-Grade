package edu.uml.android.keepthegrade;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Tim on 11/15/2016.
 */

public class ClassActivity extends AppCompatActivity {

    // variables I might need
    private DatabaseUtils dbUtils;
    private CategoryAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        dbUtils = new DatabaseUtils(this);

        /* WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl("http://chart.apis.google.com/chart?\n" +
                "chs=200x200\n" +
                "&chdlp=b\n" +
                "&chtt=Uberman\n" +
                "&chdl=Asleep|Awake\n" +
                "&chd=t:1,11,1,11,1,11,1,11,1,11,1,11\n" +
                "&cht=p\n" +
                "&chco=586F8E,7D858F,586F8E,7D858F,586F8E,7D858F,586F8E,7D858F,586F8E,7D858F,586F8E,7D858F"); */

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
