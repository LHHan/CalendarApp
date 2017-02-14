package com.example.lehoanghan.choosemenu;


import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.lehoanghan.appcalendar.LoginActivity;
import com.example.lehoanghan.appcalendar.R;
import com.example.lehoanghan.optionmenu.AboutActivity;
import com.example.lehoanghan.optionmenu.AccountActivity;
import com.example.lehoanghan.optionmenu.FindFriendActivity;
import com.example.lehoanghan.optionmenu.FriendAcceptActivity;
import com.example.lehoanghan.optionmenu.HomeActivity;
import com.example.lehoanghan.optionmenu.MyEventActivity;
import com.example.lehoanghan.optionmenu.MyFriendActivity;
import com.example.lehoanghan.optionmenu.NewEventActivity;
import com.example.lehoanghan.optionmenu.OldEventActivity;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;

public class Menu_Choose extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ListView lvMenu;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private CharSequence drawerTitle;
    private CharSequence title;

    private SearchView srvFindFriend;

    private String[] MenuTitle;
    private int[] MenuIcon = {R.drawable.ic_house, R.drawable.ic_account, R.drawable.ic_my_friend, R.drawable.ic_add_friend, R.drawable.ic_find_friend,
            R.drawable.ic_new_event, R.drawable.ic_my_event, R.drawable.ic_old_event, R.drawable.ic_about, R.drawable.ic_exit};
    private ArrayList<MenuItem> arrListMenuItem;
    private MenuAdapter menuAdapter;
    private ActionBar actionBar;

    private int fragmentHome = 0;
    private int fragmentAccount = 1;
    private int fragmentMyFriend = 3;
    private int fragmentFindFriend = 5;
    private int fragmentFriendAccept = 4;
    private int fragmentNewEvent = 7;
    private int fragmentMyEvent = 8;
    private int fragmentOldEvent = 9;
    private int fragmentAbout = 10;
    private int Fragment = fragmentHome;
    private int FragmentItem_addevent = 11;


    private static Fragment fragment = null;
    private Bundle bundle, bundlesetNameforHome, bundlePassMailtoFindAccept;
    private String setTitleItem = null;
    private MaterialCalendarView calendar;
    private String textDate;
    private Intent intentAddEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_choose);
        Init();
        setNameUser();
        if (savedInstanceState == null) {
            doItemListener(0);
        }

    }


    private void Init() {
        drawerTitle = getTitle();
        title = getTitle();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout_menu);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.hello, R.string.hello) {

            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                actionBar.setTitle(title);
                invalidateOptionsMenu();
            }


            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                actionBar.setTitle(drawerTitle);
                invalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        lvMenu = (ListView) findViewById(R.id.activity_menu_choose_lv_navigation);

        MenuTitle = getResources().getStringArray(R.array.my_data);

        arrListMenuItem = new ArrayList<MenuItem>();
        CreateListMenu();

        menuAdapter = new MenuAdapter(arrListMenuItem, getApplicationContext());
        lvMenu.setAdapter(menuAdapter);

        lvMenu.setOnItemClickListener(new ItemListener());

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        actionBarDrawerToggle.syncState();
        super.onPostCreate(savedInstanceState);
    }

    private static MenuInflater menuInflater;
    private static Menu menu;

    //Create options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuInflater = getMenuInflater();
        this.menu = menu;
        menuInflater.inflate(R.menu.menu_main, this.menu);
        if (menu != null)
            menu.clear();
        switch (Fragment) {
            case 0:
                menuInflater.inflate(R.menu.menu_home, menu);
                break;
            case 1:
                menuInflater.inflate(R.menu.menu_account, menu);
                break;
            case 3:
                menuInflater.inflate(R.menu.menu_my_friend, menu);
                break;
            case 4:
                menuInflater.inflate(R.menu.menu_friend_accept, menu);
                break;
            case 5:
                menuInflater.inflate(R.menu.menu_find_friend, menu);
                break;
            case 7:
                menuInflater.inflate(R.menu.menu_new_event, menu);
                break;
            case 8:
                menuInflater.inflate(R.menu.menu_my_event, menu);
                break;
            case 9:
                menuInflater.inflate(R.menu.menu_old_event, menu);
                break;
            case 10:
                menuInflater.inflate(R.menu.menu_about, menu);
                break;
        }
        return true;
    }


    //Option Item Select
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(lvMenu))
                drawerLayout.closeDrawer(lvMenu);
            else
                drawerLayout.openDrawer(lvMenu);
        }
