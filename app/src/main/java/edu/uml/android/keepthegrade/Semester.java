package edu.uml.android.keepthegrade;

import java.util.Calendar;

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

    public boolean isCompleted() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        switch (this.getSeason()) {
            case "Fall":
                if (this.getYear() < year) return true;
                else return false;
            case "Winter":
                if (this.getYear() < year) return true;
                else return false;
            case "Spring":
                if (this.getYear() < year) return true;
                else return false;
            case "Summer":
                if (this.getYear() < year) return true;
                else return false;
        }
        return false;
    }

}
