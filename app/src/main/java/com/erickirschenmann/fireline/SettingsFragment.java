package com.erickirschenmann.fireline;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

/**
 * Created by eric on 3/21/17.
 */
public class SettingsFragment extends PreferenceFragmentCompat implements
    SharedPreferences.OnSharedPreferenceChangeListener {

  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    addPreferencesFromResource(R.xml.pref_fireline);

    SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
    PreferenceScreen prefScreen = getPreferenceScreen();
    int count = prefScreen.getPreferenceCount();

    // Go through all of the preferences, and set up their preference summary.
    for (int i = 0; i < count; i++) {
      Preference p = prefScreen.getPreference(i);
      // You don't need to set up preference summaries for checkbox preferences because
      // they are already set up in xml using summaryOff and summary On
      if (!(p instanceof CheckBoxPreference)) {
        String value = sharedPreferences.getString(p.getKey(), "");
        setPreferenceSummary(p, value);
      }
    }
  }

  void setPreferenceSummary(Preference preference, String value) {
    if (preference instanceof EditTextPreference) {
      preference.setSummary(value);
    }
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getPreferenceScreen().getSharedPreferences()
        .registerOnSharedPreferenceChangeListener(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    getPreferenceScreen().getSharedPreferences()
        .unregisterOnSharedPreferenceChangeListener(this);
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    // Figure out which preference was changed
    Preference preference = findPreference(key);
    if (null != preference) {
      // Updates the summary for the preference
      if (!(preference instanceof CheckBoxPreference)) {
        String value = sharedPreferences.getString(preference.getKey(), "");
        setPreferenceSummary(preference, value);
      }
    }
  }
}
