package com.example.skylink.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import androidx.preference.PreferenceManager;

import com.example.skylink.R;

public class ThemeUtil {
    private Context context;
    private SharedPreferences sharedPref;

    private final String themeDay = "day";
    private final String themeNight = "night";
    private final String themeDayNight = "day_night";

    public ThemeUtil(Context context){
        this.context = context;
    }

    public int setThemeType(){
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        String newThemeKey = context.getString(R.string.pref_theme_key);
        String defaultThemeValue = context.getString(R.string.default_theme_value);
        String newTheme = sharedPref.getString(newThemeKey, defaultThemeValue);

        String newColorKey = context.getString(R.string.pref_color_key);
        String defaultColorValue = context.getString(R.string.default_color_value);
        String newColor = sharedPref.getString(newColorKey, defaultColorValue);

        if (newTheme.equals(themeDay)) {
            return dayTheme(newColor);
        } else if (newTheme.equals(themeNight)) {
            return nightTheme(newColor);
        } else if (newTheme.equals(themeDayNight)) {
            if (dayOrNight()) {
                return nightTheme(newColor);
            } else {
                return dayTheme(newColor);
            }
        } else {
            return R.style.Theme_SkyLink_Light_Teal;
        }
    }

    private int dayTheme(String currentColor) {
        switch (currentColor) {
            case "teal":
                return R.style.Theme_SkyLink_Light_Teal;
            case "indigo":
                return R.style.Theme_SkyLink_Light_Indigo;
            case "dark_purple":
                return R.style.Theme_SkyLink_Light_DarkPurple;
            case "blue":
                return R.style.Theme_SkyLink_Light_Blue;
            case "green":
                return R.style.Theme_SkyLink_Light_Green;
            case "amber":
                return R.style.Theme_SkyLink_Light_Amber;
            case "red":
                return R.style.Theme_SkyLink_Light_Red;
            case "pink":
                return R.style.Theme_SkyLink_Light_Pink;
            default:
                return R.style.Theme_SkyLink_Light_Teal;
        }
    }

    private int nightTheme(String currentColor) {
        switch (currentColor) {
            case "teal":
                return R.style.Theme_SkyLink_Dark_Teal;
            case "indigo":
                return R.style.Theme_SkyLink_Dark_Indigo;
            case "dark_purple":
                return R.style.Theme_SkyLink_Dark_DarkPurple;
            case "blue":
                return R.style.Theme_SkyLink_Dark_Blue;
            case "green":
                return R.style.Theme_SkyLink_Dark_Green;
            case "amber":
                return R.style.Theme_SkyLink_Dark_Amber;
            case "red":
                return R.style.Theme_SkyLink_Dark_Red;
            case "pink":
                return R.style.Theme_SkyLink_Dark_Pink;
            default:
                return R.style.Theme_SkyLink_Dark_Teal;
        }
    }

    private boolean dayOrNight() {
        return (context.getResources().getConfiguration().uiMode &
                Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
    }
}
