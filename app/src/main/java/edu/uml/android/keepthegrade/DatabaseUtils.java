package edu.uml.android.keepthegrade;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

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
        Cursor cursor = db.query(SemesterEntry.TABLE_NAME,
                new String[]{SemesterEntry.COLUMN_SEASON},
                SemesterEntry.COLUMN_SEASON + " = ? AND " + SemesterEntry.COLUMN_YEAR + " = ?",
                new String[]{Integer.toString(sI), Integer.toString(year)},
                null, null, null);

        if (cursor.getCount() != 0) {
            // There are duplicates, so we don't want to add
            return false;
        } else {
            // There are no duplicates, so we can add it
            return true;
        }
    }

    /*
        Return the list of classes based on current semester
        @param currentSemesterId = the id of the semester user is viewing
        @return list of classes
     */
    public ArrayList<Class> getClassList(int currentSemesterId) {
        ArrayList<Class> classes = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(ClassEntry.TABLE_NAME,
                new String[]{ClassEntry._ID, ClassEntry.COLUMN_SEM_ID, ClassEntry.COLUMN_NAME, ClassEntry.COLUMN_GRADE},
                ClassEntry.COLUMN_SEM_ID + " = ?",
                new String[]{Integer.toString(currentSemesterId)},
                null, null, null);

        int classIdColumnIndex = cursor.getColumnIndex(ClassEntry._ID);
        int semIdColumnIndex = cursor.getColumnIndex(ClassEntry.COLUMN_SEM_ID);
        int nameColumnIndex = cursor.getColumnIndex(ClassEntry.COLUMN_NAME);
        int gradeColumnIndex = cursor.getColumnIndex(ClassEntry.COLUMN_GRADE);
        int counter = 0;
        while(cursor.moveToNext()) {
            classes.add(counter, new Class(cursor.getInt(classIdColumnIndex), cursor.getInt(semIdColumnIndex),
                    cursor.getString(nameColumnIndex), cursor.getDouble(gradeColumnIndex)));
            counter++;
        }
        return classes;
    }

    public boolean addClass(Class c) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ClassEntry.COLUMN_SEM_ID, c.getSemesterId());
        values.put(ClassEntry.COLUMN_NAME, c.getName());
        values.put(ClassEntry.COLUMN_GRADE, c.getGrade());

        long newRowId = db.insert(ClassEntry.TABLE_NAME, null, values);

        if (newRowId != -1)
            return true;
        else
            return false;
    }

}
