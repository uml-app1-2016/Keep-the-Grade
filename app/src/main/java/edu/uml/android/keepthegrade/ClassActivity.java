package edu.uml.android.keepthegrade;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Tim on 11/15/2016.
 */

public class ClassActivity extends AppCompatActivity {

    // variables I might need
    private DatabaseUtils dbUtils;
    private CategoryAdapter adapter;
    private int mClassId;
    private String mClassName;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        dbUtils = new DatabaseUtils(this);

        // Set up private variables
        mClassName = getIntent().getStringExtra("className");
        mClassId = getIntent().getIntExtra("classId", 0);

        // Set the titlebar
        getSupportActionBar().setTitle(mClassName);

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

    /*
        Create the options menu in the action bar.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Load the menu
        getMenuInflater().inflate(R.menu.menu_class, menu);
        return true;
    }

    /*
        Find what option was selected and deal with accordingly.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        final Context context = this;

        // User wants to delete class
        if (id == R.id.action_delete_class) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            // Set up the alert dialog
            alertDialogBuilder.setCancelable(true);
            alertDialogBuilder.setMessage("Are you sure you want to delete the current class?");
            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Just dismiss the alert
                    dialogInterface.dismiss();
                }
            });
            alertDialogBuilder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Delete the class
                    dbUtils.deleteClass(mClassId);
                    Toast.makeText(ClassActivity.this, "Deleted class!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();// show it
            alertDialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
