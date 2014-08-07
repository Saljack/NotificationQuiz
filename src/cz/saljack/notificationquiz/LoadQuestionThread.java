/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.saljack.notificationquiz;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import cz.saljack.notificationquiz.model.DB;
import cz.saljack.notificationquiz.model.DBSql;
import java.io.IOException;

/**
 *
 * @author saljack
 */
public class LoadQuestionThread implements Runnable {
    
    public static final String TAG = "LoadQuestionThread";

    private Handler handler;
    private Context ctx;

    public LoadQuestionThread(Handler handler, Context ctx) {
        this.handler = handler;
        this.ctx = ctx;
    }

    public void run() {
        reloadQuestion();
        handler.sendEmptyMessage(NotificationQuiz.ALL_LOADED_WHAT);
    }

    public void reloadQuestion() {
        try {
            DB db = new DBSql(ctx);
            db.clearAll();
            QuestionLoadedListener listener = new QuestionLoadedListener() {

                public void onQuestionLoadedLister(int number) {
                    handler.sendEmptyMessage(NotificationQuiz.LOADED_QUESTION_WHAT);
                }
            };
            XMLLoaderHelper.loadXMLQuestions(ctx, db, listener);
            Log.d(TAG, "Loaded questions: " + db.count());
            db.close();
        } catch (IOException ex) {
            Log.d(TAG, ex.toString());
        }
    }

}
