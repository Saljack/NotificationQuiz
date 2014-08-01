/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.saljack.notificationquiz;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import cz.saljack.notificationquiz.model.DB;
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
public class XMLLoaderHelper {

    private static final String TAG = "XMLLoaderHelper";
    
    private static final String FILENAME = "questions.xml";
    
    /**
     * XML TAGS
     */
    private static final String QUESTIONS_TAG = "questions";
    private static final String QUESTION_TAG = "question";
    private static final String QUESTION_TEXT_TAG = "text";
    private static final String ANSWERS_TAG = "answers";
    private static final String ANSWER_TAG = "answer";
    private static final String CORRECT_ATTRIBUTE = "correct";
    private static final String TRUE_VALUE = "true";

    public static void loadXMLQuestions(Context ctx, DB db) {
        List<Question> questions = new ArrayList<Question>();
        InputStream open = null;
        try {
            open = openXMLFile(FILENAME, ctx);
            XmlPullParser parser = openParser(open);
            Question question = xmlParseQuestion(parser);
            while (question != null) {
                db.insert(question);
                question = xmlParseQuestion(parser);
            }

        } catch (IOException ex) {
            Log.d(TAG, ex.toString());
        } catch (XmlPullParserException ex) {
            Log.d(TAG, ex.toString());

        } finally {
            try {
                if (open != null) {
                    open.close();
                }
            } catch (IOException ex) {
                Log.d(TAG, ex.toString());
            }
        }
    }

    private static InputStream openXMLFile(String name, Context ctx) throws IOException {
        AssetManager assets = ctx.getAssets();
        InputStream open = assets.open(name);
        return open;
    }

    /**
     *
     * @param open
     * @return null if was found error
     */
    private static XmlPullParser openParser(InputStream open) throws XmlPullParserException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(open, "UTF-8");
        return parser;
    }

    public static Question xmlParseQuestion(XmlPullParser parser) throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();

        Question question = null;
        int ID = 0;

        int answerID = 0;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String name = parser.getName();
                if (name.equals(QUESTION_TAG)) {
                    question = new Question();
                    question.setId(ID);
                    ++ID;
                } else if (name.equals(QUESTION_TEXT_TAG)) {
                    eventType = parser.next();
                    if (eventType == XmlPullParser.TEXT) {
                        question.setQuestion(parser.getText());
                    } else {
                        Log.d(TAG, "Text question not found");
                    }
                } else if (name.equals(ANSWERS_TAG)) {
                    question.setAnswers(new String[3]);
                    answerID = 0;
                } else if (name.equals(ANSWER_TAG)) {
                    if (parser.getAttributeCount() > 0) {
                        if (parser.getAttributeName(0).equals(CORRECT_ATTRIBUTE)) {
                            if (parser.getAttributeValue(0).equals(TRUE_VALUE)) {
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
                if (name.equals(QUESTION_TAG)) {
                    parser.next();
                    return question;
                } else if (name.equals(ANSWER_TAG)) {
                    ++answerID;
                }
            }

            eventType = parser.next();

        }

        return null;
    }

}
