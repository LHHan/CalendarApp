package com.example.lehoanghan.choosemenu;


public class MenuItem {

    private final int Item_Title = 0;
    private final int Item_Body = 1;

    private int Item_Type = Item_Title;
    private String Title;
    private int Icon;

    public MenuItem(String Title) {
        this.Title = Title;
        Item_Type = Item_Title;
    }

    public MenuItem(String Title, int Icon) {
        this.Title = Title;
        this.Icon = Icon;
        Item_Type = Item_Body;
    }


    public int getItem_Type() {
        return Item_Type;
    }


    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getIcon() {
        return Icon;
    }

    public void setIcon(int icon) {
        Icon = icon;
    }
}

