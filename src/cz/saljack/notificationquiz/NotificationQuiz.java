package cz.saljack.notificationquiz;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import cz.saljack.notificationquiz.model.Question;
import cz.saljack.notificationquiz.model.QuestionSQLHelper;
import java.util.List;

public class NotificationQuiz extends Activity {

    private static final String TAG = "NotificationQuiz";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        reloadQuestion(null);
//        NotificationHelper.makeOrUpdateNotification(this, NotificationHelper.loadXMLQuestions(this).get(0));
        

    }

    public void makeNotification(View view) {
        NotificationHelper.makeOrUpdateNotification(this, NotificationHelper.loadXMLQuestions(this).get(0));

    }

    public void reloadQuestion(View view) {
        //Load xml
        List<Question> loadedXML = NotificationHelper.loadXMLQuestions(this);
        
        //Open DB to write
        QuestionSQLHelper helper = new QuestionSQLHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        
        //Drop table and creat new
        helper.onUpgrade(db, 1, 2);
        
        for (Question question : loadedXML) {
            QuestionSQLHelper.insertQuestion(db, question);
        }
        db.close();
    }
}
