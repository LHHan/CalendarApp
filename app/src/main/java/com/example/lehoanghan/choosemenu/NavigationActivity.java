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

import com.example.lehoanghan.appcalendar.LoginActivity_;
import com.example.lehoanghan.appcalendar.R;
import com.example.lehoanghan.optionmenu.AboutFragment;
import com.example.lehoanghan.optionmenu.AboutFragment_;
import com.example.lehoanghan.optionmenu.AccountFragment;
import com.example.lehoanghan.optionmenu.FindFriendFragment;
import com.example.lehoanghan.optionmenu.FriendAcceptFragment;
import com.example.lehoanghan.optionmenu.HomeFragment;
import com.example.lehoanghan.optionmenu.MyEventFragment;
import com.example.lehoanghan.optionmenu.MyFriendFragment;
import com.example.lehoanghan.optionmenu.NewEventFragment;
import com.example.lehoanghan.optionmenu.OldEventFragment;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;

public class NavigationActivity extends AppCompatActivity {

    private static Fragment sFragment = null;

    private static MenuInflater sMenuInflater;

    private static Menu sMenu;

    private DrawerLayout drawerLayout;

    private ListView lvMenu;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    private CharSequence drawerTitle;

    private CharSequence strTitle;

    private SearchView srvFindFriend;

    private String[] strMenuTitle;

    private int[] menuIcon = {R.drawable.ic_house, R.drawable.ic_account, R.drawable.ic_my_friend,
            R.drawable.ic_add_friend, R.drawable.ic_find_friend,
            R.drawable.ic_new_event, R.drawable.ic_my_event,
            R.drawable.ic_old_event, R.drawable.ic_about, R.drawable.ic_exit};

    private ArrayList<NavigationClass> arrListMenuItem;

    private NavigationAdapter menuAdapter;

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

    private int intFragment = fragmentHome;

    private int intFragmentItemAddevent = 11;

    private Bundle bundlesetNameforHome;

    private Bundle bundlePassMailtoFindAccept;

    private String setTitleItem = null;

    private MaterialCalendarView mcvCalendar;

    private String textDate;

