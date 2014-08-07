/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.saljack.notificationquiz.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;

/**
 *
 * @author saljack
 */
public class WidgetProvider extends AppWidgetProvider {
    
    private static final String TAG = "WidgetProvider";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int id : appWidgetIds) {
            Log.d(TAG, "Widget id "+ id +" is updated");    
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

}
