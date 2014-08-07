/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.saljack.notificationquiz.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import cz.saljack.notificationquiz.R;

/**
 *
 * @author saljack
 */
public class Settings extends PreferenceFragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //To change body of generated methods, choose Tools | Templates.
        addPreferencesFromResource(R.xml.settings);
    }
    
    
    
}
