/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.saljack.notificationquiz.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author saljack
 */
public class DBList implements DB {

    private List<Question> questions;

    public DBList() {
        questions = new ArrayList<Question>();
    }

    public void insert(Question question) {
        questions.add(question);
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public Question getQuestionById(int id) {
        return questions.get(id);
    }

    public Question getRandomQuestion() {
        Random rnd = new Random();
        int rndId = rnd.nextInt(questions.size());
        return questions.get(rndId);
    }

    public void close() {
        //EMPTY
    }

    public void clearAll() {
        questions.clear();
    }

    public int count() {
        return questions.size();
    }
    
    

}
