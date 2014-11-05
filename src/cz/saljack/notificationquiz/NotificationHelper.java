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

    public static void makeOrUpdateNotification(Context ctx, Question question,
                                                PreviousQuestions previousQuestions) {
        makeOrUpdateNotification(ctx, question, -1, -1, previousQuestions, false);
    }

    public static void makeOrUpdateNotification(Context ctx, Question question,
                                                int selected, int correct,
                                                PreviousQuestions previousQuestions,
                                                boolean showCorrect) {
        RemoteViews rview = makeRemoteViews(ctx, question, selected, correct, previousQuestions, showCorrect);
        Notification.Builder notBuilder = new Notification.Builder(ctx).setContent(rview).setSmallIcon(R.drawable.ic_launcher);
        Notification not = notBuilder.build();
        not.bigContentView = rview;
        not.flags |= Notification.FLAG_NO_CLEAR;
        NotificationManager mng = (NotificationManager) ctx.getSystemService(NOTIFICATION_SERVICE);

        mng.notify(Constants.NOTIFICATION_ID, not);
    }

    public static RemoteViews makeRemoteViews(Context ctx, Question question,
                                              int selected, int correct,
                                              PreviousQuestions previousQuestions,
                                              boolean showCorrect) {
        RemoteViews rview = new RemoteViews(ctx.getPackageName(), R.layout.notification_layout);

        int RIDAnswer[] = {R.id.answerA, R.id.answerB, R.id.answerC};
        int RIDImg[] = {R.id.imgA, R.id.imgB, R.id.imgC};
        
        ViewHelper.fillView(rview, RIDAnswer, RIDImg, R.id.question, R.id.next, question, selected, correct, previousQuestions, showCorrect, ctx);

        rview.setOnClickPendingIntent(R.id.close, QuizIntentBuilder.createPendingIntentForClose(ctx));

        return rview;
    }
}
