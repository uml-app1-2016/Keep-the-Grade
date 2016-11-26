package edu.uml.android.keepthegrade;

import java.util.Date;

/**
 * Created by adam on 11/22/16.
 */

public class Grade {

    private String mName;
    private Date mDate;
    private int mId;
    private String mType;
    private double mGrade;

    Grade(String n, Date d, int i, String t, double g) {
        mName = n;
        mDate = d;
        mId = i;
        mType = t;
        mGrade = g;
    }

    public String getName() { return mName; }
    public Date getDate() { return mDate; }
    public int getId() { return mId; }
    public String getType() { return mType; }
    public double getGrade() { return mGrade; }

}
