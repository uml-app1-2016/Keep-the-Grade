package edu.uml.android.keepthegrade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
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

        // Change color based on current grade
        LinearLayout mainView = (LinearLayout) listItemView.findViewById(R.id.class_item_ll);
        if (currentClass.getGrade() >= 90)
            mainView.setBackgroundColor(getContext().getResources().getColor(R.color.colorStatus100));
        else if (currentClass.getGrade() <= 90 && currentClass.getGrade() > 80)
            mainView.setBackgroundColor(getContext().getResources().getColor(R.color.colorStatus90));
        else if (currentClass.getGrade() <= 80 && currentClass.getGrade() > 70)
            mainView.setBackgroundColor(getContext().getResources().getColor(R.color.colorStatus80));
        else if (currentClass.getGrade() <= 70 && currentClass.getGrade() > 60)
            mainView.setBackgroundColor(getContext().getResources().getColor(R.color.colorStatus70));
        else if (currentClass.getGrade() <= 60 && currentClass.getGrade() > 50)
            mainView.setBackgroundColor(getContext().getResources().getColor(R.color.colorStatus60));
        else if (currentClass.getGrade() <= 50 && currentClass.getGrade() != -1)
            mainView.setBackgroundColor(getContext().getResources().getColor(R.color.colorStatus50));
        else if (currentClass.getGrade() == -1)
            mainView.setBackgroundColor(getContext().getResources().getColor(R.color.colorListItem));

        TextView nameView = (TextView) listItemView.findViewById(R.id.class_name);
        nameView.setText(currentClass.getName());

        TextView gradeView = (TextView) listItemView.findViewById(R.id.class_grade);
        if (currentClass.getGrade() == -1)
            gradeView.setText("N/A");
        else
            gradeView.setText(Double.toString(currentClass.getGrade()));

        return listItemView;
    }

}
