/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.saljack.notificationquiz.model;

import android.content.ContentValues;
import android.database.Cursor;

/**
 *
 * @author saljack
 */
public class Question {

    private String question;
    private int id;

    private String[] answers;

    private int correct;
    
    private int level;
    
    private String language;
    
    //TODO category
    
    

    public Question() {
    }

    public Question(Cursor cursor) {
        id = cursor.getInt(cursor.getColumnIndex(QuestionSQLHelper.COLUMN_ID));
        question = cursor.getString(cursor.getColumnIndex(QuestionSQLHelper.COLUMN_QUESTION));
        
        answers = new String[3];
        answers[0] = cursor.getString(cursor.getColumnIndex(QuestionSQLHelper.COLUMN_ANSWER_A));
        answers[1] = cursor.getString(cursor.getColumnIndex(QuestionSQLHelper.COLUMN_ANSWER_B));
        answers[2] = cursor.getString(cursor.getColumnIndex(QuestionSQLHelper.COLUMN_ANSWER_C));
        
        correct = cursor.getInt(cursor.getColumnIndex(QuestionSQLHelper.COLUMN_CORRECT));
    }

    public Question(int id, String question, String[] answers, int correct) {
        this.question = question;
        this.id = id;
        this.answers = answers;
        this.correct = correct;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String[] getAnswers() {
        return answers;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
//        cv.put(QuestionSQLHelper.COLUMN_ID, id);
        cv.put(QuestionSQLHelper.COLUMN_QUESTION, question);
        cv.put(QuestionSQLHelper.COLUMN_ANSWER_A, answers[0]);
        cv.put(QuestionSQLHelper.COLUMN_ANSWER_B, answers[1]);
        cv.put(QuestionSQLHelper.COLUMN_ANSWER_C, answers[2]);
        cv.put(QuestionSQLHelper.COLUMN_CORRECT, correct);

        return cv;
    }

    @Override
    public String toString() {
        return id+": "+question +"\n a)"+answers[0] + "\n b)"+answers[1] + "\n c)"+answers[2] +"\nCorrect: "+ correct;
    }
    
    
}
