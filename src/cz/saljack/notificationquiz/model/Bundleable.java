/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.saljack.notificationquiz.model;

import android.os.Bundle;

/**
 *
 * @author saljack
 */
public interface Bundleable<T> {
    
    public Bundle toBundle();
}
