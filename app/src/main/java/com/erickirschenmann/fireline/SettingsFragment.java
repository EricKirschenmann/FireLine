package com.erickirschenmann.fireline;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

/** Created by eric on 3/21/17. */
public class SettingsFragment extends PreferenceFragmentCompat
    implements SharedPreferences.OnSharedPreferenceChangeListener {

  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    // if app is in debug mode use debug settings otherwise use regular settings
    if (getContext().getResources().getBoolean(R.bool.debug)) {
      addPreferencesFromResource(R.xml.pref_fireline_debug);
    } else {
      addPreferencesFromResource(R.xml.pref_fireline);
    }

    SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
    PreferenceScreen preferenceScreen = getPreferenceScreen();
    int count = preferenceScreen.getPreferenceCount();

    // check if the current build is in debug or not and remove debug data if it is not
    resetDebugPreference();

    // Go through all of the preferences, and set up their preference summary.
    for (int i = 0; i < count; i++) {
      Preference preference = preferenceScreen.getPreference(i);
      // don't need to set up preference summaries for checkbox preferences because
      // they are already set up in xml using summaryOff and summary On
      if (!(preference instanceof CheckBoxPreference)) {
        String value = sharedPreferences.getString(preference.getKey(), "");
        setPreferenceSummary(preference, value);
      }
    }
  }

  /** Removing debug from settings when the debug is disabled */
  private void resetDebugPreference() {
    boolean debug = getContext().getResources().getBoolean(R.bool.debug);
    SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();

    // if the app isn't in debug and the data might be debug data reset it
    if (!debug && sharedPreferences.contains(getString(R.string.pref_show_debug_key))) {
      SharedPreferences.Editor editor = sharedPreferences.edit();
      editor.putBoolean(getString(R.string.pref_show_debug_key), false);
      editor.apply();
    }
  }

  /**
   * Sets the summary of the provided Preference to the current value
   *
   * @param preference The preference to set the summary of
   * @param value The value of the current preference, if ListPreference need to find entry from
   *     value
   */
  private void setPreferenceSummary(Preference preference, String value) {
    if (preference instanceof EditTextPreference) {
      // if this is an EditTextPreference you can just use the value for the summary
      preference.setSummary(value);
    }
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
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
