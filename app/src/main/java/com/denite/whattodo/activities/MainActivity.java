package com.denite.whattodo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.denite.whattodo.R;
import com.denite.whattodo.activities.LoginActivity;
import com.denite.whattodo.fragments.AttractionListFragment;
import com.denite.whattodo.fragments.HomeFragment;
import com.denite.whattodo.fragments.WebViewFragment;
import com.denite.whattodo.utils.Utils;
import com.denite.whattodo.viewmodels.MainActivityViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import static com.denite.whattodo.utils.Constants.*;

public class MainActivity extends AppCompatActivity
{
    private final String TAG = "MainActivity";
    private static SharedPreferences sharedPrefs;
    private static SharedPreferences.Editor prefEditor;
    private MainActivityViewModel viewModel;

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        sharedPrefs = getSharedPreferences(SHARED_PREF_NAME, 0);
        prefEditor = sharedPrefs.edit();
        Utils.applyTheme(sharedPrefs.getInt(THEME_PREFERENCE, THEME_PREFERENCE_DEFAULT));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        setup();

        if (!viewModel.isInitialized())
        {
            loadPage(viewModel.getCurrentPage());
            viewModel.setInitialized(true);
        }

        setSelectedPage(viewModel.getCurrentPage());
    }

    // initiate fields and views
    private void setup()
    {
        toolbar = findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            toolbar.setTransitionName("toolbar");
        }
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // setup navigationDrawer options
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.action_home:
                        loadHomePage();
                        break;
                    case R.id.action_attraction_list:
                        loadAttractionList();
                        break;
                    case R.id.action_map:
                        loadPage(PAGE_MAP);
                        break;
                    case R.id.action_logout:
                        logout();
                        break;
                    case R.id.action_about:
                        displayAboutDialog();
                        break;
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override // theme menu
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.theme_menu, menu);
        return true;
    }

    @Override // theme menu options
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Log.d(TAG, "onOptionsItemSelected: item: " + item.getItemId());
        if (toggle.onOptionsItemSelected(item))
        {
            return true;
        }

        switch (item.getItemId())
        {
            case android.R.id.home:
                returnToAttractionList();
                return true;
            case R.id.theme_light:
                prefEditor.putInt(THEME_PREFERENCE, AppCompatDelegate.MODE_NIGHT_NO).commit();
                Utils.applyTheme(sharedPrefs.getInt(THEME_PREFERENCE, THEME_PREFERENCE_DEFAULT));
                return true;
            case R.id.theme_dark:
                prefEditor.putInt(THEME_PREFERENCE, AppCompatDelegate.MODE_NIGHT_YES).commit();
                Utils.applyTheme(sharedPrefs.getInt(THEME_PREFERENCE, THEME_PREFERENCE_DEFAULT));
                return true;
            case R.id.theme_default:
                prefEditor.putInt(THEME_PREFERENCE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM).commit();
                Utils.applyTheme(sharedPrefs.getInt(THEME_PREFERENCE, THEME_PREFERENCE_DEFAULT));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Load fragment selector
     *
     * @param pageToLoad - fragment page you would like to load
     */
    private void loadPage(int pageToLoad)
    {
        Log.d(TAG, "loadPage: ");
        switch (pageToLoad)
        {
            case PAGE_HOME:
                loadHomePage();
                break;
            case PAGE_ATTRACTION_LIST:
                loadAttractionList();
                break;
            case PAGE_MAP:
                loadWebView(TORONTO_MAP_URL);
                setSelectedPage(PAGE_MAP);
                setCurrentPage(PAGE_MAP);
                break;
        }
    }

    // load main page intro
    private void loadHomePage()
    {
        if (viewModel.getHomeFragment() == null)
        {
            viewModel.setHomeFragment(HomeFragment.newInstance());
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (getSupportFragmentManager().getFragments().size() == 0)
        {
            transaction.add(R.id.main_container, viewModel.getHomeFragment()).commit();
        }
        else
        {
            transaction.replace(R.id.main_container, viewModel.getHomeFragment()).commit();
        }
        viewModel.setCurrentPage(PAGE_HOME);
        setSelectedPage(PAGE_HOME);
        turnOffBackButton();
    }

    // load attraction list fragment
    private void loadAttractionList()
    {
        if (viewModel.getAttractionListFragment() == null)
        {
            viewModel.setAttractionListFragment(AttractionListFragment.newInstance());
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, viewModel.getAttractionListFragment()).commit();
        viewModel.setCurrentPage(PAGE_ATTRACTION_LIST);
        setSelectedPage(PAGE_ATTRACTION_LIST);
        turnOffBackButton();
    }

    // load webView fragment
    private void loadWebView(String url)
    {
        WebViewFragment webViewFragment = WebViewFragment.newInstance(url);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, webViewFragment)
                .addToBackStack(null)
                .commit();
        viewModel.setCurrentPage(PAGE_WEBVIEW);
    }

    // set selected item in navigationDrawer
    private void setSelectedPage(int page)
    {
        navigationView.getMenu().getItem(0).setChecked(false);
        navigationView.getMenu().getItem(1).setChecked(false);
        navigationView.getMenu().getItem(2).setChecked(false);

        switch (page)
        {
            case PAGE_HOME:
                navigationView.getMenu().getItem(0).setChecked(true);
                break;
            case PAGE_ATTRACTION_LIST:
                navigationView.getMenu().getItem(1).setChecked(true);
                break;
            case PAGE_MAP:
                navigationView.getMenu().getItem(2).setChecked(true);
                break;
        }
    }

    // log user out and send back to login screen
    private void logout()
    {
        prefEditor.putString(LOGIN_CURRENT_USER, null).commit();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    // display about dialog
    private void displayAboutDialog()
    {
        final Dialog aboutDialog = new Dialog(MainActivity.this, R.style.AlertDialogFullScreen);
        aboutDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        aboutDialog.setContentView(R.layout.dialog_about);
        aboutDialog.setCancelable(true);

        TextView versionTextView = aboutDialog.findViewById(R.id.aboutVersion_textView);
        try
        {
            // load current version number
            versionTextView.setText(
                    getString(R.string.version_) + " " +
                            getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        final MaterialButton closeButton = aboutDialog.findViewById(R.id.close_button);
        Utils.setHaptic(closeButton);
        closeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                aboutDialog.dismiss();
            }
        });

        aboutDialog.show();
    }

    /**
     * Called only when attraction details page is open
     * to go back to list of attractions
     */
    private void returnToAttractionList()
    {
        super.onBackPressed();
        turnOffBackButton();
        viewModel.setCurrentPage(PAGE_ATTRACTION_LIST);
    }

    //Turn back button on
    private void turnOnBackButton()
    {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });
    }

    // Turn back button off
    private void turnOffBackButton()
    {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     * @param currentPage - set reference for current fragment loaded
     */
    public void setCurrentPage(int currentPage)
    {
        viewModel.setCurrentPage(currentPage);
    }

    // viewModel getter
    public MainActivityViewModel getViewModel()
    {
        return viewModel;
    }

    @Override // receive intents form other pages
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);

        if (intent != null && intent.getAction() != null)
        {
            switch (intent.getAction())
            {
                case ACTION_DISPLAY_BACK_BUTTON:
                    turnOnBackButton();
                    break;
                case ACTION_LOAD_WEBVIEW:
                    loadWebView(intent.getStringExtra(EXTRA_URL_STRING));
                    break;
            }
        }
    }

    @Override // back affordance
    public void onBackPressed()
    {
        // ensure nav drawer is closed
        drawer.closeDrawer(GravityCompat.START);
        if (viewModel.getCurrentPage() == PAGE_WEBVIEW)
        {
            super.onBackPressed();
            setCurrentPage(PAGE_ATTRACTION);
            setSelectedPage(viewModel.getCurrentPage());
        }
        else if (viewModel.getCurrentPage() == PAGE_ATTRACTION)
        {
            returnToAttractionList();
            setSelectedPage(viewModel.getCurrentPage());
        }
        else if (viewModel.getCurrentPage() != PAGE_HOME)
        {
            loadHomePage();
        }
        else if (viewModel.getCurrentPage() == PAGE_HOME)
        {
            finish();
        }
        else
        {
            super.onBackPressed();
        }
    }
}