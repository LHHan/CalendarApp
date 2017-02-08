package com.example.lehoanghan;

import java.io.Serializable;


public class EventValue implements Serializable {
    private String NameEvent;
    private String DateFrom;
    private String TimeFrom;
    private String DateTo;
    private String TimeTo;
    private String Description;
    private String Place;
    private String FriendInvite;
    private String Alarm;
    private String Repeat;
    private int Check;
    public EventValue(){}
    public EventValue(String DF){
        DateFrom=DF;
    }
    public EventValue(String N, String DF, String TF, String DT, String TT, String D, String P, String F,String A, String R ){
        NameEvent=N;
        DateFrom=DF;
        TimeFrom=TF;
        DateTo=DT;
        TimeTo=TT;
        Description=D;
        Place=P;
        FriendInvite=F;
        Alarm=A;
        Repeat=R;
    }

    public EventValue(String N, String DF, String TF, String DT, String TT, String D, String P, String F,String A, String R , int C){
        NameEvent=N;
        DateFrom=DF;
        TimeFrom=TF;
        DateTo=DT;
        TimeTo=TT;
        Description=D;
        Place=P;
        FriendInvite=F;
        Alarm=A;
        Repeat=R;
        Check=C;
    }
    public  String getNameEvent(){
        return NameEvent;
    }

    public String getDateFrom() {
        return DateFrom;
    }

    public String getTimeFrom() {
        return TimeFrom;
    }

    public String getDateTo() {
        return DateTo;
    }

    public String getTimeTo() {
        return TimeTo;
    }

    public String getDescription() {
        return Description;
    }

    public String getPlace() {
        return Place;
    }

    public String getFriendInvite() {
        return FriendInvite;
    }

    public String getAlarm() {
        return Alarm;
    }

    public String getRepeat() {
        return Repeat;
    }

    public void setNameEvent(String nameEvent) {
        NameEvent = nameEvent;
    }

    public void setDateFrom(String dateFrom) {
        DateFrom = dateFrom;
    }

    public void setTimeFrom(String timeFrom) {
        TimeFrom = timeFrom;
    }

    public void setDateTo(String dateTo) {
        DateTo = dateTo;
    }

    public void setTimeTo(String timeTo) {
        TimeTo = timeTo;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public void setFriendInvite(String friendInvite) {
        FriendInvite = friendInvite;
    }

    public void setAlarm(String alarm) {
        Alarm = alarm;
    }

    public void setRepeat(String repeat) {
        Repeat = repeat;
    }

    public int getCheck() {
        return Check;
    }

    public void setCheck(int check) {
        Check = check;
    }
}
