package edu.uml.android.keepthegrade;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import edu.uml.android.keepthegrade.DatabaseContract.SemesterEntry;
import edu.uml.android.keepthegrade.DatabaseContract.ClassEntry;
import edu.uml.android.keepthegrade.DatabaseContract.GradeEntry;

public class DatabaseUtils {

    // Set up private member variables
    private DatabaseHelper dbHelper;
    private Context context;

    public DatabaseUtils(Context c) {
        context = c;
        dbHelper = new DatabaseHelper(context);
    }

    /*
        Return all the contents of the Semesters table in an array of Semesters
     */
    public Semester[] getSemesterList() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + SemesterEntry.TABLE_NAME
                + " ORDER BY " + SemesterEntry.COLUMN_YEAR + " DESC", null);

        Semester[] semListTemp = new Semester[cursor.getCount()];
        int idColumnIndex = cursor.getColumnIndex(SemesterEntry._ID);
        int seasonColumnIndex = cursor.getColumnIndex(SemesterEntry.COLUMN_SEASON);
        int yearColumnIndex = cursor.getColumnIndex(SemesterEntry.COLUMN_YEAR);
        int counter = 0;
        while(cursor.moveToNext()) {
            int currentId = cursor.getInt(idColumnIndex);
            int currentSeason = cursor.getInt(seasonColumnIndex);
            int currentYear = cursor.getInt(yearColumnIndex);
            semListTemp[counter] = new Semester(currentId, currentSeason, currentYear);
            counter++;
        }
        cursor.close();
        return semListTemp;
    }

    /*
        Add the semester to the Semesters table
     */
    public boolean addSemester(Semester semester) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Get the season id first
        String season = semester.getSeason();
        int sI;
        if (season.equals("Fall")) sI = 0;
        else if (season.equals("Winter")) sI = 1;
        else if (season.equals("Spring")) sI = 2;
        else if (season.equals("Summer")) sI = 3;
        else sI = 0;

        ContentValues values = new ContentValues();
        values.put(SemesterEntry.COLUMN_SEASON, sI);
        values.put(SemesterEntry.COLUMN_YEAR, semester.getYear());

        long newRowId = db.insert(SemesterEntry.TABLE_NAME, null, values);

        if (newRowId != -1)
            return true;
        else
            return false;
    }

    /*
        Delete an entry from the Semesters table
     */
    public void deleteSemester(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String whereClause = SemesterEntry._ID + " = ?";
        String[] whereArgs = { Integer.toString(id) };

        db.delete(SemesterEntry.TABLE_NAME, whereClause, whereArgs);
    }

    /*
        Check to see if there are any other identical entries so we don't create duplicates
        @return true if there are no duplicates
        @return false if there are duplicates
     */
    public boolean checkForDuplicates(String season, int year) {
        int sI;
        if (season.equals("Fall")) sI = 0;
        else if (season.equals("Winter")) sI = 1;
        else if (season.equals("Spring")) sI = 2;
        else if (season.equals("Summer")) sI = 3;
        else sI = 0;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + SemesterEntry.TABLE_NAME
                + " WHERE " + SemesterEntry.COLUMN_SEASON + " = " + sI
                + " AND " + SemesterEntry.COLUMN_YEAR + " = " + SemesterEntry.COLUMN_YEAR, null);

        if (cursor.getCount() != 0) {
            // There are duplicates, so we don't want to add
            return false;
        } else {
            // There are no duplicates, so we can add it
            return true;
        }
    }

}
