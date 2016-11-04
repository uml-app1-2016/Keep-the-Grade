package edu.uml.android.keepthegrade;

/**
 * Created by adam on 11/2/16.
 */

public class Semester {

    private int id;
    private String season;
    private int year;

    public Semester(int i, int s, int y) {
        id = i;
        switch (s) {
            case 0:
                season = "Fall";
                break;
            case 1:
                season = "Winter";
                break;
            case 2:
                season = "Spring";
                break;
            case 3:
                season = "Summer";
                break;
        }
        year = y;
    }

    public Semester(int s, int y) {
        switch (s) {
            case 0:
                season = "Fall";
                break;
            case 1:
                season = "Winter";
                break;
            case 2:
                season = "Spring";
                break;
            case 3:
                season = "Summer";
                break;
        }
        year = y;
    }

    public int getId() { return id; }
    public String getSeason() { return season; }
    public int getYear() { return year; }

}
