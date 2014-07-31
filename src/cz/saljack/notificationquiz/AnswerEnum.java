/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.saljack.notificationquiz;

/**
 *
 * @author saljack
 */
public enum AnswerEnum {
    A(0), B(1), C(2);

    public static AnswerEnum values[] = {A, B, C};
    private final int value;
    private AnswerEnum(int value) {
        this.value = value;
    }
    
    public int getID(){
        return value;
    }
    
    
}
