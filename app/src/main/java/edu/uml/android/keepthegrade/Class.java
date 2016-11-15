package edu.uml.android.keepthegrade;

/**
 * Created by adam on 11/15/16.
 */

public class Class {

    // Private member variables
    private int mClassId;
    private int mSemesterId;
    private String mName;
    private double mGrade;

    // Default constructor
    public Class() {
        mClassId = -1;
        mSemesterId = -1;
        mName = "";
        mGrade = -1;
    }

    // Overloaded constructor
    public Class(int cId, int sId, String name, double grade) {
        mClassId = cId;
        mSemesterId = sId;
        mName = name;
        mGrade = grade;
    }

    // Getters
    public int getClassId() { return mClassId; }
    public int getSemesterId() { return mSemesterId; }
    public String getName() { return mName; }
    public double getGrade() { return mGrade; }

}
