package edu.uml.android.keepthegrade;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.uml.android.keepthegrade.DatabaseContract.SemesterEntry;
import edu.uml.android.keepthegrade.DatabaseContract.ClassEntry;
import edu.uml.android.keepthegrade.DatabaseContract.GradeEntry;
import edu.uml.android.keepthegrade.DatabaseContract.Weights;

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

        // Delete from the semesters
        String whereClause = SemesterEntry._ID + " = ?";
        String[] whereArgs = { Integer.toString(id) };
        db.delete(SemesterEntry.TABLE_NAME, whereClause, whereArgs);

        // Delete grades and weights for each class
        ArrayList<Class> classes = getClassList(id);
        for (int i = 0; i < classes.size(); i++) {
            // Delete the grades that were in each class
            whereClause = GradeEntry.COLUMN_CLASS_ID + " = ?";
            String[] whereArgsGrades = { Integer.toString(classes.get(i).getClassId()) };
            db.delete(GradeEntry.TABLE_NAME, whereClause, whereArgsGrades);
            // Delete the weights in each class
            whereClause = Weights.COLUMN_CLASS_ID + " = ?";
            String[] whereArgsWeights = { Integer.toString(classes.get(i).getClassId()) };
            db.delete(Weights.TABLE_NAME, whereClause, whereArgsWeights);
        }

        // Delete the classes for the semester
        whereClause = ClassEntry.COLUMN_SEM_ID + " = ?";
        String[] whereArgsClass = { Integer.toString(id) };
        db.delete(ClassEntry.TABLE_NAME, whereClause, whereArgsClass);
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
            cursor.close();
            return false;
        } else {
            // There are no duplicates, so we can add it
            cursor.close();
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
        cursor.close();
        return classes;
    }

    /*
        Add a class to the table.
     */
    public boolean addClass(Class c, int exam, int quiz, int hw, int fin) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ClassEntry.COLUMN_SEM_ID, c.getSemesterId());
        values.put(ClassEntry.COLUMN_NAME, c.getName());
        values.put(ClassEntry.COLUMN_GRADE, c.getGrade());

        // Add it to the Class entry
        long newRowId = db.insert(ClassEntry.TABLE_NAME, null, values);

        // Now add the weights
        values = new ContentValues();
        values.put(Weights.COLUMN_CLASS_ID, newRowId);
        values.put(Weights.COLUMN_EXAM, exam);
        values.put(Weights.COLUMN_QUIZ, quiz);
        values.put(Weights.COLUMN_HW, hw);
        values.put(Weights.COLUMN_FINAL, fin);

        newRowId = db.insert(Weights.TABLE_NAME, null, values);

        if (newRowId != -1)
            return true;
        else
            return false;
    }

    /*
        Delete a class from the table.
        @param classId: The class id we want to delete.
        @return void
     */
    public void deleteClass(int classId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Delete the class
        String whereClause = ClassEntry._ID + " = ?";
        String[] whereArgs = { Integer.toString(classId) };
        db.delete(ClassEntry.TABLE_NAME, whereClause, whereArgs);

        // Delete the grades that were in each class
        whereClause = GradeEntry.COLUMN_CLASS_ID + " = ?";
        String[] whereArgsGrades = { Integer.toString(classId) };
        db.delete(GradeEntry.TABLE_NAME, whereClause, whereArgsGrades);
        // Delete the weights in each class
        whereClause = Weights.COLUMN_CLASS_ID + " = ?";
        String[] whereArgsWeights = { Integer.toString(classId) };
        db.delete(Weights.TABLE_NAME, whereClause, whereArgsWeights);
    }

    /*
        Get the weight for a given class.
     */
    public Weight getWeight(int classId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(Weights.TABLE_NAME,
                new String[]{Weights.COLUMN_CLASS_ID, Weights.COLUMN_EXAM, Weights.COLUMN_QUIZ, Weights.COLUMN_HW, Weights.COLUMN_FINAL},
                Weights.COLUMN_CLASS_ID + " = ?",
                new String[]{Integer.toString(classId)},
                null, null, null);

        int columnClassId = cursor.getColumnIndex(Weights.COLUMN_CLASS_ID);
        int columnExam = cursor.getColumnIndex(Weights.COLUMN_EXAM);
        int columnQuiz = cursor.getColumnIndex(Weights.COLUMN_QUIZ);
        int columnHw = cursor.getColumnIndex(Weights.COLUMN_HW);
        int columnFinal = cursor.getColumnIndex(Weights.COLUMN_FINAL);

        cursor.moveToNext();
        Weight w = new Weight(
                cursor.getInt(columnClassId),
                cursor.getInt(columnExam),
                cursor.getInt(columnQuiz),
                cursor.getInt(columnHw),
                cursor.getInt(columnFinal)
        );

        cursor.close();
        return w;
    }

    /*
        Add a grade to the database.
     */
    public void addGrade(Grade g) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GradeEntry.COLUMN_CLASS_ID, g.getId());
        values.put(GradeEntry.COLUMN_GRADE, g.getGrade());
        values.put(GradeEntry.COLUMN_NAME, g.getName());
        if (g.getType().equals("Exam"))
            values.put(GradeEntry.COLUMN_TYPE, GradeEntry.TYPE_EXAM);
        else if (g.getType().equals("Quiz"))
            values.put(GradeEntry.COLUMN_TYPE, GradeEntry.TYPE_QUIZ);
        else if (g.getType().equals("Hw"))
            values.put(GradeEntry.COLUMN_TYPE, GradeEntry.TYPE_HW);
        else if (g.getType().equals("Final"))
            values.put(GradeEntry.COLUMN_TYPE, GradeEntry.TYPE_FINAL);

        db.insert(GradeEntry.TABLE_NAME, null, values);
    }

    /*
        Get list of exams in a certain class.
     */
    public ArrayList<Grade> getExamGradesList(int classId) {
        ArrayList<Grade> grades = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(GradeEntry.TABLE_NAME,
                new String[]{GradeEntry.COLUMN_NAME, GradeEntry.COLUMN_GRADE},
                GradeEntry.COLUMN_TYPE + " = ? AND " +
                        GradeEntry.COLUMN_CLASS_ID + " = ?",
                new String[]{Integer.toString(GradeEntry.TYPE_EXAM), Integer.toString(classId)},
                null, null, null);

        int columnName = cursor.getColumnIndex(GradeEntry.COLUMN_NAME);
        int columnGrade = cursor.getColumnIndex(GradeEntry.COLUMN_GRADE);
        int counter = 0;
        while (cursor.moveToNext()) {
            grades.add(counter, new Grade(cursor.getString(columnName), null, classId, "Exam", cursor.getDouble(columnGrade)));
            counter++;
        }
        cursor.close();
        return grades;
    }

    /*
        Get list of quizzes in a certain class.
     */
    public ArrayList<Grade> getQuizGradesList(int classId) {
        ArrayList<Grade> grades = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(GradeEntry.TABLE_NAME,
                new String[]{GradeEntry.COLUMN_NAME, GradeEntry.COLUMN_GRADE},
                GradeEntry.COLUMN_TYPE + " = ? AND " +
                        GradeEntry.COLUMN_CLASS_ID + " = ?",
                new String[]{Integer.toString(GradeEntry.TYPE_QUIZ), Integer.toString(classId)},
                null, null, null);

        int columnName = cursor.getColumnIndex(GradeEntry.COLUMN_NAME);
        int columnGrade = cursor.getColumnIndex(GradeEntry.COLUMN_GRADE);
        int counter = 0;
        while (cursor.moveToNext()) {
            grades.add(counter, new Grade(cursor.getString(columnName), null, classId, "Quiz", cursor.getDouble(columnGrade)));
            counter++;
        }
        cursor.close();
        return grades;
    }

    /*
        Get list of hws in a certain class.
     */
    public ArrayList<Grade> getHwGradesList(int classId) {
        ArrayList<Grade> grades = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(GradeEntry.TABLE_NAME,
                new String[]{GradeEntry.COLUMN_NAME, GradeEntry.COLUMN_GRADE},
                GradeEntry.COLUMN_TYPE + " = ? AND " +
                GradeEntry.COLUMN_CLASS_ID + " = ?",
                new String[]{Integer.toString(GradeEntry.TYPE_HW), Integer.toString(classId)},
                null, null, null);

        int columnName = cursor.getColumnIndex(GradeEntry.COLUMN_NAME);
        int columnGrade = cursor.getColumnIndex(GradeEntry.COLUMN_GRADE);
        int counter = 0;
        while (cursor.moveToNext()) {
            grades.add(counter, new Grade(cursor.getString(columnName), null, classId, "Hw", cursor.getDouble(columnGrade)));
            counter++;
        }
        cursor.close();
        return grades;
    }

    /*
        Get list of finals in a certain class.
     */
    public ArrayList<Grade> getFinalGradesList(int classId) {
        ArrayList<Grade> grades = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(GradeEntry.TABLE_NAME,
                new String[]{GradeEntry.COLUMN_NAME, GradeEntry.COLUMN_GRADE},
                GradeEntry.COLUMN_TYPE + " = ? AND " +
                        GradeEntry.COLUMN_CLASS_ID + " = ?",
                new String[]{Integer.toString(GradeEntry.TYPE_FINAL), Integer.toString(classId)},
                null, null, null);

        int columnName = cursor.getColumnIndex(GradeEntry.COLUMN_NAME);
        int columnGrade = cursor.getColumnIndex(GradeEntry.COLUMN_GRADE);
        int counter = 0;
        while (cursor.moveToNext()) {
            grades.add(counter, new Grade(cursor.getString(columnName), null, classId, "Final", cursor.getDouble(columnGrade)));
            counter++;
        }
        cursor.close();
        return grades;
    }

    /*
        Get list of all grades in a certain class.
     */
    public ArrayList<Grade> getCompleteGradesList(int classId) {
        ArrayList<Grade> grades = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(GradeEntry.TABLE_NAME,
                new String[]{GradeEntry.COLUMN_NAME, GradeEntry.COLUMN_GRADE, GradeEntry.COLUMN_TYPE},
                null, null, null, null, null);

        int columnName = cursor.getColumnIndex(GradeEntry.COLUMN_NAME);
        int columnGrade = cursor.getColumnIndex(GradeEntry.COLUMN_GRADE);
        int columnType = cursor.getColumnIndex(GradeEntry.COLUMN_TYPE);
        int counter = 0;
        while (cursor.moveToNext()) {
            String type = "";
            if (cursor.getInt(columnType) == GradeEntry.TYPE_EXAM)
                type = "Exam";
            else if (cursor.getInt(columnType) == GradeEntry.TYPE_QUIZ)
                type = "Quiz";
            else if (cursor.getInt(columnType) == GradeEntry.TYPE_HW)
                type = "Hw";
            else if (cursor.getInt(columnType) == GradeEntry.TYPE_FINAL)
                type = "Final";
            grades.add(counter, new Grade(cursor.getString(columnName), null, classId, type, cursor.getDouble(columnGrade)));
            counter++;
        }
        cursor.close();
        return grades;
    }

    /*
        Delete a grade
     */
    public void deleteGrade(Grade g) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int type = 0;
        if (g.getType().equals("Exam"))
            type = GradeEntry.TYPE_EXAM;
        if (g.getType().equals("Quiz"))
            type = GradeEntry.TYPE_QUIZ;
        if (g.getType().equals("Hw"))
            type = GradeEntry.TYPE_HW;
        if (g.getType().equals("Final"))
            type = GradeEntry.TYPE_FINAL;

        String whereClause = GradeEntry.COLUMN_NAME + " = ? AND "
                + GradeEntry.COLUMN_CLASS_ID + " = ? AND "
                + GradeEntry.COLUMN_GRADE + " = ? AND "
                + GradeEntry.COLUMN_TYPE + " = ?";
        String[] whereArgs = {
                g.getName(),
                Integer.toString(g.getId()),
                Double.toString(g.getGrade()),
                Integer.toString(type)
        };
        db.delete(GradeEntry.TABLE_NAME, whereClause, whereArgs);
    }

    /*
        Update the class grade based on the Grades and Weights table.
     */
    public double updateClassGrade(int classId) {
        SQLiteDatabase readDb = dbHelper.getReadableDatabase();
        SQLiteDatabase writeDb = dbHelper.getWritableDatabase();
        double examWeight = 0, quizWeight = 0, hwWeight = 0, finalWeight = 0;
        double examTotal = 0, quizTotal = 0, hwTotal = 0, finalTotal = 0, finalGrade = 0, totalWeight = 0;

        // Get the weights associated with this class
        Cursor cursor = readDb.query(Weights.TABLE_NAME,
                new String[]{Weights.COLUMN_EXAM, Weights.COLUMN_QUIZ, Weights.COLUMN_HW, Weights.COLUMN_FINAL},
                Weights.COLUMN_CLASS_ID + " = ?",
                new String[]{Integer.toString(classId)},
                null, null, null);
        int examColumn = cursor.getColumnIndex(Weights.COLUMN_EXAM);
        int quizColumn = cursor.getColumnIndex(Weights.COLUMN_QUIZ);
        int hwColumn = cursor.getColumnIndex(Weights.COLUMN_HW);
        int finalColumn = cursor.getColumnIndex(Weights.COLUMN_FINAL);
        cursor.moveToNext();
        examWeight = cursor.getInt(examColumn);
        quizWeight = cursor.getInt(quizColumn);
        hwWeight = cursor.getInt(hwColumn);
        finalWeight = cursor.getInt(finalColumn);

        // Get the list of grades
        ArrayList<Grade> examList = getExamGradesList(classId);
        ArrayList<Grade> quizList = getQuizGradesList(classId);
        ArrayList<Grade> hwList = getHwGradesList(classId);
        ArrayList<Grade> finalList = getFinalGradesList(classId);

        // Do the calculation to get total grade
        if (examList.size() != 0)
            totalWeight += examWeight;
        if (quizList.size() != 0)
            totalWeight += quizWeight;
        if (hwList.size() != 0)
            totalWeight += hwWeight;
        if (finalList.size() != 0)
            totalWeight += finalWeight;
        // Exam Section
        if (examList.size() != 0) {
            for (int i = 0; i < examList.size(); i++)
                examTotal += examList.get(i).getGrade();
            examTotal /= examList.size();
            examTotal = examTotal * (examWeight / 100);
        }
        // Quiz Section
        if (quizList.size() != 0) {
            for (int i = 0; i < quizList.size(); i++)
                quizTotal += quizList.get(i).getGrade();
            quizTotal /= quizList.size();
            quizTotal = quizTotal * (quizWeight / 100);
        }
        // HW Section
        if (hwList.size() != 0) {
            for (int i = 0; i < hwList.size(); i++)
                hwTotal += hwList.get(i).getGrade();
            hwTotal /= hwList.size();
            hwTotal = hwTotal * (hwWeight / 100);
        }
        // Final Section
        if (finalList.size() != 0) {
            for (int i = 0; i < finalList.size(); i++)
                finalTotal += finalList.get(i).getGrade();
            finalTotal /= finalList.size();
            finalTotal = finalTotal * (finalWeight / 100);
        }
        // Now sum it all up
        finalGrade = examTotal + quizTotal + hwTotal + finalTotal;
        totalWeight /= 100;
        finalGrade /= totalWeight;
        finalGrade = Math.round(finalGrade * 100) / 100;
        cursor.close();

        // Update the database now
        if (Double.isNaN(finalGrade)) return -1;
        String whereClause = ClassEntry._ID + " = ?";
        String[] whereArgs = { Integer.toString(classId) };
        ContentValues values = new ContentValues();
        values.put(ClassEntry.COLUMN_GRADE, finalGrade);
        writeDb.update(ClassEntry.TABLE_NAME, values, whereClause, whereArgs);
        return finalGrade;
    }

}
