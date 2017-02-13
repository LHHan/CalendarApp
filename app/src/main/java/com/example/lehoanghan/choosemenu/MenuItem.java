package com.example.lehoanghan.choosemenu;


public class MenuItem {

    private final int itemTitle = 0;
    private final int itemBody = 1;

    private int itemType = itemTitle;
    private String title;
    private int icon;

    public MenuItem(String Title) {
        this.title = Title;
        itemType = itemTitle;
    }

    public MenuItem(String Title, int Icon) {
        this.title = Title;
        this.icon = Icon;
        itemType = itemBody;
    }


    public int getItemType() {
        return itemType;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}

