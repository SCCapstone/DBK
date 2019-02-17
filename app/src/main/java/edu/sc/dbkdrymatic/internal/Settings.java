package edu.sc.dbkdrymatic.internal;

import javax.measure.quantity.Temperature;
import javax.measure.quantity.Volume;
import javax.measure.unit.Unit;

public class Settings {

  private Unit<Volume> volumeUnit;
  private Unit<Temperature> temperatureUnit;
  private Country country;

  public Settings(Unit<Volume> volumeUnit, Unit<Temperature> temperatureUnit, Country country) {
    this.volumeUnit = volumeUnit;
    this.temperatureUnit = temperatureUnit;
    this.country = country;
  }

  public Unit<Volume> getVolumeUnit() {
    return this.volumeUnit;
  }
  public void setVolumeUnit(Unit<Volume> volumeUnit) {
    this.volumeUnit = volumeUnit;
  }

  public Unit<Temperature> getTemperatureUnit() {
    return this.temperatureUnit;
  }
  public void setTemperatureUnit(Unit<Temperature> temperatureUnit) {
    this.temperatureUnit = temperatureUnit;
  }

  public Country getCountry() {
    return this.country;
  }
  public void setCountry(Country country) {
    this.country = country;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj.getClass() != this.getClass()) {
      return false;
    }
    Settings that = (Settings) obj;

    return that.getCountry() == this.getCountry() &&
        that.getVolumeUnit() == this.getVolumeUnit() &&
        that.getTemperatureUnit() == this.getTemperatureUnit();
  }
}
