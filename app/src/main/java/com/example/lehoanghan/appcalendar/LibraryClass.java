package com.example.lehoanghan.appcalendar;

import com.firebase.client.Firebase;

/**
 * Created by lehoanghan on 4/18/2016.
 */
public final class LibraryClass {

    public LibraryClass() {
    }

    private static Firebase firebase;

    public static Firebase getFirebase() {
        if (firebase == null) {
            firebase = new Firebase("https://appcalendar.firebaseio.com/");
        }
        return (firebase);
    }

}
