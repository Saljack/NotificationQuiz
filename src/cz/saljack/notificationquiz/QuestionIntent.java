/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.saljack.notificationquiz;

import android.content.Intent;
import android.util.Log;
import cz.saljack.notificationquiz.model.PreviousQuestions;

/**
 *
 * @author saljack
 */
public class QuestionIntent {
    
    public final static String TAG = "QuestionIntent";

    public final IntentType type;
    public final int questionID;
    public final int answer;
    public final PreviousQuestions previous;
    public final boolean existNotification;
    public final int[] permutation;

    public QuestionIntent(Intent intent) {
        if (intent.getBooleanExtra(Constants.NEXT, false)) {
            type = IntentType.NEXT;
        } else if (intent.getBooleanExtra(Constants.CLOSE, false)) {
            type = IntentType.CLOSE;
        } else if (intent.hasExtra(Constants.ANSWER)) {
            type = IntentType.ANSWER;
        } else {
            type = IntentType.NONE;
        }

        questionID = intent.getIntExtra(Constants.QUESTION, -1);
        answer = intent.getIntExtra(Constants.ANSWER, -1);
        
        previous = intent.getParcelableExtra(Constants.PREVIOUS);
        
        existNotification = intent.getBooleanExtra(Constants.EXIST_NOTIFICATION, false);
        permutation = intent.getIntArrayExtra(Constants.PERMUTATION);
    }

    public enum IntentType {

        NEXT, CLOSE, ANSWER, NONE

    }
}
