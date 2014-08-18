/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.saljack.notificationquiz.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author saljack
 */
public class PreviousQuestions implements Parcelable {

    public static final String TAG = "PreviousQuestions";

    public static int MAX_PREVIOUS_SIZE = 20;

    private final LinkedList<Integer> previous;

    public static final Parcelable.Creator<PreviousQuestions> CREATOR = new Creator<PreviousQuestions>() {

        public PreviousQuestions createFromParcel(Parcel in) {
            PreviousQuestions ret = new PreviousQuestions();
            in.readList(ret.getPrevious(), null);
            return ret;
        }

        public PreviousQuestions[] newArray(int size) {
            return new PreviousQuestions[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flag) {
        out.writeList(previous);
    }

    public PreviousQuestions() {
        previous = new LinkedList<Integer>();

    }

    public void add(int id) {
        previous.add(id);
        if (previous.size() > MAX_PREVIOUS_SIZE) {
            previous.poll();
        }
    }

    public List<Integer> getPrevious() {
        return previous;
    }

    @Override
    public String toString() {
//        Log.d(TAG, previous.toString());
        String ret;
        if (previous.size() > 0) {
            ret = previous.toString();
            ret = "(" + ret.subSequence(1, ret.length() - 1) + ")";
        }else{
            ret = "()";
        }
        return ret;
    }

}
