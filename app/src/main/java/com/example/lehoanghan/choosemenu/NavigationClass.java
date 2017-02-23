package com.example.lehoanghan.choosemenu;

public class NavigationClass {

    private final int itemTitle = 0;

    private final int itemBody = 1;

    private int itemType = itemTitle;

    private String strTitle;

    private int intIcon;

    public NavigationClass(String Title) {
        this.strTitle = Title;
        itemType = itemTitle;
    }

    public NavigationClass(String Title, int Icon) {
        this.strTitle = Title;
        this.intIcon = Icon;
        itemType = itemBody;
    }

    public int getItemType() {
        return itemType;
    }

    public String getTitle() {
        return strTitle;
    }

    public void setTitle(String title) {
        this.strTitle = title;
    }

    public int getIcon() {
        return intIcon;
    }

    public void setIcon(int icon) {
        this.intIcon = icon;
    }
}