//
//        switch (id) {
//            case (R.id.menu_about_back):
//                if (menu != null)
//                    menu.clear();
//                setNameUser();
//                fragment = new HomeActivity();
//                fragment.setArguments(bundlesetNameforHome);
//                setTitleItem = "HomeActivity";
//                actionBar.setDisplayShowHomeEnabled(true);
//                actionBar.setDisplayHomeAsUpEnabled(true);
//                Fragment = fragmentHome;
//                break;
//            case R.id.menu_find_friend_ab_search:
//                srvFindFriend = (SearchView) findViewById(R.id.activity_find_friend_srv_search);
//                if (srvFindFriend.getVisibility() == View.INVISIBLE)
//                    srvFindFriend.setVisibility(View.VISIBLE);
//                else
//                    srvFindFriend.setVisibility(View.INVISIBLE);
//                break;
//        }
        if (fragment != null) {
            FragmentManager fManager = getFragmentManager();
            fManager.beginTransaction().replace(R.id.activity_menu_choose_fl_navigation, fragment).commit();
            setTitle(setTitleItem);
        } else {
            Log.e("Main", "error");
        }

        switch (Fragment) {
            case 0:
                if (menu != null)
                    menu.clear();
                menuInflater.inflate(R.menu.menu_home, menu);
                break;
            case 11:
                if (menu != null)
                    menu.clear();
                menuInflater.inflate(R.menu.menu_add_event, menu);
                break;
        }


        return super.onOptionsItemSelected(item);
    }


    //Listen ic_new_event Button
    private class ItemListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            doItemListener(position);
        }
    }


    //Change among Fragments
    public void doItemListener(int position) {
        if (menu != null)
            menu.clear();
        switch (position) {
            case 0:
                Fragment = fragmentHome;
                if (menuInflater != null) {
                    menuInflater.inflate(R.menu.menu_home, menu);
                }
                setNameUser();
                fragment = new HomeActivity();
                fragment.setArguments(bundlesetNameforHome);
                break;
            case 1:
                Fragment = fragmentAccount;
                PassDatatoFind_Accept_Friend();
                // menuInflater.inflate(R.menu.menu_account,menu);
                fragment = new AccountActivity();
                fragment.setArguments(bundlePassMailtoFindAccept);
                break;
            case 3:
                Fragment = fragmentMyFriend;
                // menuInflater.inflate(R.menu.menu_my_friend,menu);
                PassDatatoFind_Accept_Friend();
                fragment = new MyFriendActivity();
                fragment.setArguments(bundlePassMailtoFindAccept);
                break;
            case 4:
                Fragment = fragmentFriendAccept;
                // menuInflater.inflate(R.menu.menu_friend_accept,menu);
                PassDatatoFind_Accept_Friend();
                fragment = new FriendAcceptActivity();
                fragment.setArguments(bundlePassMailtoFindAccept);
                break;
            case 5:
                Fragment = fragmentFindFriend;
                // menuInflater.inflate(R.menu.menu_find_friend,menu);
                PassDatatoFind_Accept_Friend();
                fragment = new FindFriendActivity();
                fragment.setArguments(bundlePassMailtoFindAccept);
                break;
            case 7:
                Fragment = fragmentNewEvent;
                PassDatatoFind_Accept_Friend();
                //menuInflater.inflate(R.menu.menu_new_event,menu);
                fragment = new NewEventActivity();
                fragment.setArguments(bundlePassMailtoFindAccept);
                break;
            case 8:
                Fragment = fragmentMyEvent;
                PassDatatoFind_Accept_Friend();
                // menuInflater.inflate(R.menu.menu_old_event,menu);
                fragment = new MyEventActivity();
                fragment.setArguments(bundlePassMailtoFindAccept);
                break;
            case 9:
                Fragment = fragmentOldEvent;
                PassDatatoFind_Accept_Friend();
                // menuInflater.inflate(R.menu.menu_old_event,menu);
                fragment = new OldEventActivity();
                fragment.setArguments(bundlePassMailtoFindAccept);
                break;
            case 10:
                Fragment = fragmentAbout;
                //  menuInflater.inflate(R.menu.menu_about,menu);
                fragment = new AboutActivity();
                break;
            case 11:
                Thoat();
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fManager = getFragmentManager();
            fManager.beginTransaction().replace(R.id.activity_menu_choose_fl_navigation, fragment).commit();
            lvMenu.setItemChecked(position, true);
            lvMenu.setSelection(position);

            setTitle(MenuTitle[position]);
            title = getTitle();
            drawerLayout.closeDrawer(lvMenu);

        } else {
            Log.e("Main", "error");
        }
    }


    public void CreateListMenu() {
        arrListMenuItem.add(new MenuItem(MenuTitle[0], MenuIcon[0]));
        arrListMenuItem.add(new MenuItem(MenuTitle[1], MenuIcon[1]));
        arrListMenuItem.add(new MenuItem(MenuTitle[2]));
        arrListMenuItem.add(new MenuItem(MenuTitle[3], MenuIcon[2]));
        arrListMenuItem.add(new MenuItem(MenuTitle[4], MenuIcon[3]));
        arrListMenuItem.add(new MenuItem(MenuTitle[5], MenuIcon[4]));
        arrListMenuItem.add(new MenuItem(MenuTitle[6]));
        arrListMenuItem.add(new MenuItem(MenuTitle[7], MenuIcon[5]));
        arrListMenuItem.add(new MenuItem(MenuTitle[8], MenuIcon[6]));
        arrListMenuItem.add(new MenuItem(MenuTitle[9], MenuIcon[7]));
        arrListMenuItem.add(new MenuItem(MenuTitle[10], MenuIcon[8]));
        arrListMenuItem.add(new MenuItem(MenuTitle[11], MenuIcon[9]));

    }


    public void Thoat() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Accept");
        builder.setMessage("You want to exit?");
        builder.setIcon(R.drawable.ic_warning);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//                dialog.dismiss();
