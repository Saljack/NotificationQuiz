/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.saljack.notificationquiz.model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 *
 * @author saljack
 */
public class PreferencesDB {

    public static final String PREFERENCES_NAME = "NotificationQuizPreferences";
    public static final String NOTIFICATION_VISIBLE = "NOTIFICATION_VISIBLE";

    public static boolean isNotificationVisible(Context ctx) {
        SharedPreferences pref = ctx.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        boolean isVisible = pref.getBoolean(NOTIFICATION_VISIBLE, true);
        return isVisible;
    }

    public static void setNotificationVisible(boolean isVisible, Context ctx) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFERENCES_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(NOTIFICATION_VISIBLE, isVisible);
        editor.commit();
    }

}
