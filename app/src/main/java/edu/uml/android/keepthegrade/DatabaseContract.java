package edu.uml.android.keepthegrade;

import android.provider.BaseColumns;

@SuppressWarnings("ALL")
public final class DatabaseContract {

    // Default constructor
    private DatabaseContract() {}

    // Define constant values of the Semesters table
    public static final class SemesterEntry implements BaseColumns {
        public static final String TABLE_NAME = "Semesters";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_SEASON = "season";
        public static final String COLUMN_YEAR = "year";
        public static final int SEASON_FALL = 0;
        public static final int SEASON_WINTER = 1;
        public static final int SEASON_SPRING = 2;
        public static final int SEASON_SUMMER = 3;
    }

    public static final class ClassEntry implements BaseColumns {
        public static final String TABLE_NAME = "Classes";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_SEM_ID = "sem_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_GRADE = "grade";
    }

    public static final class GradeEntry implements BaseColumns {
        public static final String TABLE_NAME = "Grades";
        public static final String COLUMN_CLASS_ID = "class_id";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_GRADE = "grade";
        public static final int TYPE_EXAM = 0;
        public static final int TYPE_QUIZ = 1;
        public static final int TYPE_HW = 2;
        public static final int TYPE_FINAL = 3;
    }

}
