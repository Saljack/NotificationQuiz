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
import android.content.res.AssetManager;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import cz.saljack.notificationquiz.model.Question;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 *
 * @author saljack
 */
public class NotificationHelper {

    private static final String TAG = "NotificationHelper";

    public static Intent createIntentForAnswer(int questionID, AnswerEnum answer, Context ctx) {
        Intent intentService = new Intent(ctx, NotificationQuizService.class);
        intentService = intentService.putExtra(Constants.ANSWER, answer.getID());
        intentService = intentService.putExtra(Constants.QUESTION, questionID);
        return intentService;
    }

    public static PendingIntent createPendingIntentForAnswer(int questionID, AnswerEnum answer, Context ctx) {
        Intent intent = createIntentForAnswer(questionID, answer, ctx);
        PendingIntent penIntentService = PendingIntent.getService(ctx, answer.getID(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return penIntentService;
    }

    public static PendingIntent createPendingIntentForNext(int questionID, Context ctx) {
        Intent intent = new Intent(ctx, NotificationQuizService.class);
        intent.putExtra(Constants.QUESTION, questionID);
        intent.putExtra(Constants.NEXT, true);
        PendingIntent penIntentService = PendingIntent.getService(ctx, 1000, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return penIntentService;
    }

    public static PendingIntent createPendingIntentForClose(Context ctx) {
        Intent intent = new Intent(ctx, NotificationQuizService.class);
        intent.putExtra(Constants.CLOSE, true);
        PendingIntent penIntentService = PendingIntent.getService(ctx, 1001, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return penIntentService;
    }

    public static void makeOrUpdateNotification(Context ctx, Question question) {
        makeOrUpdateNotification(ctx, question, -1, -1);
    }

    public static void makeOrUpdateNotification(Context ctx, Question question, int selected, int correct) {
        RemoteViews rview = makeRemoteViews(ctx, question, selected, correct);
        Notification.Builder notBuilder = new Notification.Builder(ctx).setContent(rview).setSmallIcon(R.drawable.ic_launcher);
        Notification not = notBuilder.build();
        not.bigContentView = rview;
        not.flags |= Notification.FLAG_NO_CLEAR;
        NotificationManager mng = (NotificationManager) ctx.getSystemService(NOTIFICATION_SERVICE);

        mng.notify(Constants.NOTIFICATION_ID, not);
    }

    public static RemoteViews makeRemoteViews(Context ctx, Question question, int selected, int correct) {
        RemoteViews rview = new RemoteViews(ctx.getPackageName(), R.layout.notification_layout);
        rview.setTextViewText(R.id.question, question.getQuestion());

        int RIDAnswer[] = {R.id.answerA, R.id.answerB, R.id.answerC};
        int RIDImg[] = {R.id.imgA, R.id.imgB, R.id.imgC};

        int color = ctx.getResources().getColor(android.R.color.primary_text_dark);

        for (int i = 0; i < RIDAnswer.length; i++) {
            if (selected == -1) {
                PendingIntent penIntentService = createPendingIntentForAnswer(question.getId(), AnswerEnum.values[i], ctx);
                rview.setOnClickPendingIntent(RIDAnswer[i], penIntentService);
            }
            rview.setTextViewText(RIDAnswer[i], question.getAnswers()[i]);
            if (selected == i) {
                rview.setTextColor(RIDAnswer[i], Color.MAGENTA);
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
            rview.setOnClickPendingIntent(R.id.next, createPendingIntentForNext(question.getId(), ctx));
        } else {
            rview.setViewVisibility(R.id.next, View.GONE);
        }

        rview.setOnClickPendingIntent(R.id.close, createPendingIntentForClose(ctx));

        return rview;
    }

//    public static List<Question> db() {
//        List<Question> questions = new ArrayList<Question>(2);
//        Question q = new Question(0, "Kdo tohle naprogramoval?", new String[]{"Tomas Poledny", "Franta Hruska", "Petr Vocasek"}, 0);
//        questions.add(q);
//
//        q = new Question(1, "Aplikace bezi na?", new String[]{"Windows", "Android", "iOS"}, 1);
//        questions.add(q);
//
//        return questions;
//    }
    public static List<Question> loadXMLQuestions(Context ctx) {
        List<Question> questions = new ArrayList<Question>();

        AssetManager assets = ctx.getAssets();

        try {
            InputStream open = assets.open("questions.xml");
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(open, "UTF-8");
            int eventType = parser.getEventType();

            Question question = null;
            int ID = 0;

            int answerID = 0;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String name = parser.getName();
                    if (name.equals("question")) {
                        question = new Question();
                        question.setId(ID);
                        ++ID;
                    } else if (name.equals("text")) {
                        eventType = parser.next();
                        if (eventType == XmlPullParser.TEXT) {
                            question.setQuestion(parser.getText());
                        } else {
                            Log.d(TAG, "Text question not found");
                        }
                    } else if (name.equals("answers")) {
                        question.setAnswers(new String[3]);
                        answerID = 0;
                    } else if (name.equals("answer")) {
                        if (parser.getAttributeCount() > 0) {
                            if (parser.getAttributeName(0).equals("correct")) {
                                if (parser.getAttributeValue(0).equals("true")) {
                                    question.setCorrect(answerID);
                                }
                            }
                        }
                        eventType = parser.next();
                        if (eventType == XmlPullParser.TEXT) {
                            question.getAnswers()[answerID] = parser.getText();
                        } else {
                            Log.d(TAG, "Text answer not found");
                        }
                    }

                }

                if (eventType == XmlPullParser.END_TAG) {
                    String name = parser.getName();
                    if (name.equals("question")) {
                        //TODO validate question
                        questions.add(question);
                    } else if (name.equals("answer")) {
                        ++answerID;
                    }
                }

                eventType = parser.next();

            }
        } catch (IOException ex) {
            Log.d(TAG, "XML Questions was not loaded.");
        } catch (XmlPullParserException ex) {
            Log.d(TAG, ex.toString());

        }
        return questions;
    }
}
