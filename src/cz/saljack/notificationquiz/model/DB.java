/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.saljack.notificationquiz.model;

import java.io.Closeable;

/**
 *
 * @author saljack
 */
public interface DB extends Closeable{
    public void insert(Question question);
    
    public Question getQuestionById(int id);
    
    public Question getRandomQuestion();
    
    public void clearAll();
    
    public int count();
}
