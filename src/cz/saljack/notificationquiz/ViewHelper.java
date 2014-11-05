/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.saljack.notificationquiz;

import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.RemoteViews;
import cz.saljack.notificationquiz.model.PreviousQuestions;
import cz.saljack.notificationquiz.model.Question;

/**
 *
 * @author saljack
 */
public class ViewHelper {

    public static void fillView(RemoteViews rview,
                                  int[] RIDAnswer,
                                  int[] RIDImg,
                                  int questionID,
                                  int nextID,
                                  Question question,
                                  int selected,
                                  int correct,
                                  PreviousQuestions previousQuestions,
                                  boolean showCorrect,
                                  Context ctx) {

        rview.setTextViewText(questionID, question.getQuestion());

        int color = ctx.getResources().getColor(android.R.color.primary_text_dark);

        for (int i = 0; i < RIDAnswer.length; i++) {
            final int p = question.getPermutation()[i];
            if (selected == -1) {
                PendingIntent penIntentService = QuizIntentBuilder.createPendingIntentForAnswer(
                        question.getId(),
                        AnswerEnum.values[p],
                        previousQuestions,
                        question.getPermutation(),
                        ctx
                );

                rview.setOnClickPendingIntent(RIDAnswer[i], penIntentService);
            } else {
                PendingIntent pi = QuizIntentBuilder.createPendingIntentForDummy(ctx);
                rview.setOnClickPendingIntent(RIDAnswer[i], pi);
            }
            rview.setTextViewText(RIDAnswer[i], question.getAnswers()[p]);
            if (selected == p) {
                if (selected == correct) {
                    rview.setTextColor(RIDAnswer[i], Color.GREEN);
                } else {
                    rview.setTextColor(RIDAnswer[i], Color.RED);
                }
            } else {
                rview.setTextColor(RIDAnswer[i], color);
            }

            if (p == correct) {

                if (showCorrect || selected == p) {
                    rview.setViewVisibility(RIDImg[i], View.VISIBLE);
                } else {
                    rview.setViewVisibility(RIDImg[i], View.INVISIBLE);
                }
            } else {
                rview.setViewVisibility(RIDImg[i], View.INVISIBLE);
            }
        }

        if (selected >= 0) {
            rview.setViewVisibility(nextID, View.VISIBLE);
            rview.setOnClickPendingIntent(nextID, QuizIntentBuilder.createPendingIntentForNext(question.getId(), previousQuestions, ctx));
        } else {
            rview.setViewVisibility(nextID, View.GONE);
        }
    }
}
