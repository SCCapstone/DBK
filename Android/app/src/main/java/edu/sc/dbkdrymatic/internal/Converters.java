package edu.sc.dbkdrymatic.internal;

import android.arch.persistence.room.TypeConverter;

import org.jscience.physics.amount.Amount;

import javax.measure.quantity.Temperature;
import javax.measure.quantity.Volume;

public class Converters {
  @TypeConverter
  public static String stringFromVolume(Amount<Volume> value) {
    return value.toString();
  }

  @TypeConverter
  public static Amount<Volume> volumeFromString(String string) {
    return (Amount<Volume>) Amount.valueOf(string);
  }

  @TypeConverter
  public static String stringFromTemperature(Amount<Temperature> value) {
    return value.toString();
  }

  @TypeConverter
  public static Amount<Temperature> temperatureFromString(String string) {
    return (Amount<Temperature>) Amount.valueOf(string);
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
