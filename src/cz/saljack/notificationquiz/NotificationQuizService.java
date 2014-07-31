/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.saljack.notificationquiz;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import static android.content.Context.NOTIFICATION_SERVICE;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import cz.saljack.notificationquiz.model.Question;
import cz.saljack.notificationquiz.model.QuestionSQLHelper;
import java.util.List;

/**
 *
 * @author saljack
 */
public class NotificationQuizService extends IntentService {

    private static final String TAG = "NotificationQuizService";

    public NotificationQuizService() {
        super("NotificationQuizService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent.getBooleanExtra(Constants.NEXT, false)) {
            int id = intent.getIntExtra(Constants.QUESTION, 0);

            SQLiteDatabase db = loadDB();

            Question question = QuestionSQLHelper.getRandomQuestion(db);
            NotificationHelper.makeOrUpdateNotification(this, question);
            Log.d(TAG, "ID next question: " + question.getId());
            db.close();
        } else if (intent.getBooleanExtra(Constants.CLOSE, false)) {
            NotificationManager mng = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mng.cancel(Constants.NOTIFICATION_ID);
        } else {
            int id = intent.getIntExtra(Constants.QUESTION, 0);
            
            SQLiteDatabase db = loadDB();
            Question question = QuestionSQLHelper.getQuestionById(db, id);
            NotificationHelper.makeOrUpdateNotification(this, question, intent.getIntExtra(Constants.ANSWER, 0), question.getCorrect());
            db.close();
        }
    }

    private SQLiteDatabase loadDB() {
        QuestionSQLHelper helper = new QuestionSQLHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        return db;
    }

}
