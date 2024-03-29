/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.saljack.notificationquiz.model;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 *
 * @author saljack
 */
public class QuestionSQLHelper extends SQLiteOpenHelper {

    public static final String TAG = "QuestionSQLHelper";

    public static final String DATABASE_NAME = "questionsDB";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "question";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_QUESTION = "question";
    public static final String COLUMN_ANSWER_A = "answer_a";
    public static final String COLUMN_ANSWER_B = "answer_b";
    public static final String COLUMN_ANSWER_C = "answer_c";
    public static final String COLUMN_CORRECT = "correct";

    private static final String SQL_TABLE_CREATE
            = "CREATE TABLE " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_QUESTION + " TEXT, "
            + COLUMN_ANSWER_A + " TEXT, "
            + COLUMN_ANSWER_B + " TEXT, "
            + COLUMN_ANSWER_C + " TEXT, "
            + COLUMN_CORRECT + " INTEGER"
            + ");";

    private static final String SQL_DELETE_ALL
            = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String[] projection
            = {COLUMN_ID, COLUMN_QUESTION, COLUMN_ANSWER_A, COLUMN_ANSWER_B, COLUMN_ANSWER_C, COLUMN_CORRECT};

    public static final String SELECT_BY_ID
            = COLUMN_ID + " = ?";

    public static final String SELECT_NOT_ID_IN_ARRAY = COLUMN_ID + " NOT IN ";

    public QuestionSQLHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        db.execSQL(SQL_DELETE_ALL);
        onCreate(db);
    }

    public static long insertQuestion(SQLiteDatabase db, Question question) {
        return db.insert(TABLE_NAME, null, question.getContentValues());
    }

    public static Question getQuestionById(SQLiteDatabase db, int id) {
        Cursor q = db.query(TABLE_NAME, null, SELECT_BY_ID, new String[]{Integer.toString(id)}, null, null, QuestionSQLHelper.COLUMN_ID);
        q.moveToFirst();
        return new Question(q);
    }

    public static Question getRandomQuestion(SQLiteDatabase db) {
        Cursor q = db.query(TABLE_NAME, null, null, null, null, null, "RANDOM()", "1");
        q.moveToFirst();
        return new Question(q);
    }

    public static Question getRandomQuestionWithoutPrevious(SQLiteDatabase db, PreviousQuestions previousQuestions) {
        if (previousQuestions.getPrevious().size() > 0) {
            String[] args = new String[]{previousQuestions.toString()};
            Cursor q = db.query(TABLE_NAME, null, SELECT_NOT_ID_IN_ARRAY+previousQuestions.toString(), null, null, null, "RANDOM()", "1");
            q.moveToFirst();
            return new Question(q);
        }
        return getRandomQuestion(db);
    }

    public static long getCount(SQLiteDatabase db) {
//        Cursor c = db.query(TABLE_NAME, null, "COUNT(*)", projection, COLUMN_ID, COLUMN_ID, COLUMN_ID)
        long ret = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return ret;
    }
}
