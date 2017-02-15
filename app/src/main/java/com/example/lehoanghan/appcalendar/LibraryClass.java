package com.example.lehoanghan.appcalendar;

import com.firebase.client.Firebase;

/**
 * Created by lehoanghan on 4/18/2016.
 */
public final class LibraryClass {
    private static Firebase sFirebase;

    public LibraryClass() {
    }

    public static Firebase getsFirebase() {
        if (sFirebase == null) {
            sFirebase = new Firebase("https://appcalendar.firebaseio.com/");
        }
        return (sFirebase);
    }

}
