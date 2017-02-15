package com.example.lehoanghan.appcalendar;

import com.firebase.client.Firebase;

public class User {
    private String jName;

    private String jMail;

    private String jPass;

    private String jId;

    public User() {
        super();
    }

    public void setjName(String jName) {
        this.jName = jName;
    }

    public void setjPass(String jPass) {
        this.jPass = jPass;
    }

    public void setjMail(String jMail) {
        this.jMail = jMail;
    }

    public void setjId(String jID) {
        this.jId = jID;
    }

    public String getjName() {
        return jName;
    }

    public String getjPass() {
        return jPass;
    }

    public String getjMail() {
        return jMail;
    }

    public String getjId() {
        return jId;
    }

    public void setValueChild() {
        Firebase firebase = LibraryClass.getsFirebase();
        firebase = firebase.child("User").child(getjMail());
        setjPass(null);
        setjId(null);
        firebase.setValue(this);
    }

}
