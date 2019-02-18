package edu.sc.dbkdrymatic.internal.viewmodels;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.concurrent.ExecutionException;

import javax.measure.unit.NonSI;
import javax.measure.unit.SI;

import edu.sc.dbkdrymatic.internal.Country;
import edu.sc.dbkdrymatic.internal.Settings;
import edu.sc.dbkdrymatic.internal.SiteInfo;
import edu.sc.dbkdrymatic.test.Utils;


@RunWith(RobolectricTestRunner.class)
public class SettingsModelTest {
  @Rule
  public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

  private LifecycleOwner owner;
  private LifecycleRegistry registry;
  private SharedPreferences preferences;
  private LiveData<Settings> liveSettings;

  @Before
  public void setUp() {
    Context appContext = RuntimeEnvironment.application.getApplicationContext();
    this.preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
    this.owner = Mockito.mock(LifecycleOwner.class);
    this.registry = new LifecycleRegistry(owner);
    Mockito.when(owner.getLifecycle()).thenReturn(this.registry);
  }

  @Test
  public void getSettings_defaultValue() throws ExecutionException, InterruptedException {
    SettingsModel settingsModel = new SettingsModel(preferences);
    Utils.observe(settingsModel.getSettings(), this.owner, Mockito.mock(Observer.class));

    Settings settings = settingsModel.getSettings().getValue();
    Assert.assertNotNull(settings);
    Assert.assertEquals(Country.USA, settings.getCountry());
    Assert.assertEquals(SiteInfo.CUBIC_FOOT, settings.getVolumeUnit());
    Assert.assertEquals(NonSI.FAHRENHEIT, settings.getTemperatureUnit());
  }

  @Test
  public void getSettings_upToDate() throws ExecutionException, InterruptedException {
    final SettingsModel settingsModel = new SettingsModel(preferences);

    liveSettings = settingsModel.getSettings();
    Settings settings = new Settings(SI.CUBIC_METRE, SI.CELSIUS, Country.AUS);
    settingsModel.updateSettings(settings);
    Utils.observe(liveSettings, this.owner, Mockito.mock(Observer.class));

    Assert.assertEquals(settings, liveSettings.getValue());
  }

  @Test
  public void getSettings_hasInitialValue() {
    SettingsModel settingsModel = new SettingsModel(preferences);
    Assert.assertNotNull(settingsModel.getSettings().getValue());
  }

  @Test
  public void setCountry_upToDate() throws ExecutionException, InterruptedException {
    SettingsModel settingsModel = new SettingsModel(preferences);
    liveSettings = settingsModel.getSettings();

    settingsModel.selectCountry(Country.AUS);
    Utils.observe(liveSettings, this.owner, Mockito.mock(Observer.class));

    Assert.assertEquals(Country.AUS, liveSettings.getValue().getCountry());
  }

  @Test
  public void setImperial_setFalse() throws ExecutionException, InterruptedException {
    SettingsModel settingsModel = new SettingsModel(preferences);
    liveSettings = settingsModel.getSettings();

    settingsModel.setImperial(false);
    Utils.observe(liveSettings, this.owner, Mockito.mock(Observer.class));

    Assert.assertEquals(SI.CUBIC_METRE, liveSettings.getValue().getVolumeUnit());
  }

  @Test
  public void setImperial_setTrue() throws ExecutionException, InterruptedException {
    SettingsModel settingsModel = new SettingsModel(preferences);
    preferences.edit()
        .putBoolean("imperial", false)
        .apply();
    liveSettings = settingsModel.getSettings();

    settingsModel.setImperial(true);
    Utils.observe(liveSettings, this.owner, Mockito.mock(Observer.class));

    Assert.assertEquals(SiteInfo.CUBIC_FOOT, liveSettings.getValue().getVolumeUnit());
  }
}
