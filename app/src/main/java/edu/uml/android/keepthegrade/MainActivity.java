package edu.uml.android.keepthegrade;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    // Some good ol' private member variables
    // Variables for the drawer
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    // Variables for the use of database
    private DatabaseUtils dbUtils;
    private Semester currentSemester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get our utilities for querying db
        dbUtils = new DatabaseUtils(this);
        // Set up the drawer
        mDrawerList = (ListView)findViewById(R.id.semester_list);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        // Add the semesters, and display the most recent one first.
        addSemestersToDrawer("top", 0);
        setupDrawer();
        updateSemesterView();
        // Display the two menu buttons on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    /*
        Query the database and get the ordered list of semesters and place them in the drawer.
        The two parameters combined are what the function will load in.
        @param season; The season we want to display.
            Note: If season == "top", it will always display the top semester.
        @param year; The year we want to display.
        @return N/A
     */
    private void addSemestersToDrawer(String season, int year) {
        // Fetch the list of semesters from database and add them to view
        Semester[] semesters = dbUtils.getSemesterList();
        String[] semestersArray = new String[semesters.length + 1];
        // Do we want to set the current semester to the top element?
        if (semesters.length != 0 && season.equals("top"))
            currentSemester = semesters[0];
        for (int i = 0; i < semesters.length; i++) {
            semestersArray[i] = semesters[i].getSeason() + " " + semesters[i].getYear();
            // Make sure if this is the element we want displayed on top
            if (semesters[i].getSeason().equals(season) && semesters[i].getYear() == year)
                currentSemester = semesters[i];
        }
        // Add the add new semester button
        semestersArray[semesters.length] = "   + Add new semester";
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, semestersArray);
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
                                String y = yearField.getText().toString(), s = seasonField.getText().toString();
                                // Check to make sure we don't have duplicates
                                if (dbUtils.checkForDuplicates(s, Integer.parseInt(y))) {
                                    int sI, yI = Integer.parseInt(y);
                                    if (s.equals("Fall")) sI = 0;
                                    else if (s.equals("Winter")) sI = 1;
                                    else if (s.equals("Spring")) sI = 2;
                                    else if (s.equals("Summer")) sI = 3;
                                    else sI = 0;
                                    Semester sem = new Semester(sI, yI);
                                    if (dbUtils.addSemester(sem)) {
                                        Toast.makeText(MainActivity.this, "Added semester!", Toast.LENGTH_SHORT).show();
                                        mDrawerLayout.closeDrawers();
                                        addSemestersToDrawer(s, yI);
                                    } else {
                                        Toast.makeText(MainActivity.this, "Error adding semester", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, "Semester already exists.", Toast.LENGTH_SHORT).show();
                                }
                            }
                    }).create();
                    alertDialog.show();
                // Otherwise, just go to the new semester
                } else {
                    String season = newSemester.split("\\s+")[0];
                    int year = Integer.parseInt(newSemester.split("\\s+")[1]);
                    addSemestersToDrawer(season, year);
                    updateSemesterView();
                    // Close the drawer
                    mDrawerLayout.closeDrawers();
                }
            }
        });
    }

    /*
        Set up the drawer states so it works correctly
        @param N/A
        @return N/A
     */

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

    /*
        Simply update the main view so the new current semester is displayed
        @param N/A
        @return N/A
     */
    private void updateSemesterView() {
        TextView tv = (TextView) findViewById(R.id.current_semester_name);
        if (currentSemester != null) {
            tv.setText("(ID: " + currentSemester.getId() + ") " + currentSemester.getSeason()
                + " " + currentSemester.getYear());
        } else {
            tv.setText("No current semester. Please select one from the drawer.");
        }
    }

    /*
        Once the drawer is created, make sure that the icon in the drawer is always at the correct
        state.
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // This is so the hamburer icon is always the correct icon
        mDrawerToggle.syncState();
    }

    /*
        If a configuration was changed, we want to update it accordingly.
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /*
        Create the options menu in the action bar.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Load the menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*
        Find what option was selected and deal with accordingly.
     */
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
                    dbUtils.deleteSemester(currentSemester.getId());
                    currentSemester = null;
                    Toast.makeText(MainActivity.this, "Deleted semester!", Toast.LENGTH_SHORT).show();
                    addSemestersToDrawer("top", 0);
                    updateSemesterView();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();// show it
            alertDialog.show();
            return true;
        }

        // Toggle the drawer
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}