package edu.sc.dbkdrymatic.internal;

import javax.measure.quantity.Temperature;
import javax.measure.quantity.Volume;
import javax.measure.unit.Unit;

public class Settings {

  public Settings(Unit<Volume> volumeUnit, Unit<Temperature> temperatureUnit) {
    this.volumeUnit = volumeUnit;
    this.temperatureUnit = temperatureUnit;
  }

  private Unit<Volume> volumeUnit;
  private Unit<Temperature> temperatureUnit;

  public Unit<Volume> getVolumeUnit() {
    return volumeUnit;
  }

  public Unit<Temperature> getTemperatureUnit() {
    return temperatureUnit;
  }
}
