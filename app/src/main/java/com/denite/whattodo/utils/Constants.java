package com.denite.whattodo.utils;

import androidx.appcompat.app.AppCompatDelegate;

public class Constants
{
    private final String TAG = "Constants";
    public final static String SHARED_PREF_NAME = "MCMB_Prefs";
    public final static String THEME_PREFERENCE = "themePreference";
    public final static int THEME_PREFERENCE_DEFAULT = AppCompatDelegate.MODE_NIGHT_YES;

    public final static int PAGE_HOME = 0;
    public final static int PAGE_ATTRACTION_LIST = 1;
    public final static int PAGE_ATTRACTION = 2;
    public final static int PAGE_WEBVIEW = 3;
    public final static int PAGE_MAP = 4;

    public final static String TORONTO_MAP_URL = "https://bing.com/maps/default.aspx?cp=43.6532~-79.3832";
    public final static String LOGIN_REMEMBER_ME = "loginRememberMe";
    public final static String LOGIN_CURRENT_USER = "loginCurrentUser";
    public final static Boolean LOGIN_REMEMBER_ME_DEFAULT = false;

    public static final String ACTION_DISPLAY_BACK_BUTTON = "actionDisplayBackButton";
    public static final String ACTION_LOAD_WEBVIEW = "actionLoadWebview";

    public static final String EXTRA_ATTRACTION = "extraAttraction";
    public static final String EXTRA_URL_STRING = "extraUrlString";

    public static final String INITIAL_FRAGMENT_LOAD = "initialFragmentLoad";

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;


}
