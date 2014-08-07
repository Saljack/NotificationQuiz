/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.saljack.notificationquiz;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import static android.content.Context.NOTIFICATION_SERVICE;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.RemoteViews;
import cz.saljack.notificationquiz.model.PreviousQuestions;
import cz.saljack.notificationquiz.model.Question;

/**
 *
 * @author saljack
 */
public class NotificationHelper {

    private static final String TAG = "NotificationHelper";

    public static void makeOrUpdateNotification(Context ctx, Question question, PreviousQuestions previousQuestions) {
        makeOrUpdateNotification(ctx, question, -1, -1, previousQuestions);
    }

    public static void makeOrUpdateNotification(Context ctx, Question question, int selected, int correct, PreviousQuestions previousQuestions) {
        RemoteViews rview = makeRemoteViews(ctx, question, selected, correct, previousQuestions);
        Notification.Builder notBuilder = new Notification.Builder(ctx).setContent(rview).setSmallIcon(R.drawable.ic_launcher);
        Notification not = notBuilder.build();
        not.bigContentView = rview;
        not.flags |= Notification.FLAG_NO_CLEAR;
        NotificationManager mng = (NotificationManager) ctx.getSystemService(NOTIFICATION_SERVICE);

        mng.notify(Constants.NOTIFICATION_ID, not);
    }

    public static RemoteViews makeRemoteViews(Context ctx, Question question, int selected, int correct, PreviousQuestions previousQuestions) {
        RemoteViews rview = new RemoteViews(ctx.getPackageName(), R.layout.notification_layout);
        rview.setTextViewText(R.id.question, question.getQuestion());

        int RIDAnswer[] = {R.id.answerA, R.id.answerB, R.id.answerC};
        int RIDImg[] = {R.id.imgA, R.id.imgB, R.id.imgC};

        int color = ctx.getResources().getColor(android.R.color.primary_text_dark);

        for (int i = 0; i < RIDAnswer.length; i++) {
            if (selected == -1) {
                PendingIntent penIntentService = QuizIntentBuilder.createPendingIntentForAnswer(question.getId(), AnswerEnum.values[i], previousQuestions, ctx);
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

            if (correct == i) {
                rview.setViewVisibility(RIDImg[i], View.VISIBLE);
            } else {
                rview.setViewVisibility(RIDImg[i], View.INVISIBLE);
            }
        }

        if (selected >= 0) {
            rview.setViewVisibility(R.id.next, View.VISIBLE);
            rview.setOnClickPendingIntent(R.id.next, QuizIntentBuilder.createPendingIntentForNext(question.getId(), previousQuestions, ctx));
        } else {
            rview.setViewVisibility(R.id.next, View.GONE);
        }

        rview.setOnClickPendingIntent(R.id.close, QuizIntentBuilder.createPendingIntentForClose(ctx));

        return rview;
    }
}
