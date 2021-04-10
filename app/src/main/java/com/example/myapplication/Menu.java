package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;

import nl.joery.animatedbottombar.AnimatedBottomBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the Menu Activity Boundary which displays the menu bar and helps to switch between fragments after user login
 */
public class Menu extends AppCompatActivity {
    /**
     * Tag to store the simple name of the Menu class
     */
    private static final String TAG = LoginUser.class.getSimpleName();
    /**
     * AnimatedBottomBar for users to switch between different sections of the application
     */
    private AnimatedBottomBar animatedBottomBar;
    /**
     * Fragment Manager to manage the switching of fragments
     */
    private FragmentManager fragmentManager;
    /**
     * Creation of activity from savedInstanceState and setting the layout
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        animatedBottomBar = findViewById(R.id.bottom_bar);

        if (savedInstanceState == null){
            animatedBottomBar.selectTabById(R.id.home, true);
            fragmentManager = getSupportFragmentManager();
            HomeFragment homeFragment = new HomeFragment();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
        }

        animatedBottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {

            @Override
            public void onTabSelected(int lastIndex, @Nullable AnimatedBottomBar.Tab lastTab, int newIndex, @NotNull AnimatedBottomBar.Tab newTab) {
                Fragment fragment = null;
                switch (newTab.getId()){
                    case R.id.home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.search:
                        fragment = new SearchFragment();
                        break;
                    case R.id.account:
                        fragment = new AccountFragment();
                        break;
                    case R.id.forum:
                        fragment = new ForumFragment();
                        break;
                    case R.id.favList:
                        fragment = new FavListFragment();
                        break;
                    case R.id.map:
                        fragment = new MapsFragment();
                }

                if (fragment != null){
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                }
                else{
                    Log.e(TAG, "Error in creating fragment");
                }
            }

            @Override
            public void onTabReselected(int i, @NotNull AnimatedBottomBar.Tab tab) {

            }
        });
    }
}