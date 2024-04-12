package com.example.myeducationapp.ui.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.myeducationapp.DAO.UserDAO.UserDao;
import com.example.myeducationapp.Global;
import com.example.myeducationapp.ui.navigation.NavigationActivity;
import com.example.myeducationapp.R;
import com.example.myeducationapp.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
/**
 * @author all members
 * **/
public class SettingsActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_LOCATION = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        public static SettingsActivity settingsActivity;
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            /**
             * @author u7560434 Ethan Yifan Zhu
             * set text field listeners
             * **/
            EditTextPreference nameEditTextPreference = findPreference("name");
            nameEditTextPreference.setDefaultValue(Global.currentUser.getName());
            nameEditTextPreference.setText(Global.currentUser.getName());
            // set names
            nameEditTextPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                    Log.d("TAG", newValue.toString());
                    Global.currentUser.setName(newValue.toString());
                    new UserDao().updateUserInfo(Global.currentUser);
                    return true;
                }
            });

            // set gender
            ListPreference genderListPreference = findPreference("gender");
            if(Global.currentUser.getSex() == 0){
                genderListPreference.setValueIndex(1);
                genderListPreference.setSummary("Female");
            } else if (Global.currentUser.getSex() == 1) {
                genderListPreference.setValueIndex(0);
                genderListPreference.setSummary("Male");
            }else if (Global.currentUser.getSex() == 2){
                genderListPreference.setValueIndex(2);
                genderListPreference.setSummary("Other");
            }else {
                genderListPreference.setValueIndex(3);
                genderListPreference.setSummary("Prefer not to say");
            }
            genderListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                    Log.d("TAG", newValue.toString());
                    if(newValue.toString().equals("Female")){
                        Global.currentUser.setSex(0);
                    } else if (newValue.toString().equals("Male")) {
                        Global.currentUser.setSex(1);
                    } else if (newValue.toString().equals("Other")) {
                        Global.currentUser.setSex(2);
                    }else {
                        Global.currentUser.setSex(3);
                    }
                    new UserDao().updateUserInfo(Global.currentUser);
                    genderListPreference.setSummary(newValue.toString());
                    return true;
                }
            });

            EditTextPreference emailEditTextPreference = findPreference("email");
            emailEditTextPreference.setEnabled(false);
            emailEditTextPreference.setText(Global.currentUser.getEmail());

            ListPreference regionListPreference = findPreference("region");
            List<Locale> locales = getAllLocale();
            CharSequence entries[] = new String[locales.size()];
            CharSequence entrisValues[] = new String[locales.size()];
            int i = 0;
            for(Locale l : locales){
                entries[i] = l.getDisplayCountry();
                entrisValues[i] = l.getDisplayCountry();
                i++;
            }
            regionListPreference.setEntries(entries);
            regionListPreference.setEntryValues(entrisValues);
            String country = Global.currentUser.getCountry();
            String countryValue = Global.currentUser.getCountry();
            if(country == null){
                regionListPreference.setSummary("Current country: Cannot get current location");
            }
                else{
                regionListPreference.setSummary("Current country: " + country);
                regionListPreference.setValue(countryValue);
            }
                // set region
            regionListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                    Log.d("TAG", newValue.toString());
                    Global.currentUser.setCountry(newValue.toString());
                    regionListPreference.setValue(Global.currentUser.getCountry());
                    regionListPreference.setSummary("Current country: " +Global.currentUser.getCountry());
                    return false;
                }
            });

                // set language
            ListPreference languageListPreference = findPreference("language");
            String language = getContext().getResources().getConfiguration().getLocales().get(0).getDisplayLanguage();
            Log.d("TAG", language);
            String displayLanguage = "";
            switch (language) {
                case "English":
                    displayLanguage = "English";
                    break;
                case "Chinese":
                    displayLanguage = "简体中文";
                    break;
                case "Japanese":
                    displayLanguage = "日本語";
                    break;
                case "French":
                    displayLanguage = "Français";
                    break;
                default:
                    break;
            }
            languageListPreference.setSummary("Current language: " + displayLanguage);
            languageListPreference.setValue(displayLanguage);
            languageListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                    Log.d("TAG", newValue.toString());

                    Configuration config = new Configuration();
                    Resources resources = getResources();
                    DisplayMetrics dm = resources.getDisplayMetrics();

                    switch (newValue.toString()){
                        case "English":
                            config.setLocale(Locale.ENGLISH);
                            break;
                        case "简体中文":
                            config.setLocale(Locale.SIMPLIFIED_CHINESE);
                            break;
                        case "繁體中文":
                            config.setLocale(Locale.TRADITIONAL_CHINESE);
                            break;
                        case "日本語":
                            config.setLocale(Locale.JAPANESE);
                            break;
                        case "Français":
                            config.setLocale(Locale.FRENCH);
                            break;
                        default:
                            config.setLocale(Locale.ENGLISH);
                            break;
                    }
                    resources.updateConfiguration(config, dm);
                    languageListPreference.setValue(newValue.toString());
                    languageListPreference.setSummary("Current Language: " +newValue.toString());

                    Intent intent = new Intent(getContext(), NavigationActivity.class);
                    startActivity(intent);
                    return false;
                }
            });
            /**
             * end of setting text field listeners
             * **/

            // Handle click events for "Log out" preference
            Preference logoutPreference = findPreference("logout");
            logoutPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    showLogoutConfirmationDialog();
                    return true;
                }
            });
        }

        /**
         * @author u7560434 Ethan Yifan Zhu
         * get all countries' name for the list
         * **/
        public List<Locale> getAllLocale(){
            List<Locale> mAllLocale  = new ArrayList<>();
            for (String str : Locale.getISOCountries()){
                mAllLocale.add(new Locale("",str));
            }
            return mAllLocale;
        }

        /**
         * @author u7518626 Pin-Shen Chang
         * show Logout Confirmation Dialog
         */
        private void showLogoutConfirmationDialog() {
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.logout_title)
                    .setMessage(R.string.sure_logout)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // log out user and redirect to login activity
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            startActivity(intent);
                            Global.currentUser.logoutUser();
                            getActivity().finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    /**
     * @author Pin-Shen Chang
     * leave the Settings Page
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
