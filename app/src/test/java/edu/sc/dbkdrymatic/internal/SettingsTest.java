package edu.sc.dbkdrymatic.internal;

import org.junit.Assert;
import org.junit.Test;

import javax.measure.unit.SI;

public class SettingsTest {
  @Test
  public void equals_trulyEqual() {
    Settings s1 = new Settings(SI.CUBIC_METRE, SI.CELSIUS, Country.USA);
    Settings s2 = new Settings(SI.CUBIC_METRE, SI.CELSIUS, Country.USA);

    Assert.assertEquals(s1, s2);
  }

  @Test
  public void equals_differentCountry() {
    Settings s1 = new Settings(SI.CUBIC_METRE, SI.CELSIUS, Country.USA);
    Settings s2 = new Settings(SI.CUBIC_METRE, SI.CELSIUS, Country.UK);

    Assert.assertNotEquals(s1, s2);
  }

  @Test
  public void equals_differentVolumeUnit() {
    Settings s1 = new Settings(SiteInfo.CUBIC_FOOT, SI.CELSIUS, Country.USA);
    Settings s2 = new Settings(SI.CUBIC_METRE, SI.CELSIUS, Country.USA);

    Assert.assertNotEquals(s1, s2);
  }

  @Test
  public void equals_differentTemperatureUnit() {
    Settings s1 = new Settings(SI.CUBIC_METRE, SI.KELVIN, Country.USA);
    Settings s2 = new Settings(SI.CUBIC_METRE, SI.CELSIUS, Country.USA);

    Assert.assertNotEquals(s1, s2);
  }
}