    private Intent intentAddEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_choose);
        aInit();
        setNameUser();
        if (savedInstanceState == null) {
            doItemListener(0);
        }

    }

    private void aInit() {
        drawerTitle = getTitle();
        strTitle = getTitle();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout_menu);
        actionBarDrawerToggle =
                new ActionBarDrawerToggle(this, drawerLayout, R.string.hello, R.string.hello) {

                    @Override
                    public void onDrawerClosed(View view) {
                        super.onDrawerClosed(view);
                        actionBar.setTitle(strTitle);
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

        strMenuTitle = getResources().getStringArray(R.array.my_data);

        arrListMenuItem = new ArrayList<NavigationClass>();
        createListMenu();

        menuAdapter = new NavigationAdapter(arrListMenuItem, getApplicationContext());
        lvMenu.setAdapter(menuAdapter);

        lvMenu.setOnItemClickListener(new ItemListener());

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        actionBarDrawerToggle.syncState();
        super.onPostCreate(savedInstanceState);
    }

    //Create options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        sMenuInflater = getMenuInflater();
        this.sMenu = menu;
        sMenuInflater.inflate(R.menu.menu_main, this.sMenu);
        if (menu != null) {
            menu.clear();
        } else if (intFragment == 0) {
            sMenuInflater.inflate(R.menu.menu_home, menu);
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
            if (drawerLayout.isDrawerOpen(lvMenu)) {
                drawerLayout.closeDrawer(lvMenu);
            } else {
                drawerLayout.openDrawer(lvMenu);
            }
        }


        if (sFragment != null) {
            FragmentManager fManager = getFragmentManager();
            fManager.beginTransaction().replace(
                    R.id.activity_menu_choose_fl_navigation, sFragment).commit();
            setTitle(setTitleItem);
        } else {
            Log.e("Main", "error");
        }

        switch (intFragment) {
            case 0:
                if (sMenu != null) {
                    sMenu.clear();
                }
                sMenuInflater.inflate(R.menu.menu_home, sMenu);
                break;
            case 11:
                if (sMenu != null) {
                    sMenu.clear();
                }
                sMenuInflater.inflate(R.menu.menu_add_event, sMenu);
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
        if (sMenu != null) {
            sMenu.clear();
        }
        switch (position) {
            case 0:
                intFragment = fragmentHome;
                if (sMenuInflater != null) {
                    sMenuInflater.inflate(R.menu.menu_home, sMenu);
                }
                setNameUser();
                sFragment = new HomeFragment();
                sFragment.setArguments(bundlesetNameforHome);
                break;
            case 1:
                intFragment = fragmentAccount;
                passDatatoFindAcceptFriend();
                // sMenuInflater.inflate(R.sMenu.menu_account,sMenu);
                sFragment = new AccountFragment();
                sFragment.setArguments(bundlePassMailtoFindAccept);
                break;
            case 3:
                intFragment = fragmentMyFriend;
                // sMenuInflater.inflate(R.sMenu.menu_my_friend,sMenu);
                passDatatoFindAcceptFriend();
                sFragment = new MyFriendFragment();
                sFragment.setArguments(bundlePassMailtoFindAccept);
                break;
            case 4:
                intFragment = fragmentFriendAccept;
                // sMenuInflater.inflate(R.sMenu.menu_friend_accept,sMenu);
                passDatatoFindAcceptFriend();
                sFragment = new FriendAcceptFragment();
                sFragment.setArguments(bundlePassMailtoFindAccept);
                break;
            case 5:
                intFragment = fragmentFindFriend;
                // sMenuInflater.inflate(R.sMenu.menu_find_friend,sMenu);
                passDatatoFindAcceptFriend();
                sFragment = new FindFriendFragment();
                sFragment.setArguments(bundlePassMailtoFindAccept);
                break;
            case 7:
                intFragment = fragmentNewEvent;
                passDatatoFindAcceptFriend();
                //sMenuInflater.inflate(R.sMenu.menu_new_event,sMenu);
                sFragment = new NewEventFragment();
                sFragment.setArguments(bundlePassMailtoFindAccept);
                break;
            case 8:
                intFragment = fragmentMyEvent;
                passDatatoFindAcceptFriend();
                // sMenuInflater.inflate(R.sMenu.menu_old_event,sMenu);
                sFragment = new MyEventFragment();
                sFragment.setArguments(bundlePassMailtoFindAccept);
                break;
            case 9:
                intFragment = fragmentOldEvent;
                passDatatoFindAcceptFriend();
                // sMenuInflater.inflate(R.sMenu.menu_old_event,sMenu);
                sFragment = new OldEventFragment();
                sFragment.setArguments(bundlePassMailtoFindAccept);
                break;
            case 10:
                intFragment = fragmentAbout;
                //  sMenuInflater.inflate(R.sMenu.menu_about,sMenu);
                sFragment = new AboutFragment_();
                break;
            case 11:
                aExit();
                break;
            default:
                break;
        }

        if (sFragment != null) {
            FragmentManager fManager = getFragmentManager();
            fManager.beginTransaction().replace(
                    R.id.activity_menu_choose_fl_navigation, sFragment).commit();
            lvMenu.setItemChecked(position, true);
            lvMenu.setSelection(position);

            setTitle(strMenuTitle[position]);
            strTitle = getTitle();
            drawerLayout.closeDrawer(lvMenu);

        } else {
            Log.e("Main", "error");
        }
    }

    public void createListMenu() {
        arrListMenuItem.add(new NavigationClass(strMenuTitle[0], menuIcon[0]));
        arrListMenuItem.add(new NavigationClass(strMenuTitle[1], menuIcon[1]));
        arrListMenuItem.add(new NavigationClass(strMenuTitle[2]));
        arrListMenuItem.add(new NavigationClass(strMenuTitle[3], menuIcon[2]));
        arrListMenuItem.add(new NavigationClass(strMenuTitle[4], menuIcon[3]));
        arrListMenuItem.add(new NavigationClass(strMenuTitle[5], menuIcon[4]));
        arrListMenuItem.add(new NavigationClass(strMenuTitle[6]));
        arrListMenuItem.add(new NavigationClass(strMenuTitle[7], menuIcon[5]));
        arrListMenuItem.add(new NavigationClass(strMenuTitle[8], menuIcon[6]));
        arrListMenuItem.add(new NavigationClass(strMenuTitle[9], menuIcon[7]));
        arrListMenuItem.add(new NavigationClass(strMenuTitle[10], menuIcon[8]));
        arrListMenuItem.add(new NavigationClass(strMenuTitle[11], menuIcon[9]));

    }

    public void aExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Accept");
        builder.setMessage("Do You want to Exit?");
        builder.setIcon(R.drawable.ic_warning);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(NavigationActivity.this, LoginActivity_.class);
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
        switch (intFragment) {
            case 0:
                aExit();
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
        String nameUser = "", mailUser = "";
        String nameUserfromAddEvent = getIntent().getStringExtra("NameUserfromAddEvent");
        if (bundle != null) {
            nameUser = bundle.getString("NameUser").toLowerCase();
            mailUser = bundle.getString("MailUser").toLowerCase();
        }
        bundlesetNameforHome = new Bundle();
        bundlesetNameforHome.putString("MailforHome", mailUser);
        if (nameUser != null) {
            bundlesetNameforHome.putString("NameforHome", nameUser);
        } else {
            bundlesetNameforHome.putString("NameforHome", nameUserfromAddEvent);
        }

    }

    public void passDatatoFindAcceptFriend() {
        //pass MailUSER to FindName for don't give my mail into list
        Bundle bundle = getIntent().getExtras();
        String mailUser = "";
        String nameUser = "";
        if (bundle != null) {
            mailUser = bundle.getString("MailUser");
            nameUser = bundle.getString("NameUser");
        }
        bundlePassMailtoFindAccept = new Bundle();
        bundlePassMailtoFindAccept.putString("MailforFindFriend", mailUser);
        bundlePassMailtoFindAccept.putString("NameforFindFriend", nameUser);
    }
}




