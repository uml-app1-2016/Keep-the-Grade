package edu.uml.android.keepthegrade;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Ashley on 11/15/2016.
 */
public class CategoryAdapter extends FragmentPagerAdapter {

    private ArrayList<String> titles = new ArrayList<String>();
    private ArrayList<ChartFragment> chartFragments = new ArrayList<ChartFragment>();
    private int mClassID;
    private Context mC;

    /**
     * Create a new {@link CategoryAdapter} object.
     *
     * @param context is the context of the app
     * @param fm is the fragment manager that will keep each fragment's state in the adapter
     *           across swipes.
     */
    public CategoryAdapter(Context context, FragmentManager fm, int classID) {
        super(fm);
        mClassID = classID;
        mC = context;

        Collections.addAll(titles, context.getResources().getStringArray(R.array.chart_titles));

        for (int i = 0; i < getCount(); i++) {
            chartFragments.add(null);
        }
    }

    /**
     * Return the {@link Fragment} that should be displayed for the given page number.
     */
    @Override
    public Fragment getItem(int position) {
        if (chartFragments.get(position) != null) {
            return chartFragments.get(position);
        }

        Chart c = null;
        DatabaseUtils d = new DatabaseUtils(mC);

        if(position == 0) {          //All
            c = new Chart(getPageTitle(position).toString(), Arrays.asList(85.0, 92.0, 76.0, 100.0, 88.0), Arrays.asList("A", "B", "C", "D", "E"));
        }else if(position == 1) {    //Exams
            c = new Chart(getPageTitle(position).toString(), getGradeFromList(d.getExamGradesList(mClassID)), getNameFromList(d.getExamGradesList(mClassID)));
            //c = new Chart(getPageTitle(position).toString(), Arrays.asList(85, 92, 100, 100, 88), Arrays.asList("A", "B", "C", "D", "E"));
        }else if(position == 2) {     //HW
            c = new Chart(getPageTitle(position).toString(), getGradeFromList(d.getHwGradesList(mClassID)), getNameFromList(d.getHwGradesList(mClassID)));
        }else if(position == 3) {     //Quizzes
            c = new Chart(getPageTitle(position).toString(),getGradeFromList(d.getQuizGradesList(mClassID)), getNameFromList(d.getQuizGradesList(mClassID)));
        }else if(position == 4) {     //Final
            c = new Chart(getPageTitle(position).toString(), getGradeFromList(d.getFinalGradesList(mClassID)), getNameFromList(d.getFinalGradesList(mClassID)));
        }

//        Chart c = new Chart(getPageTitle(position).toString(), Arrays.asList(85, 92, 76, 100, 88));

        ChartFragment f = new ChartFragment();
        f.setChart(c);
        chartFragments.set(position, f);
        return f;
    }

    /**
     * Return the total number of pages.
     */
    @Override
    public int getCount() { return titles.size(); }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    public ArrayList getGradeFromList(ArrayList<Grade> g) {
        ArrayList grades = new ArrayList();

        for(int i = 0; i < g.size(); i++){
            grades.add(g.get(i).getGrade());
        }
        return grades;
    }

    public ArrayList getNameFromList(ArrayList<Grade> g) {
        ArrayList grades = new ArrayList();

        for(int i = 0; i < g.size(); i++){
            grades.add(g.get(i).getName());
        }
        return grades;
    }
}
