package edu.uml.android.keepthegrade;

/**
 * Created by adam on 11/22/16.
 */

public class Weight {

    private int mClassId;
    private int mExam;
    private int mQuiz;
    private int mHw;
    private int mFinal;

    Weight(int c, int e, int q, int h, int f) {
        mClassId = c;
        mExam = e;
        mQuiz = q;
        mHw = h;
        mFinal = f;
    }

    int getClassId() { return mClassId; }
    int getExam() { return mExam; }
    int getQuiz() { return mQuiz; }
    int getHw() { return mHw; }
    int getFinal() { return mFinal; }

}
