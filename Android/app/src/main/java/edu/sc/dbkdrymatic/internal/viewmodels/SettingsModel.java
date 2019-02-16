package edu.sc.dbkdrymatic.internal.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import javax.measure.quantity.Temperature;
import javax.measure.quantity.Volume;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import edu.sc.dbkdrymatic.internal.Country;
import edu.sc.dbkdrymatic.internal.Settings;
import edu.sc.dbkdrymatic.internal.SiteInfo;
import edu.sc.dbkdrymatic.internal.database.SiteInfoDao;


public class SettingsModel extends ViewModel
    implements SharedPreferences.OnSharedPreferenceChangeListener {

  SharedPreferences preferences;
  MutableLiveData<Settings> settings;

  public SettingsModel(SharedPreferences preferences) {
    this.preferences = preferences;
  }

  public void updateSettings(Settings settings) {
    preferences.edit()
        .putBoolean("imperial", settings.getVolumeUnit() == SiteInfo.CUBIC_FOOT)
        .putString("country", settings.getCountry().toString())
        .apply();
  }

  /**
   * Provides a monitor of the application settings that will always be up-to-date, tracking any
   * changes that are made to the settings during its lifetime and notifying observers when those
   * changes are made.
   */
  public LiveData<Settings> getSettings() {
    if (settings == null) {
      settings = new MutableLiveData<>();
      new SettingsLoader().start();
      preferences.registerOnSharedPreferenceChangeListener(this);
    }
    return settings;
  }

  /**
   * When SharedPreferences is updated, this will be called by the application and trigger a
   * complete reload of the settings object. The settings object
   */
  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    new SettingsLoader().start();
  }

  /**
   * Completely reloads the settings for the application from a worker thread so as not to lock the
   * UI thread.
   */
  private class SettingsLoader extends Thread {

    @Override
    public void run() {
      boolean imperial = preferences.getBoolean("imperial", true);
      Country country = Country.valueOf(
          preferences.getString("country", Country.USA.toString()));
      Unit<Volume> volumeUnit = imperial ? SiteInfo.CUBIC_FOOT : SI.CUBIC_METRE;
      Unit<Temperature> temperatureUnit = imperial ? NonSI.FAHRENHEIT : SI.CELSIUS;

      Settings res = new Settings(volumeUnit, temperatureUnit, country);
      settings.postValue(res);
    }
  }

  /**
   * Factory for creating {@code SelectedJobModel}s with the DAOs they require.
   *
   * <p>This method is used to avoid the {@code SelectedJobModel} having to know about the
   * Application Context, helping with separation of concerns.
   */
  public static class Factory extends ViewModelProvider.NewInstanceFactory {

    @NonNull
    private final SharedPreferences preferences;

    public Factory(SharedPreferences preferences) {
      this.preferences = preferences;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
      return (T) new SettingsModel(preferences);
    }
  }
}
