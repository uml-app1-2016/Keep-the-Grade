package edu.uml.android.keepthegrade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by adam on 11/15/16.
 */

public class ClassAdapter extends ArrayAdapter<Class> {

    public ClassAdapter(Context context, ArrayList<Class> classes){
        super(context, 0, classes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Make sure the view hasn't been used yet, if not, inflate it
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.class_item, parent, false);
        }

        // Get the current class object
        Class currentClass = getItem(position);

        TextView nameView = (TextView) listItemView.findViewById(R.id.class_name);
        nameView.setText(currentClass.getName());

        TextView gradeView = (TextView) listItemView.findViewById(R.id.class_grade);
        if (gradeView.getText().toString().equals("-1"))
            gradeView.setText("--");
        else
            gradeView.setText(Double.toString(currentClass.getGrade()));

        return listItemView;
    }

}
