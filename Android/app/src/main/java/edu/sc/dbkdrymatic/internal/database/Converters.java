package edu.sc.dbkdrymatic.internal;

import android.arch.persistence.room.TypeConverter;

import org.jscience.physics.amount.Amount;

import javax.measure.quantity.Temperature;
import javax.measure.quantity.Volume;
import javax.measure.unit.SI;

public class Converters {
  @TypeConverter
  public static double volumeToSI(Amount<Volume> value) {
    return value.doubleValue(SI.CUBIC_METRE);
  }

  @TypeConverter
  public static Amount<Volume> volumeFromSI(double value) {
    return Amount.valueOf(value, SI.CUBIC_METRE);
  }

  @TypeConverter
  public static double tempToSI(Amount<Temperature> value) {
    return value.doubleValue(SI.KELVIN);
  }

  @TypeConverter
  public static Amount<Temperature> temperatureFromSI(double value) {
    return Amount.valueOf(value, SI.KELVIN);
  }

  @TypeConverter
  public static String stringFromDamage(Damage damage) {
    return damage.toString();
  }

  @TypeConverter
  public static Damage damageFromString(String label) {
    for (Damage damage: Damage.values()) {
      if(damage.toString().equals(label)) {
        return damage;
      }
    }
    return Damage.valueOf(label);
  }

  @TypeConverter
  public static String stringFromCountry(Country country) {
    return country.toString();
  }

  @TypeConverter
  public static Country countryFromString(String label) {
    for(Country country: Country.values()) {
      if (country.toString().equals(label)) {
        return country;
      }
    }
    return Country.valueOf(label);
  }
}