//                finish();
//                  System.exit(0);
//                moveTaskToBack(true);
                Intent intent = new Intent(Menu_Choose.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();

    }


    @Override
    public void onBackPressed() {
        switch (Fragment) {
            case 0:
                Thoat();
                break;
            case 1:
                doItemListener(0);
                break;
            case 2:
                doItemListener(0);
                break;
            case 3:
                doItemListener(0);
                break;
            case 4:
                doItemListener(0);
                break;
            case 5:
                doItemListener(0);
                break;
            case 6:
                doItemListener(0);
                break;
            case 7:
                doItemListener(0);
                break;
            case 8:
                doItemListener(0);
                break;
            case 9:
                doItemListener(0);
                break;

        }
    }

    public void setNameUser() {
        Bundle bundle = getIntent().getExtras();
        String NameUser = "", MailUser = "";
        String NameUserfromAddEvent = getIntent().getStringExtra("NameUserfromAddEvent");
        if (bundle != null) {
            NameUser = bundle.getString("NameUser").toLowerCase();
            MailUser = bundle.getString("MailUser").toLowerCase();
        }
        bundlesetNameforHome = new Bundle();
        bundlesetNameforHome.putString("MailforHome", MailUser);
        if (NameUser != null)
            bundlesetNameforHome.putString("NameforHome", NameUser);
        else
            bundlesetNameforHome.putString("NameforHome", NameUserfromAddEvent);

    }

    //pass MailUSER to FindName for don't give my Mail into list
    public void PassDatatoFind_Accept_Friend() {
        Bundle bundle = getIntent().getExtras();
        String Mailuser = "";
        String Nameuser = "";
        if (bundle != null) {
            Mailuser = bundle.getString("MailUser");
            Nameuser = bundle.getString("NameUser");
        }
        bundlePassMailtoFindAccept = new Bundle();
        bundlePassMailtoFindAccept.putString("MailforFindFriend", Mailuser);
        bundlePassMailtoFindAccept.putString("NameforFindFriend", Nameuser);
    }


}




