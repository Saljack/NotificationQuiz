/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.saljack.notificationquiz;

import cz.saljack.notificationquiz.widget.WidgetProvider;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import static android.content.Context.NOTIFICATION_SERVICE;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import cz.saljack.notificationquiz.model.DB;
import cz.saljack.notificationquiz.model.DBSql;
import cz.saljack.notificationquiz.model.PreferencesDB;
import cz.saljack.notificationquiz.model.PreviousQuestions;
import cz.saljack.notificationquiz.model.Question;
import java.io.IOException;

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
        QuestionIntent questionIntent = new QuestionIntent(intent);
        try {

            switch (questionIntent.type) {
                case NEXT:
                    DB db = new DBSql(this, true);

                    PreviousQuestions prev;
                    if (questionIntent.previous == null) {
                        prev = new PreviousQuestions();
                    } else {
                        prev = questionIntent.previous;
                    }

                    if (questionIntent.questionID != -1) {
                        prev.add(questionIntent.questionID);
                    }

//                    Question question = db.getRandomQuestion();
                    Question question = db.getRandomQuestionWithouPrevious(prev);
                    question.makePermutation();

                    if (PreferencesDB.isNotificationVisible(this)) {
                        nextNotificationQuestion(question, prev);
                    }
                    nextWidgetQuestion(question, prev);

                    db.close();
                    break;
                case CLOSE:
                    NotificationManager mng = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    mng.cancel(Constants.NOTIFICATION_ID);
                    PreferencesDB.setNotificationVisible(false, this);
                    break;
                case ANSWER:
                    DB dba = new DBSql(this, true);
                    Question questiona = dba.getQuestionById(questionIntent.questionID);

                    questiona.setPermutation(questionIntent.permutation);
                    PreviousQuestions preva;
                    if (questionIntent.previous == null) {
                        preva = new PreviousQuestions();
                    } else {
                        preva = questionIntent.previous;
                    }

                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                    boolean showCorrect = sharedPref.getBoolean("show_correct", true);

                    if (PreferencesDB.isNotificationVisible(this)) {
                        answerEvaluationNotification(questiona, questionIntent.answer, preva, showCorrect);
                    }
                    answerEvaluationWidgets(questiona, questionIntent.answer, preva, showCorrect);

                    dba.close();
                    break;
                default:
                    Log.d(TAG, "Unknown intent");
            }
        } catch (IOException ex) {
            Log.d(TAG, ex.toString());
        }
    }

    public void nextNotificationQuestion(Question question,
                                         PreviousQuestions prev) throws IOException {
        NotificationHelper.makeOrUpdateNotification(this, question, prev);
    }

    public void answerEvaluationNotification(Question question, int answer,
                                             PreviousQuestions previousQuestions,
                                             boolean showCorrect) throws IOException {
        NotificationHelper.makeOrUpdateNotification(this, question, answer, question.getCorrect(), previousQuestions, showCorrect);
    }

    public void answerEvaluationWidgets(Question question, int answer,
                                        PreviousQuestions previousQuestions,
                                        boolean showCorrect) throws IOException {
        loadWidgets(question, answer, question.getCorrect(), previousQuestions, showCorrect);
    }

    public void loadWidgets(Question question, int selected, int correct,
                            PreviousQuestions previousQuestions,
                            boolean showCorrect) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        ComponentName name = new ComponentName(this, WidgetProvider.class);
        int[] widgetsIDs = appWidgetManager.getAppWidgetIds(name);
        RemoteViews remoteView = makeRemoteViewsForWidget(this, question, selected, correct, previousQuestions, showCorrect);
        appWidgetManager.updateAppWidget(name, remoteView);
    }

    public static RemoteViews makeRemoteViewsForWidget(Context ctx,
                                                       Question question,
                                                       int selected, int correct,
                                                       PreviousQuestions previousQuestions,
                                                       boolean showCorrect) {

        RemoteViews rview = new RemoteViews(ctx.getPackageName(), R.layout.widget_layout);

        int RIDAnswer[] = {R.id.answerAW, R.id.answerBW, R.id.answerCW};
        int RIDImg[] = {R.id.imgAW, R.id.imgBW, R.id.imgCW};
        
        ViewHelper.fillView(rview, RIDAnswer, RIDImg, R.id.question, R.id.next, question, selected, correct, previousQuestions, showCorrect, ctx);

        return rview;
    }

    private void nextWidgetQuestion(Question question,
                                    PreviousQuestions previousQuestions) {
        loadWidgets(question, -1, -1, previousQuestions, true);
    }
}
