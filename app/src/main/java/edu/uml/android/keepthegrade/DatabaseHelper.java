package edu.uml.android.keepthegrade;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import edu.uml.android.keepthegrade.DatabaseContract.SemesterEntry;
import edu.uml.android.keepthegrade.DatabaseContract.ClassEntry;
import edu.uml.android.keepthegrade.DatabaseContract.GradeEntry;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "keepthegrade.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_SEMESTER_TABLE = "CREATE TABLE " + SemesterEntry.TABLE_NAME + " ("
                + SemesterEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SemesterEntry.COLUMN_SEASON + " INTEGER NOT NULL, "
                + SemesterEntry.COLUMN_YEAR + " TEXT NOT NULL);";
        String SQL_CREATE_CLASS_TABLE = "CREATE TABLE " + ClassEntry.TABLE_NAME + " ("
                + ClassEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ClassEntry.COLUMN_SEM_ID + " INTEGER NOT NULL, "
                + ClassEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + ClassEntry.COLUMN_GRADE + " FLOAT NOT NULL);";
        String SQL_CREATE_GRADE_TABLE = "CREATE TABLE " + GradeEntry.TABLE_NAME + " ("
                + GradeEntry.COLUMN_CLASS_ID + " INTEGER, "
                + GradeEntry.COLUMN_NAME + " TEXT, "
                + GradeEntry.COLUMN_DATE + " DATE, "
                + GradeEntry.COLUMN_TYPE + " INTEGER, "
                + GradeEntry.COLUMN_GRADE + " FLOAT);";

        db.execSQL(SQL_CREATE_SEMESTER_TABLE);
        db.execSQL(SQL_CREATE_CLASS_TABLE);
        db.execSQL(SQL_CREATE_GRADE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }

}
