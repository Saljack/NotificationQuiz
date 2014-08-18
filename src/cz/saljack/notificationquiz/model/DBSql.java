/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.saljack.notificationquiz.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 *
 * @author saljack
 */
public class DBSql implements DB {

    private SQLiteDatabase db;
    private QuestionSQLHelper helper;

    public DBSql(Context ctx) {
        this(ctx, false);
    }

    public DBSql(Context ctx, boolean readOnly) {
        helper = new QuestionSQLHelper(ctx);
        if (readOnly) {
            db = helper.getReadableDatabase();
        } else {
            db = helper.getWritableDatabase();
        }
    }

    public void insert(Question question) {
        QuestionSQLHelper.insertQuestion(db, question);
    }

    public void close() {
        db.close();
    }

    public Question getQuestionById(int id) {
        return QuestionSQLHelper.getQuestionById(db, id);
    }

    public Question getRandomQuestion() {
        return QuestionSQLHelper.getRandomQuestion(db);
    }

    public void clearAll() {
        helper.onUpgrade(db, 1, 2);
    }

    public int count() {
        return (int) QuestionSQLHelper.getCount(db);
    }

    public Question getRandomQuestionWithouPrevious(PreviousQuestions previousQuestions) {
        return QuestionSQLHelper.getRandomQuestionWithoutPrevious(db, previousQuestions);
    }

}
