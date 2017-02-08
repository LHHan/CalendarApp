package com.example.lehoanghan;

public class UserFriend {
    protected String Name;
    protected String Mail;

    public UserFriend() {}
    public UserFriend(String _Name, String _Mail)
    {
        Name=_Name;
        Mail=_Mail;
    }

    public String getName(){
        return Name;
    }

    public String getMail(){
        return Mail;
    }

}
