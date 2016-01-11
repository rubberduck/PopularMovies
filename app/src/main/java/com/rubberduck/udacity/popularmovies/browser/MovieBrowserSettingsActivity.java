package com.rubberduck.udacity.popularmovies.browser;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.rubberduck.udacity.popularmovies.R;


public class MovieBrowserSettingsActivity extends PreferenceActivity {

    private static final String LOG_TAG = MovieBrowserSettingsActivity.class.getSimpleName();

    private boolean settingsChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SettingsFragment settingsFragment = new SettingsFragment();
        FragmentManager fragmentManager = getFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(android.R.id.content, settingsFragment);
        fragmentTransaction.commit();

    }

    protected void setSettingsChanged()  {
        this.settingsChanged = true;
        Intent data = new Intent();
        data.putExtra(getString(R.string.key_intent_settings_changed), true);
        setResult(RESULT_OK, data);
    }

}
