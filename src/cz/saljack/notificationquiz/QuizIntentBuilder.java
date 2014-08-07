/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.saljack.notificationquiz;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import cz.saljack.notificationquiz.model.PreviousQuestions;

/**
 *
 * @author saljack
 */
public class QuizIntentBuilder {

    public static final int NEXT_REQUEST_CODE = 1000;
    public static final int CLOSE_REQUEST_CODE = 1001;
    public static final int DUMMY_REQUEST_CODE = 11111;

    private final Intent intent;

    private final Context ctx;

    private int requestCode;

    private QuizIntentBuilder(Context context, Class cls) {
        intent = new Intent(context, cls);
        ctx = context;
        requestCode = -1;
    }

    public static QuizIntentBuilder Builder(Context context, Class cls) {
        return new QuizIntentBuilder(context, cls);
    }

    /**
     * Class is NotficationQuizService
     *
     * @param context
     * @return
     */
    public static QuizIntentBuilder Builder(Context context) {
        return new QuizIntentBuilder(context, NotificationQuizService.class);
    }

    public static QuizIntentBuilder BuilderDummy(Context context) {
        return new QuizIntentBuilder(context, DUMMY.class);
    }

    public static QuizIntentBuilder BuilderIntentForAnswer(int questionID, AnswerEnum answer, Context ctx) {
        QuizIntentBuilder builder = Builder(ctx).setQuestion(questionID).setAnswer(answer);
        return builder;
    }

    public static QuizIntentBuilder BuilderIntentForNext(int questionID, Context ctx) {
        QuizIntentBuilder builder = Builder(ctx).setQuestion(questionID).makeNext();
        return builder;
    }

    public static PendingIntent createPendingIntentForAnswer(int questionID, AnswerEnum answer, Context ctx) {
        QuizIntentBuilder builder = BuilderIntentForAnswer(questionID, answer, ctx);
        return builder.buildPendingIntent();
    }

    public static PendingIntent createPendingIntentForNext(int questionID, Context ctx) {
        QuizIntentBuilder builder = BuilderIntentForNext(questionID, ctx);
        return builder.buildPendingIntent();
    }

    public static PendingIntent createPendingIntentForClose(Context ctx) {
        QuizIntentBuilder builder = Builder(ctx).makeClose();
        return builder.buildPendingIntent();
    }

    public static PendingIntent createPendingIntentForDummy(Context ctx) {
        QuizIntentBuilder builder = BuilderDummy(ctx).makeDUMMY();
        return builder.buildPendingIntent();
    }

    public Intent build() {
        return intent;
    }

    public PendingIntent buildPendingIntent() {
        if (requestCode == DUMMY_REQUEST_CODE) {
            return PendingIntent.getBroadcast(ctx, DUMMY_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        return PendingIntent.getService(ctx, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public QuizIntentBuilder setQuestion(int questionID) {
        intent.putExtra(Constants.QUESTION, questionID);
        return this;
    }

    public QuizIntentBuilder makeNext() {
        intent.putExtra(Constants.NEXT, true);
        requestCode = NEXT_REQUEST_CODE;
        return this;
    }

    public QuizIntentBuilder setAnswer(AnswerEnum answer) {
        intent.putExtra(Constants.ANSWER, answer.getID());
        requestCode = answer.getID();
        return this;
    }

    public QuizIntentBuilder makeClose() {
        intent.putExtra(Constants.CLOSE, true);
        requestCode = CLOSE_REQUEST_CODE;
        return this;
    }

    public QuizIntentBuilder makeDUMMY() {
        requestCode = DUMMY_REQUEST_CODE;
        return this;
    }

    public QuizIntentBuilder setPreviousID(PreviousQuestions previous) {
        intent.putExtra(Constants.PREVIOUS, previous);
        return this;
    }

    public QuizIntentBuilder setExistsNotification(boolean exist) {
        intent.putExtra(Constants.EXIST_NOTIFICATION, exist);
        return this;
    }
}
