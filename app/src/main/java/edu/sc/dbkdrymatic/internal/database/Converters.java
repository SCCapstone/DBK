package edu.sc.dbkdrymatic.internal.database;

import androidx.room.TypeConverter;

import org.jscience.physics.amount.Amount;

import javax.measure.quantity.Duration;
import javax.measure.quantity.ElectricCurrent;
import javax.measure.quantity.ElectricPotential;
import javax.measure.quantity.Energy;
import javax.measure.quantity.Power;
import javax.measure.quantity.Temperature;
import javax.measure.quantity.Volume;
import javax.measure.unit.SI;

import edu.sc.dbkdrymatic.internal.Country;
import edu.sc.dbkdrymatic.internal.Damage;

/**
 * Collection of converters between the unit-independent types used by this application and
 * serializable types suitable for storage in the Database.
 */
public class Converters {
  @TypeConverter
  public static double voltageToSi(Amount<ElectricPotential> value) {
    return value.doubleValue(SI.VOLT);
  }

  @TypeConverter
  public static Amount<ElectricPotential> voltageFromSi(double value) {
    return Amount.valueOf(value, SI.VOLT);
  }

  @TypeConverter
  public static double currentToSi(Amount<ElectricCurrent> value) {
    return value.doubleValue(SI.AMPERE);
  }

  @TypeConverter
  public static Amount<ElectricCurrent> currentFromSi(double value) {
    return Amount.valueOf(value, SI.AMPERE);
  }

  @TypeConverter
  public static double powerToSi(Amount<Power> value) {
    return value.doubleValue(SI.WATT);
  }

  @TypeConverter
  public static Amount<Power> powerFromSi(double value) {
    return Amount.valueOf(value, SI.WATT);
  }

  @TypeConverter
  public static double energyToSi(Amount<Energy> value) {
    return value.doubleValue(SI.JOULE);
  }

  @TypeConverter
  public static Amount<Energy> energyFromSi(double value) {
    return Amount.valueOf(value, SI.JOULE);
  }

  @TypeConverter
  public static double volumeToSi(Amount<Volume> value) {
    return value.doubleValue(SI.CUBIC_METRE);
  }

  @TypeConverter
  public static Amount<Volume> volumeFromSi(double value) {
    return Amount.valueOf(value, SI.CUBIC_METRE);
  }

  @TypeConverter
  public static double tempToSi(Amount<Temperature> value) {
    return value.doubleValue(SI.KELVIN);
  }

  @TypeConverter
  public static Amount<Temperature> tempFromSi(double value) {
    return Amount.valueOf(value, SI.KELVIN);
  }

  @TypeConverter
  public static double timeToSi(Amount<Duration> value) {
    return value.doubleValue(SI.SECOND);
  }

  @TypeConverter
  public static Amount<Duration> timeFromSi(double value) {
    return Amount.valueOf(value, SI.SECOND);
  }

  @TypeConverter
  public static String damageToString(Damage damage) {
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
  public static String countryToString(Country country) {
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
