package edu.uml.android.keepthegrade;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import edu.uml.android.keepthegrade.DatabaseContract.SemesterEntry;
import edu.uml.android.keepthegrade.DatabaseContract.ClassEntry;
import edu.uml.android.keepthegrade.DatabaseContract.GradeEntry;

public class DatabaseUtils {

    private DatabaseHelper dbHelper;
    private Context context;

    public DatabaseUtils(Context c) {
        context = c;
        dbHelper = new DatabaseHelper(context);
    }

    public Semester[] getSemesterList() {
        Semester semList[];
        String[] projection = {
                SemesterEntry._ID,
                SemesterEntry.COLUMN_SEASON,
                SemesterEntry.COLUMN_YEAR,
                SemesterEntry.COLUMN_CURRENT
        };

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                SemesterEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        try {
            Semester[] semListTemp = new Semester[cursor.getCount()];
            int idColumnIndex = cursor.getColumnIndex(SemesterEntry._ID);
            int seasonColumnIndex = cursor.getColumnIndex(SemesterEntry.COLUMN_SEASON);
            int yearColumnIndex = cursor.getColumnIndex(SemesterEntry.COLUMN_YEAR);
            int currentColumnIndex = cursor.getColumnIndex(SemesterEntry.COLUMN_CURRENT);
            int counter = 0;
            while(cursor.moveToNext()) {
                int currentId = cursor.getInt(idColumnIndex);
                int currentSeason = cursor.getInt(seasonColumnIndex);
                int currentYear = cursor.getInt(yearColumnIndex);
                int currentCurrent = cursor.getInt(currentColumnIndex);
                semListTemp[counter] = new Semester(currentId, currentSeason, currentYear, currentCurrent);
            }
            semList = semListTemp;
        } finally {
            cursor.close();
        }
        return semList;
    }

    public boolean addSemester(Semester semester) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SemesterEntry.COLUMN_SEASON, semester.getSeason());
        values.put(SemesterEntry.COLUMN_YEAR, semester.getYear());
        values.put(SemesterEntry.COLUMN_CURRENT, semester.getCurrent());

        long newRowId = db.insert(SemesterEntry.TABLE_NAME, null, values);

        if (newRowId != -1)
            return true;
        else
            return false;
    }

}
