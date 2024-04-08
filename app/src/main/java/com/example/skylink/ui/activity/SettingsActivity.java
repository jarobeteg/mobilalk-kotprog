package com.example.skylink.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.skylink.R;

public class SettingsActivity extends AbsThemeActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTheme();
        setContentView(R.layout.activity_settings);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }

        Toolbar toolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            setThemePreference();
            setThemeColorPreference();
        }

        private void setThemePreference() {
            Preference.OnPreferenceClickListener clickListener = preference -> true;
            Preference.OnPreferenceChangeListener changeListener = (preference, newValue) -> {
                ListPreference listPreference = (ListPreference) preference;
                CharSequence[] entries = listPreference.getEntries();
                listPreference.setSummary(entries[listPreference.findIndexOfValue(newValue.toString())]);

                requireActivity().recreate();
                return true;
            };

            ListPreference themePreference = findPreference(getString(R.string.pref_theme_key));
            if (themePreference != null) {
                themePreference.setOnPreferenceChangeListener(changeListener);
                themePreference.setOnPreferenceClickListener(clickListener);
                themePreference.setSummary(themePreference.getEntry());
            }
        }

        private void setThemeColorPreference() {
            Preference.OnPreferenceClickListener clickListener = preference -> true;
            Preference.OnPreferenceChangeListener changeListener = (preference, newValue) -> {
                ListPreference listPreference = (ListPreference) preference;
                CharSequence[] entries = listPreference.getEntries();
                listPreference.setSummary(entries[listPreference.findIndexOfValue(newValue.toString())]);

                requireActivity().recreate();
                return true;
            };

            ListPreference colorPreference = findPreference(getString(R.string.pref_color_key));
            if (colorPreference != null) {
                colorPreference.setOnPreferenceChangeListener(changeListener);
                colorPreference.setOnPreferenceClickListener(clickListener);
                colorPreference.setSummary(colorPreference.getEntry());
            }
        }
    }
}
