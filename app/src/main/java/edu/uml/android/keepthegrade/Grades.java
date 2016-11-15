package edu.uml.android.keepthegrade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Created by Ashley on 11/15/2016.
 */
public class Grades {
    ArrayList<Integer> scores = new ArrayList<>();
    ArrayList<Date> dates = new ArrayList<>();

    public Grades(Collection<Integer> scores, Collection<Date> dates) {
        this.scores.addAll(scores);
        this.dates.addAll(dates);
    }
}