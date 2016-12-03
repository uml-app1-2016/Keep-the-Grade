package edu.uml.android.keepthegrade;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Tim on 11/15/2016.
 */

public class ClassActivity extends AppCompatActivity {

    // variables I might need
    private DatabaseUtils dbUtils;
    private CategoryAdapter adapter;
    private int mClassId;
    private String mClassName;
    private ArrayList<Grade> mExamList, mQuizList, mHwList, mFinalList;
    private GradeAdapter mExamAdapter, mQuizAdapter, mHwAdapter, mFinalAdapter;

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

        // Get the list of all the grades based on types and set them into the listViews
        updateGrades();
        updateListView();

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
        adapter = new CategoryAdapter(this, getSupportFragmentManager(), mClassId);

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

    @Override
    public void onResume() {
        super.onResume();
        // We will want to refresh the layout here
        updateGrades();
        updateListView();
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

        // User wants to add a grade
        if (id == R.id.action_add_grade) {
            Intent intent = new Intent(ClassActivity.this, AddGradeActivity.class);
            intent.putExtra("className", mClassName);
            intent.putExtra("classId", mClassId);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    /*
        Update the grades based on the database
     */
    public void updateGrades() {
        double finalGrade = dbUtils.updateClassGrade(mClassId);
        mExamList = dbUtils.getExamGradesList(mClassId);
        mQuizList = dbUtils.getQuizGradesList(mClassId);
        mHwList = dbUtils.getHwGradesList(mClassId);
        mFinalList = dbUtils.getFinalGradesList(mClassId);

        // Set the text view of total grade
        TextView grade = (TextView) findViewById(R.id.currentGrade);
        grade.setText(Double.toString(finalGrade) + "%");
    }

    /*
        Make sure the ListViews for each category are updated.
     */
    public void updateListView() {
        // Set up the adapters
        mExamAdapter = new GradeAdapter(this, mExamList);
        mQuizAdapter = new GradeAdapter(this, mQuizList);
        mHwAdapter = new GradeAdapter(this, mHwList);
        mFinalAdapter = new GradeAdapter(this, mFinalList);
        // Empty array list for if they are empty
        ArrayList<Grade> empty = new ArrayList<Grade>();
        empty.add(new Grade("(Empty)", null, 0, "", 0));
        GradeAdapter emptyAdapter = new GradeAdapter(this, empty);

        // Get the listviews
        ListView examList = (ListView) findViewById(R.id.exam_list);
        ListView quizList = (ListView) findViewById(R.id.quiz_list);
        ListView hwList = (ListView) findViewById(R.id.hw_list);
        ListView finalList = (ListView) findViewById(R.id.final_list);

        // Check to see if each is empty
        if (mExamList.isEmpty()) {
            mExamAdapter = emptyAdapter;
        } else {
            examList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    createDeleteGradePopup(mExamAdapter.getItem(i));
                }
            });
        }
        if (mQuizList.isEmpty()) {
            mQuizAdapter = emptyAdapter;
        } else {
            quizList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    createDeleteGradePopup(mQuizAdapter.getItem(i));
                }
            });
        }
        if (mHwList.isEmpty()) {
            mHwAdapter = emptyAdapter;
        } else {
            hwList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    createDeleteGradePopup(mHwAdapter.getItem(i));
                }
            });
        }
        if (mFinalList.isEmpty()) {
            mFinalAdapter = emptyAdapter;
        } else {
            finalList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    createDeleteGradePopup(mFinalAdapter.getItem(i));
                }
            });
        }

        // Now set the adapter
        examList.setAdapter(mExamAdapter);
        quizList.setAdapter(mQuizAdapter);
        hwList.setAdapter(mHwAdapter);
        finalList.setAdapter(mFinalAdapter);

        // Set the size of the list
        ViewGroup.LayoutParams paramsExam = examList.getLayoutParams();
        paramsExam.height = mExamAdapter.getCount() * 160;
        examList.setLayoutParams(paramsExam);
        examList.requestLayout();
        ViewGroup.LayoutParams paramsQuiz = quizList.getLayoutParams();
        paramsQuiz.height = mQuizAdapter.getCount() * 160;
        quizList.setLayoutParams(paramsQuiz);
        quizList.requestLayout();
        ViewGroup.LayoutParams paramsHw = hwList.getLayoutParams();
        paramsHw.height = mHwAdapter.getCount() * 160;
        hwList.setLayoutParams(paramsHw);
        hwList.requestLayout();
        ViewGroup.LayoutParams paramsFinal = finalList.getLayoutParams();
        paramsFinal.height = mFinalAdapter.getCount() * 160;
        finalList.setLayoutParams(paramsFinal);
        finalList.requestLayout();

    }

    /*
        Create a popup to delete a grade
     */
    public void createDeleteGradePopup(final Grade g) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // Set up the alert dialog
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setMessage("Are you sure you want to delete " + g.getName() + "?");
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
                dbUtils.deleteGrade(g);
                Toast.makeText(ClassActivity.this, "Deleted " + g.getName() + "!", Toast.LENGTH_SHORT).show();
                updateGrades();
                updateListView();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();// show it
        alertDialog.show();
    }
}
