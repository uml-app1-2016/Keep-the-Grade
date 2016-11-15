package edu.uml.android.keepthegrade;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Ashley on 11/15/2016.
 */
public class ChartFragment extends Fragment {
    private ArrayList<Chart> charts = new ArrayList<Chart>();
    private ChartAdapter adapter = null;

    /*
     * Can't set this via a constructor as a Fragment must use a default constructor
     */
    public void setChart(Chart chart) {
        charts.clear();     // Only 1 chart at a time per ChartFragment
        charts.add(chart);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //TODO graphs_list feels like an unnecesary layer, figure out how to get rid of it
        View rootView = inflater.inflate(R.layout.graphs_list, container, false);

        //TODO Charts look OK until device is rotated, then chart disappears.
        //TODO Figure out why. Maybe something about creating new adapters with empty charts list???
        // Create an {@link ChartAdapter}, whose data source is a list of {@link Chart}s. The
        // adapter knows how to create list items for each item in the list.
        adapter = new ChartAdapter(getActivity(), charts);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // graphs_list.xmlml layout file.
        ListView listView = (ListView) rootView.findViewById(R.id.list);

        // Make the {@link ListView} use the {@link ChartAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Chart} in the list.
        listView.setAdapter(adapter);

        return rootView;
    }
}
