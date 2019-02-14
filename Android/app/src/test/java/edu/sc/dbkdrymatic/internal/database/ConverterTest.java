package edu.sc.dbkdrymatic.internal.database;

import org.jscience.physics.amount.Amount;
import org.junit.Assert;
import org.junit.Test;

import javax.measure.quantity.Duration;
import javax.measure.quantity.ElectricCurrent;
import javax.measure.quantity.ElectricPotential;
import javax.measure.quantity.Energy;
import javax.measure.quantity.Power;
import javax.measure.quantity.Temperature;
import javax.measure.quantity.Volume;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;

import edu.sc.dbkdrymatic.internal.Country;
import edu.sc.dbkdrymatic.internal.Damage;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ConverterTest {
  public static final double delta = 0.0001;

  @Test
  public void voltageIsSound() {
    Amount<ElectricPotential> volt = Amount.valueOf(1.0, SI.VOLT);
    Assert.assertEquals(
        volt.doubleValue(SI.VOLT),
        Converters.voltageFromSi(Converters.voltageToSi(volt)).doubleValue(SI.VOLT),
        delta
    );
  }

  @Test
  public void currentIsSound() {
    Amount<ElectricCurrent> amp = Amount.valueOf(1.0, SI.AMPERE);
    Assert.assertEquals(
        amp.doubleValue(SI.AMPERE),
        Converters.currentFromSi(Converters.currentToSi(amp)).doubleValue(SI.AMPERE),
        delta
    );
  }

  @Test
  public void powerIsSound() {
    Amount<Power> watt = Amount.valueOf(1.0, SI.WATT);
    Assert.assertEquals(
        watt.doubleValue(SI.WATT),
        Converters.powerFromSi(Converters.powerToSi(watt)).doubleValue(SI.WATT),
        delta
    );
  }

  @Test
  public void energyIsSound() {
    Amount<Energy> joule = Amount.valueOf(1.0, SI.JOULE);
    Assert.assertEquals(
        joule.doubleValue(SI.JOULE),
        Converters.energyFromSi(Converters.energyToSi(joule)).doubleValue(SI.JOULE),
        delta
    );

  }

  @Test
  public void volumeIsSound() {
    Amount<Volume> gallon = Amount.valueOf(1.0, NonSI.GALLON_LIQUID_US);
    Assert.assertEquals(
        gallon.doubleValue(SI.CUBIC_METRE),
        Converters.volumeFromSi(Converters.volumeToSi(gallon)).doubleValue(SI.CUBIC_METRE),
        delta
    );
  }

  @Test
  public void tempIsSound() {
    Amount<Temperature> boiling = Amount.valueOf(212, NonSI.FAHRENHEIT);
    Assert.assertEquals(
        boiling.doubleValue(SI.KELVIN),
        Converters.tempFromSi(Converters.tempToSi(boiling)).doubleValue(SI.KELVIN),
        delta
    );
  }

  @Test
  public void timeIsSound() {
    Amount<Duration> hour = Amount.valueOf(1.0, NonSI.HOUR);
    Assert.assertEquals(
        hour.doubleValue(SI.SECOND),
        Converters.timeFromSi(Converters.timeToSi(hour)).doubleValue(SI.SECOND),
        delta
    );
  }

  @Test
  public void damageIsSound() {
    for(Damage damage: Damage.values()) {
      Assert.assertEquals(
          damage,
          Converters.damageFromString(Converters.damageToString(damage))
      );
    }
  }

  @Test
  public void countryIsSound() {
    for(Country country: Country.values()) {
      Assert.assertEquals(
          country,
          Converters.countryFromString(Converters.countryToString(country))
      );
    }
  }
}