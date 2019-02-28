package edu.sc.dbkdrymatic.internal.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;

import javax.measure.quantity.Temperature;
import javax.measure.quantity.Volume;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import edu.sc.dbkdrymatic.internal.Country;
import edu.sc.dbkdrymatic.internal.Settings;
import edu.sc.dbkdrymatic.internal.SiteInfo;
import edu.sc.dbkdrymatic.internal.database.Converters;

public class SettingsModel extends ViewModel
    implements SharedPreferences.OnSharedPreferenceChangeListener {

  SharedPreferences preferences;
  MutableLiveData<Settings> settings;

  public SettingsModel(SharedPreferences preferences) {
    this.preferences = preferences;
  }

  /**
   * Updates the Country both in the preferences object and in the {@code LiveData} exposed by
   * this object.
   */
  public void selectCountry(Country country) {
    this.preferences.edit()
        .putString("country", Converters.countryToString(country))
        .apply();
  }

  /**
   * If true, returned {@code Settings} will use cubic feet and degrees Fahrenheit.
   * If false, cubic metres and degrees Celsius are used instead.
   *
   * This is immediately available in both the {@code SharedPreferences} object and the
   * {@code LiveData} exposed by this object.
   */
  public void setImperial(boolean imperial) {
    this.preferences.edit()
        .putBoolean("imperial", imperial)
        .apply();
  }

  /**
   * Given a complete settings object, update the one in preferences and {@code LiveData} to match.
   *
   * @deprecated
   */
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
   *
   * This object is guaranteed to always have a value; it is always safe to call
   * {@code LiveData.getValue()} and assume that there will be a result. We use LiveData in order
   * to observe changes here; not to wait for an async task.
   */
  public LiveData<Settings> getSettings() {
    if (settings == null) {
      settings = new MutableLiveData<>();
      this.loadSettings();
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
    this.loadSettings();
  }

  private void loadSettings() {
    boolean imperial = preferences.getBoolean("imperial", true);
    String countryString = preferences.getString("country", Country.USA.toString());
    Country country = Converters.countryFromString(countryString);
    Unit<Volume> volumeUnit = imperial ? SiteInfo.CUBIC_FOOT : SI.CUBIC_METRE;
    Unit<Temperature> temperatureUnit = imperial ? NonSI.FAHRENHEIT : SI.CELSIUS;

    Settings res = new Settings(volumeUnit, temperatureUnit, country);
    settings.postValue(res);
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
