package edu.uml.android.keepthegrade;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by adam on 11/22/16.
 */

public class AddGradeActivity extends AppCompatActivity {

    private int mClassId;
    private String mClassName;
    private DatabaseUtils dbUtils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grade);

        // Set up private variables and set the title bar
        mClassId = getIntent().getIntExtra("classId", 0);
        mClassName = getIntent().getStringExtra("className");
        dbUtils = new DatabaseUtils(this);
        getSupportActionBar().setTitle("Add a grade");

        // Have the layout display the class you're adding it to
        TextView classToAdd = (TextView) findViewById(R.id.class_to_add_to);
        classToAdd.setText("You're adding a grade to " + mClassName);

        // Set up an on click listener for the burron
        Button addButton = (Button) findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Make sure all fields are filled out
                EditText nameField = (EditText) findViewById(R.id.name_field);
                String name = nameField.getText().toString();
                String type = "";
                RadioButton examRadio = (RadioButton) findViewById(R.id.radio_exam);
                RadioButton quizRadio = (RadioButton) findViewById(R.id.radio_quiz);
                RadioButton hwRadio = (RadioButton) findViewById(R.id.radio_hw);
                RadioButton finalRadio = (RadioButton) findViewById(R.id.radio_final);
                if (examRadio.isChecked())
                    type = "Exam";
                else if (quizRadio.isChecked())
                    type = "Quiz";
                else if (hwRadio.isChecked())
                    type = "Hw";
                else if (finalRadio.isChecked())
                    type = "Final";
                double grade = -1;
                EditText gradeField = (EditText) findViewById(R.id.grade_field);
                if (!gradeField.getText().toString().equals(""))
                    grade = Double.parseDouble(gradeField.getText().toString());
                if (name.equals("") || type.equals("") || grade < 0) {
                    Toast.makeText(AddGradeActivity.this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
                } else {
                    // All good, add to database
                    Toast.makeText(AddGradeActivity.this, "Added grade!", Toast.LENGTH_SHORT).show();
                    Grade gradeToAdd = new Grade(name, null, mClassId, type, grade);
                    dbUtils.addGrade(gradeToAdd);
                    finish();
                }
            }
        });
    }

}
