package edu.uml.android.keepthegrade;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private DatabaseUtils dbUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbUtils = new DatabaseUtils(this);

        mDrawerList = (ListView)findViewById(R.id.semester_list);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addSemestersToDrawer();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void addSemestersToDrawer() {
        // Fetch the list of semesters from database and add them to view
        Semester[] semesters = dbUtils.getSemesterList();
        String[] semestersArray = new String[semesters.length + 1];
        for (int i = 0; i < semesters.length; i++) {
            semestersArray[i] = semesters[i].getSeason() + " " + semesters[i].getYear();
        }
        // Add the add new semester button
        semestersArray[semesters.length] = "   + Add new semester";
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, semestersArray);
        // Set the adapter to the list we just made
        mDrawerList.setAdapter(mAdapter);
        // On click listener for each item in drawer
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String newSemester = parent.getItemAtPosition(position).toString();
                // If the user wants to add a semester...
                if (newSemester.equals("   + Add new semester")) {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                        .setCancelable(true)
                        .setMessage("Please enter semester info")
                        .setView(R.layout.add_semester_popup)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Just dismiss the alert
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Add the semester
                                EditText seasonField = (EditText) ((AlertDialog) dialog).findViewById(R.id.season_field);
                                EditText yearField = (EditText) ((AlertDialog) dialog).findViewById(R.id.year_field);
                                EditText currentField = (EditText) ((AlertDialog) dialog).findViewById(R.id.current_field);
                                String y = yearField.getText().toString(), c = currentField.getText().toString(),
                                        s = seasonField.getText().toString();
                                int sI, yI = Integer.parseInt(y), cI = Integer.parseInt(c);
                                if (s.equals("Fall")) sI = 0;
                                else if (s.equals("Winter")) sI = 1;
                                else if (s.equals("Spring")) sI = 2;
                                else if (s.equals("Summer")) sI = 3;
                                else sI = 0;
                                Semester sem = new Semester(sI, yI, cI);
                                if (dbUtils.addSemester(sem)) {
                                    Toast.makeText(MainActivity.this, "Added semester!", Toast.LENGTH_SHORT).show();
                                    mDrawerLayout.closeDrawers();
                                }else {
                                    Toast.makeText(MainActivity.this, "Error adding semester", Toast.LENGTH_SHORT).show();
                                }
                            }
                    }).create();
                    alertDialog.show();
                // Otherwise, just go to the new semester
                } else {
                    Toast.makeText(MainActivity.this, "Now showing " + newSemester, Toast.LENGTH_SHORT).show();
                    // Close the drawer
                    mDrawerLayout.closeDrawers();
                }
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Choose a semester...");
                invalidateOptionsMenu();
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // This is so the hamburer icon is always the correct icon
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Load the menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // User wants to delete semester
        if (id == R.id.action_delete_semester) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            // Set up the alert dialog
            alertDialogBuilder.setCancelable(true);
            alertDialogBuilder.setMessage("Are you sure you want to delete the current semester?");
            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Just dismiss the alert
                    dialogInterface.dismiss();
                }
            });
            alertDialogBuilder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Delete the semester
                    Toast.makeText(MainActivity.this, "Deleted semester!", Toast.LENGTH_SHORT).show();
                }
            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();
            return true;
        }

        // Refresh that drawer
        if (id == R.id.action_refresh_drawer) {
            addSemestersToDrawer();
        }

        // Toggle the drawer
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}