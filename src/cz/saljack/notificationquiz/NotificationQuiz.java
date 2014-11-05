package cz.saljack.notificationquiz;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import cz.saljack.notificationquiz.model.Game;
import cz.saljack.notificationquiz.model.GameMode;
import cz.saljack.notificationquiz.model.PreferencesDB;
import cz.saljack.notificationquiz.settings.SettingsActivity;

public class NotificationQuiz extends Activity {

    private static final String TAG = "NotificationQuiz";

    public static final int ALL_LOADED_WHAT = 0;
    public static final int LOADED_QUESTION_WHAT = 1;

    private ProgressDialog progressDialog;

    private int loaded = 0;

    private final Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == ALL_LOADED_WHAT) {
                progressDialog.dismiss();
                Log.d(TAG, "XML loaded");
                Intent intent = QuizIntentBuilder.Builder(NotificationQuiz.this).makeNext().build();
                startService(intent);
                Toast toast = Toast.makeText(NotificationQuiz.this, getString(R.string.loaded_question) + " " + loaded, Toast.LENGTH_SHORT);
                toast.show();
            } else if (msg.what == LOADED_QUESTION_WHAT) {
                ++loaded;
                progressDialog.setMessage(getString(R.string.loading_questions) + "\n" + loaded);
            }
        }

    };

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        reloadQuestion(null);
    }

    public void makeNotification(View view) {
        PreferencesDB.setNotificationVisible(true, this);
        Intent intent = QuizIntentBuilder.Builder(NotificationQuiz.this).makeNext().build();
        startService(intent);
    }

    public void survivalClicked(View view) {
        PreferencesDB.setNotificationVisible(true, this);
        Intent intent = QuizIntentBuilder.Builder(NotificationQuiz.this).makeNext().setGame(new Game(GameMode.SURVIVAL)).build();
        startService(intent);
    }

    public void millionaireClicked(View view) {
        PreferencesDB.setNotificationVisible(true, this);
        Intent intent = QuizIntentBuilder.Builder(NotificationQuiz.this).makeNext().build();
        startService(intent);
    }

    public void levelsClicked(View view) {
        PreferencesDB.setNotificationVisible(true, this);
        Intent intent = QuizIntentBuilder.Builder(NotificationQuiz.this).makeNext().build();
        startService(intent);
    }

    public void freemodeClicked(View view) {
        PreferencesDB.setNotificationVisible(true, this);
        Intent intent = QuizIntentBuilder.Builder(NotificationQuiz.this).makeNext().build();
        startService(intent);
    }

    public void reloadQuestion(View view) {
        loaded = 0;
        Thread t = new Thread(new LoadQuestionThread(handler, this));
        progressDialog = ProgressDialog.show(this, getString(R.string.loading), getString(R.string.loading_questions));
        t.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        Log.d(TAG, "Settings selected" + R.id.action_settings + " " + featureId);
        switch (item.getItemId()) {
            case R.id.action_settings:
                Log.d(TAG, "Settings selected");
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onMenuItemSelected(featureId, item);
        }
    }
}
