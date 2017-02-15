package com.example.lehoanghan;

import java.io.Serializable;

public class EventValue implements Serializable {
    private String nameEvent;

    private String dateFrom;

    private String timeFrom;

    private String dateTo;

    private String timeTo;

    private String description;

    private String place;

    private String friendInvite;

    private String alarm;

    private String repeat;

    private int check;

    public EventValue() {
    }

    public EventValue(String DF) {
        dateFrom = DF;
    }

    public EventValue(String N, String DF, String TF, String DT, String TT,
                      String D, String P, String F, String A, String R) {
        nameEvent = N;
        dateFrom = DF;
        timeFrom = TF;
        dateTo = DT;
        timeTo = TT;
        description = D;
        place = P;
        friendInvite = F;
        alarm = A;
        repeat = R;
    }

    public EventValue(String N, String DF, String TF, String DT, String TT,
                      String D, String P, String F, String A, String R, int C) {
        nameEvent = N;
        dateFrom = DF;
        timeFrom = TF;
        dateTo = DT;
        timeTo = TT;
        description = D;
        place = P;
        friendInvite = F;
        alarm = A;
        repeat = R;
        check = C;
    }

    public String getNameEvent() {
        return nameEvent;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public String getDescription() {
        return description;
    }

    public String getPlace() {
        return place;
    }

    public String getFriendInvite() {
        return friendInvite;
    }

    public String getAlarm() {
        return alarm;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setNameEvent(String nameEvent) {
        this.nameEvent = nameEvent;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setFriendInvite(String friendInvite) {
        this.friendInvite = friendInvite;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }
}
