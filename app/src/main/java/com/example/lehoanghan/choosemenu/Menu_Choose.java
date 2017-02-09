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
import com.example.lehoanghan.optionmenu.FriendAccept;
import com.example.lehoanghan.optionmenu.HomeActivity;
import com.example.lehoanghan.optionmenu.MyEvent;
import com.example.lehoanghan.optionmenu.MyFriend;
import com.example.lehoanghan.optionmenu.NewEvent;
import com.example.lehoanghan.optionmenu.OldEvent;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;

public class Menu_Choose extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ListView lstMenu;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private CharSequence DrawerTitle;
    private CharSequence Title;

    private SearchView srvFindFriend;

    private String[] MenuTitle;
    private int[] MenuIcon = {R.drawable.house, R.drawable.account, R.drawable.myfriend, R.drawable.addfriend, R.drawable.findfriend,
            R.drawable.event, R.drawable.myevent, R.drawable.oldevent, R.drawable.about, R.drawable.exitout};
    private ArrayList<MenuItem> arrayList_menuItem;
    private MenuAdapter menuAdapter;
    private ActionBar actionBar;

    private int Fragment_Home = 0;
    private int Fragment_Account = 1;
    private int Fragment_MyFriend = 3;
    private int Fragment_FindFriend = 5;
    private int Fragment_FriendAccept = 4;
    private int Fragment_NewEvent = 7;
    private int Fragment_MyEvent = 8;
    private int Fragment_OldEvent = 9;
    private int Fragment_About = 10;
    private int Fragment = Fragment_Home;
    private int FragmentItem_addevent = 11;


    private static Fragment fragment = null;
    private Bundle bundle, bundlesetNameforHome, bundlePassMailtoFind_Accept;
    private String setTitleItem = null;
    private MaterialCalendarView calendar;
    private String textDate;
    private Intent intentAddEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_choose);
        Init();
        SetNameUser();
        if (savedInstanceState == null) {
            do_ItemListener(0);
        }

    }


    private void Init() {
        DrawerTitle = getTitle();
        Title = getTitle();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout_menu);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.hello, R.string.hello) {

            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                actionBar.setTitle(Title);
                invalidateOptionsMenu();
            }


            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                actionBar.setTitle(DrawerTitle);
                invalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        lstMenu = (ListView) findViewById(R.id.lstmenu);

        MenuTitle = getResources().getStringArray(R.array.my_data);

        arrayList_menuItem = new ArrayList<MenuItem>();
        CreateListMenu();

        menuAdapter = new MenuAdapter(arrayList_menuItem, getApplicationContext());
        lstMenu.setAdapter(menuAdapter);

        lstMenu.setOnItemClickListener(new ItemListener());

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
                menuInflater.inflate(R.menu.menu_myfriend, menu);
                break;
            case 4:
                menuInflater.inflate(R.menu.menu_friendaccept, menu);
                break;
            case 5:
                menuInflater.inflate(R.menu.menu_findfriend, menu);
                break;
            case 7:
                menuInflater.inflate(R.menu.menu_newevent, menu);
                break;
            case 8:
                menuInflater.inflate(R.menu.menu_myevent, menu);
                break;
            case 9:
                menuInflater.inflate(R.menu.menu_oldevent, menu);
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
            if (drawerLayout.isDrawerOpen(lstMenu))
                drawerLayout.closeDrawer(lstMenu);
            else
                drawerLayout.openDrawer(lstMenu);
        }

        switch (id) {
            case R.id.back:
                if (menu != null)
                    menu.clear();
                SetNameUser();
                fragment = new HomeActivity();
                fragment.setArguments(bundlesetNameforHome);
                setTitleItem = "HomeActivity";
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayHomeAsUpEnabled(true);
                Fragment = Fragment_Home;
                break;
            case R.id.findfriend:
                srvFindFriend = (SearchView) findViewById(R.id.activity_find_friend_srv_search);
                if (srvFindFriend.getVisibility() == View.INVISIBLE)
                    srvFindFriend.setVisibility(View.VISIBLE);
                else
                    srvFindFriend.setVisibility(View.INVISIBLE);
                break;

        }
        if (fragment != null) {
            FragmentManager fManager = getFragmentManager();
            fManager.beginTransaction().replace(R.id.framelayout_menu, fragment).commit();
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
                menuInflater.inflate(R.menu.menu_add_newevent, menu);
                break;
        }


        return super.onOptionsItemSelected(item);
    }


    //Listen event Button
    private class ItemListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            do_ItemListener(position);
        }
    }


    //Change among Fragments
    public void do_ItemListener(int position) {
        if (menu != null)
            menu.clear();
        switch (position) {
            case 0:
                Fragment = Fragment_Home;
                if (menuInflater != null) {
                    menuInflater.inflate(R.menu.menu_home, menu);
                }
                SetNameUser();
                fragment = new HomeActivity();
                fragment.setArguments(bundlesetNameforHome);
                break;
            case 1:
                Fragment = Fragment_Account;
                PassDatatoFind_Accept_Friend();
                // menuInflater.inflate(R.menu.menu_account,menu);
                fragment = new AccountActivity();
                fragment.setArguments(bundlePassMailtoFind_Accept);
                break;
            case 3:
                Fragment = Fragment_MyFriend;
                // menuInflater.inflate(R.menu.menu_myfriend,menu);
                PassDatatoFind_Accept_Friend();
                fragment = new MyFriend();
                fragment.setArguments(bundlePassMailtoFind_Accept);
                break;
            case 4:
                Fragment = Fragment_FriendAccept;
                // menuInflater.inflate(R.menu.menu_friendaccept,menu);
                PassDatatoFind_Accept_Friend();
                fragment = new FriendAccept();
                fragment.setArguments(bundlePassMailtoFind_Accept);
                break;
            case 5:
                Fragment = Fragment_FindFriend;
                // menuInflater.inflate(R.menu.menu_findfriend,menu);
                PassDatatoFind_Accept_Friend();
                fragment = new FindFriendActivity();
                fragment.setArguments(bundlePassMailtoFind_Accept);
                break;
            case 7:
                Fragment = Fragment_NewEvent;
                PassDatatoFind_Accept_Friend();
                //menuInflater.inflate(R.menu.menu_newevent,menu);
                fragment = new NewEvent();
                fragment.setArguments(bundlePassMailtoFind_Accept);
                break;
            case 8:
                Fragment = Fragment_MyEvent;
                PassDatatoFind_Accept_Friend();
                // menuInflater.inflate(R.menu.menu_oldevent,menu);
                fragment = new MyEvent();
                fragment.setArguments(bundlePassMailtoFind_Accept);
                break;
            case 9:
                Fragment = Fragment_OldEvent;
                PassDatatoFind_Accept_Friend();
                // menuInflater.inflate(R.menu.menu_oldevent,menu);
                fragment = new OldEvent();
                fragment.setArguments(bundlePassMailtoFind_Accept);
                break;
            case 10:
                Fragment = Fragment_About;
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
            fManager.beginTransaction().replace(R.id.framelayout_menu, fragment).commit();
            lstMenu.setItemChecked(position, true);
            lstMenu.setSelection(position);

            setTitle(MenuTitle[position]);
            Title = getTitle();
            drawerLayout.closeDrawer(lstMenu);

        } else {
            Log.e("Main", "error");
        }
    }


    public void CreateListMenu() {
        arrayList_menuItem.add(new MenuItem(MenuTitle[0], MenuIcon[0]));
        arrayList_menuItem.add(new MenuItem(MenuTitle[1], MenuIcon[1]));
        arrayList_menuItem.add(new MenuItem(MenuTitle[2]));
        arrayList_menuItem.add(new MenuItem(MenuTitle[3], MenuIcon[2]));
        arrayList_menuItem.add(new MenuItem(MenuTitle[4], MenuIcon[3]));
        arrayList_menuItem.add(new MenuItem(MenuTitle[5], MenuIcon[4]));
        arrayList_menuItem.add(new MenuItem(MenuTitle[6]));
        arrayList_menuItem.add(new MenuItem(MenuTitle[7], MenuIcon[5]));
        arrayList_menuItem.add(new MenuItem(MenuTitle[8], MenuIcon[6]));
        arrayList_menuItem.add(new MenuItem(MenuTitle[9], MenuIcon[7]));
        arrayList_menuItem.add(new MenuItem(MenuTitle[10], MenuIcon[8]));
        arrayList_menuItem.add(new MenuItem(MenuTitle[11], MenuIcon[9]));

    }


    public void Thoat() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Accept");
        builder.setMessage("You want to exit?");
        builder.setIcon(R.drawable.warning);

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
                do_ItemListener(0);
                break;
            case 2:
                do_ItemListener(0);
                break;
            case 3:
                do_ItemListener(0);
                break;
            case 4:
                do_ItemListener(0);
                break;
            case 5:
                do_ItemListener(0);
                break;
            case 6:
                do_ItemListener(0);
                break;
            case 7:
                do_ItemListener(0);
                break;
            case 8:
                do_ItemListener(0);
                break;
            case 9:
                do_ItemListener(0);
                break;

        }
    }


    // private static StringBuilder txt;

    //pass date from menu_choose to HomeActivity
    /*public void DosetDateFrom() {
        calendar =(MaterialCalendarView)findViewById(R.id.calendarView);
        Calendar c= Calendar.getInstance();
        final int cday=c.get(Calendar.DAY_OF_MONTH);
        final int cmonth=c.get(Calendar.MONTH)+1;
        final int cyear=c.get(Calendar.YEAR);
        txt=new StringBuilder().append(cday).append("-").append(cmonth).append("-").append(cyear);
        textDate=getIntent().getStringExtra("ChangeDate");
        intentAddEvent=new Intent (Menu_Choose.this, AddEvent.class);
        if(textDate!=null) {
            intentAddEvent.putExtra("txtsetdateTo", textDate.toString());
        }
        else
            intentAddEvent.putExtra("txtsetdateTo", txt.toString());
    }*/

    //pass Name user to HomeActivity
    public void SetNameUser() {
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
        bundlePassMailtoFind_Accept = new Bundle();
        bundlePassMailtoFind_Accept.putString("MailforFindFriend", Mailuser);
        bundlePassMailtoFind_Accept.putString("NameforFindFriend", Nameuser);
    }


}




