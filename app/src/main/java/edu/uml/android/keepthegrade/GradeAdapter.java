package edu.uml.android.keepthegrade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by adam on 11/29/16.
 */

public class GradeAdapter extends ArrayAdapter<Grade> {

    public GradeAdapter(Context context, ArrayList<Grade> grades){
        super(context, 0, grades);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Make sure the view hasn't been used yet, if not, inflate it
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.grade_item, parent, false);
        }

        // Current grade object
        Grade currentGrade = getItem(position);

        // Set the TextViews to contain the right stuff
        TextView nameView = (TextView) listItemView.findViewById(R.id.grade_name);
        nameView.setText(currentGrade.getName());
        TextView gradeView = (TextView) listItemView.findViewById(R.id.grade_grade);
        if (!currentGrade.getName().equals("(Empty)"))
            gradeView.setText(currentGrade.getGrade() + "%");
        else
            gradeView.setText("");

        return listItemView;
    }

}
