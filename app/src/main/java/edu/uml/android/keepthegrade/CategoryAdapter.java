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

    /**
     * Create a new {@link CategoryAdapter} object.
     *
     * @param context is the context of the app
     * @param fm is the fragment manager that will keep each fragment's state in the adapter
     *           across swipes.
     */
    public CategoryAdapter(Context context, FragmentManager fm) {
        super(fm);

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

        Chart c = new Chart(getPageTitle(position).toString(), Arrays.asList(85, 92, 76, 100, 88));

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
}