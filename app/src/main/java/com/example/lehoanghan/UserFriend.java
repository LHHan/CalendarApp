package com.example.lehoanghan;

public class UserFriend {
    protected String name;

    protected String mail;

    public UserFriend() {
    }

    public UserFriend(String _Name, String _Mail) {
        name = _Name;
        mail = _Mail;
    }

    public String getName() {
        return name;
    }

    public String getMail() {
        return mail;
    }

}
