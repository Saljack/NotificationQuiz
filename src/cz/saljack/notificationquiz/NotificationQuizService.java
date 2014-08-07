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
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import cz.saljack.notificationquiz.model.DB;
import cz.saljack.notificationquiz.model.DBSql;
import cz.saljack.notificationquiz.model.PreferencesDB;
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
        QuestionIntent msg = new QuestionIntent(intent);
        try {

            switch (msg.type) {
                case NEXT:
                    DB db = new DBSql(this, true);

                    Question question = db.getRandomQuestion();

                    if (PreferencesDB.isNotificationVisible(this)) {
                        nextNotificationQuestion(question);
                    }
                    nextWidgetQuestion(question);

                    db.close();
                    break;
                case CLOSE:
                    NotificationManager mng = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    mng.cancel(Constants.NOTIFICATION_ID);
                    PreferencesDB.setNotificationVisible(false, this);
                    break;
                case ANSWER:
                    DB dba = new DBSql(this, true);
                    Question questiona = dba.getQuestionById(msg.questionID);

                    if (PreferencesDB.isNotificationVisible(this)) {
                        answerEvaluationNotification(questiona, msg.answer);
                    }
                    answerEvaluationWidgets(questiona, msg.answer);

                    dba.close();
                    break;
                default:
                    Log.d(TAG, "Unknown intent");
            }
        } catch (IOException ex) {
            Log.d(TAG, ex.toString());
        }
    }

    public void nextNotificationQuestion(Question question) throws IOException {
        NotificationHelper.makeOrUpdateNotification(this, question);
    }

    public void answerEvaluationNotification(Question question, int answer) throws IOException {
        NotificationHelper.makeOrUpdateNotification(this, question, answer, question.getCorrect());
    }

    public void answerEvaluationWidgets(Question question, int answer) throws IOException {
        loadWidgets(question, answer, question.getCorrect());
    }

    public void loadWidgets(Question question, int selected, int correct) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        ComponentName name = new ComponentName(this, WidgetProvider.class);
        int[] widgetsIDs = appWidgetManager.getAppWidgetIds(name);
        RemoteViews remoteView = makeRemoteViewsForWidget(this, question, selected, correct);
//        for (int id : widgetsIDs) {
        appWidgetManager.updateAppWidget(name, remoteView);
//        }

    }

    public static RemoteViews makeRemoteViewsForWidget(Context ctx, Question question, int selected, int correct) {

        RemoteViews rview = new RemoteViews(ctx.getPackageName(), R.layout.widget_layout);
        rview.setTextViewText(R.id.questionW, question.getQuestion());

        int RIDAnswer[] = {R.id.answerAW, R.id.answerBW, R.id.answerCW};
        int RIDImg[] = {R.id.imgAW, R.id.imgBW, R.id.imgCW};

        int color = ctx.getResources().getColor(android.R.color.primary_text_dark);

        for (int i = 0; i < RIDAnswer.length; i++) {
            if (selected == -1) {
                PendingIntent penIntentService = QuizIntentBuilder.createPendingIntentForAnswer(question.getId(), AnswerEnum.values[i], ctx);
                rview.setOnClickPendingIntent(RIDAnswer[i], penIntentService);
            } else {
                PendingIntent pi = QuizIntentBuilder.createPendingIntentForDummy(ctx);
                rview.setOnClickPendingIntent(RIDAnswer[i], pi);
            }
            rview.setTextViewText(RIDAnswer[i], question.getAnswers()[i]);
            if (selected == i) {
                if (selected == correct) {
                    rview.setTextColor(RIDAnswer[i], Color.GREEN);
                } else {
                    rview.setTextColor(RIDAnswer[i], Color.RED);
                }
            } else {
                rview.setTextColor(RIDAnswer[i], color);
            }

            if (i == correct) {
                rview.setViewVisibility(RIDImg[i], View.VISIBLE);
            } else {
                rview.setViewVisibility(RIDImg[i], View.INVISIBLE);
            }
        }

        if (selected >= 0) {
            rview.setViewVisibility(R.id.nextW, View.VISIBLE);
            rview.setOnClickPendingIntent(R.id.nextW, QuizIntentBuilder.createPendingIntentForNext(question.getId(), ctx));
        } else {
            rview.setViewVisibility(R.id.nextW, View.GONE);
        }

        rview.setOnClickPendingIntent(R.id.close, QuizIntentBuilder.createPendingIntentForClose(ctx));

        return rview;
    }

    private void nextWidgetQuestion(Question question) {
        loadWidgets(question, -1, -1);
    }
}
