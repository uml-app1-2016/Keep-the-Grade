package edu.uml.android.keepthegrade;

import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ashley on 11/15/2016.
 *
 * For information about the Google Chart API used, see:
 * https://developers.google.com/chart/image/
 */

public class Chart {
    /** Chart Title */
    private String mChartTitle = null;

    /** Scores for the grades shown in this chart */
    ArrayList<Double> scores = new ArrayList<>();

    /** Image URI for the chart */
    private Uri mImageURI = null;

    /**
     * Create a new Chart object.
     *
     * @param chartTitle is the artist of the chart
     * @param grades is the list of grades for this chart
     *
     */

    public Chart(String chartTitle, List<Double> grades, List<String> names) {
        mChartTitle = chartTitle;
        setGrades(grades, names);
//        if(imageURI != null) {mImageURI = Uri.parse(imageURI);}
    }

    /** Set a new group of grades */
    public void setGrades(List<Double> grades, List<String> names) {
        scores.clear();
        scores.addAll(grades);

        // Turn array of grades into comma-separated list for URI
        StringBuilder sb = new StringBuilder();
        for (double i : grades) {
            sb.append(i);
            sb.append(",");
        }

        // Turn array of names into bar-separated list for URI
        //|Exam 1
        StringBuilder nb = new StringBuilder();
        for (String j : names) {
            nb.append(j.replaceAll(" ", "_"));
            nb.append("|");
        }

        // Remove last comma delimiter with setLength.
        try {
            sb.setLength(sb.length() - 1);
        } catch (StringIndexOutOfBoundsException e) {
            sb.setLength(0);
        }

        //TODO Explore other options like axes labels
        // https://developers.google.com/chart/image/docs/chart_params#axis-styles-and-labels-line------bar-google-o-meter-radar-scatter
        Log.i(this.getClass().getSimpleName(), "INFO: " + nb.toString());
        mImageURI = Uri.parse(
                "http://chart.apis.google.com/chart?"
                        + "chs=400x400&"                            // Chart size
                        + "chdlp=b&"                                // Chart legend position = bottom
                        + "chtt=" + mChartTitle + "&"               // Chart title
                        + "chbh=r,0.5,1.5&"                         // Chart scaling
                        + "cht=bvs&"                                // Chart type
                        + "chxt=x,y&"                               // Chart axis
                        + "chxl=0:|" + nb.toString() + "&"          // Chart data grades
                        + "chd=t:" + sb.toString()                  // Chart data names
        );
    }

    /**
     * Get the chart title.
     */
    public String getChartTitle() { return mChartTitle; }

    /**
     * Return the URI of the image for the chart.
     */
    public Uri getImageURI() {
        return mImageURI;
    }

    /**
     * Returns whether or not there is an image for this chart.
     */
    public boolean hasImage() {
        return mImageURI != null;
    }
}
