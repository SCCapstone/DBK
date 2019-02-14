package edu.sc.dbkdrymatic.internal;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import org.jscience.physics.amount.Amount;

import javax.measure.quantity.Energy;
import javax.measure.quantity.Power;
import javax.measure.quantity.Temperature;
import javax.measure.quantity.Volume;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import edu.sc.dbkdrymatic.internal.database.Converters;

@Entity
@TypeConverters(Converters.class)
public class SiteInfo {
  public static final Unit<Volume> CUBIC_FOOT = NonSI.LITER.times(28.31685);
  public static final Unit<Energy> BTU = SI.JOULE.times(1055.056);

  @NonNull
  @PrimaryKey(autoGenerate = true)
  private int id;

  @NonNull
  public String name;

  @ColumnInfo(name = "volume")
  public Amount<Volume> volume;

  @ColumnInfo(name = "inside_temp")
  public Amount<Temperature> insideTemp;

  @ColumnInfo(name = "desired_temp")
  public Amount<Temperature> desiredTemp;

  @ColumnInfo(name = "outside_temp")
  public Amount<Temperature> outsideTemp;

  @ColumnInfo(name = "relative_humidity")
  public double relativeHumidity;

  @ColumnInfo(name = "water_loss")
  public Damage waterLoss;

  @ColumnInfo(name = "country")
  public Country country;

  public SiteInfo(Amount<Volume> volume, Amount<Temperature> insideTemp,
                  Amount<Temperature> desiredTemp, Amount<Temperature> outsideTemp,
                  double relativeHumidity, Damage waterLoss, Country country, String name) {
    this.volume = volume;
    this.outsideTemp = outsideTemp;
    this.desiredTemp = desiredTemp;
    this.insideTemp = insideTemp;
    this.relativeHumidity = relativeHumidity;
    this.waterLoss = waterLoss;
    this.country = country;
    this.name = name;
  }

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Amount<Energy> getAdjustedEnergy() {
    double energyBtu = this.getEnergy().doubleValue(BTU);
    double value = 0.0;
    switch(this.country) {
      case USA:
        value = energyBtu - 6000 - (getD2Requirement() * 4700);
        break;
      case UK:
      case AUS:  // fallthrough as both use the same formula
        value = energyBtu - 6000 - (getD2Requirement() * 9400);
        break;
    }

    return Amount.valueOf(value, BTU);
  }

  public Amount<Energy> getEnergy() {
    Amount<Temperature> desiredOutsideTempDiff = desiredTemp.minus(outsideTemp);
    Amount<Temperature> desiredInsideTempDiff = desiredTemp.minus(insideTemp);
    double energyValue =
          (0.008568 * volume.doubleValue(CUBIC_FOOT) *
              desiredOutsideTempDiff.doubleValue(NonSI.FAHRENHEIT))
        + (volume.doubleValue(CUBIC_FOOT) * waterLoss.getValue() * 12.6)
        + (volume.doubleValue(CUBIC_FOOT) * waterLoss.getValue() *
              desiredInsideTempDiff.doubleValue(NonSI.FAHRENHEIT) * 0.012)
        + (volume.doubleValue(CUBIC_FOOT) *
              desiredInsideTempDiff.doubleValue(NonSI.FAHRENHEIT) * 0.055);
    return Amount.valueOf(energyValue, BTU);
  }

  public Amount<Power> getAdjustedPower() {
    return Amount.valueOf(
        this.getAdjustedEnergy().doubleValue(BTU) / 3400.0, SI.KILO(SI.WATT));
  }

  public Amount<Power> getPower() {
    return Amount.valueOf(this.getEnergy().doubleValue(BTU) / 3400.0, SI.KILO(SI.WATT));
  }

  public double getD2Requirement() {
    return volume.doubleValue(CUBIC_FOOT) / 6000.0;
  }

  public double getBoostBoxRequirement() {
    return this.getAdjustedPower().doubleValue(SI.KILO(SI.WATT)) / country.getKilowattRating();
  }

  @Override
  public boolean equals(Object that) {
    if (that.getClass() != this.getClass()) return false;
    SiteInfo other = (SiteInfo) that;
    return other.volume.compareTo(volume) == 0 &&
        other.outsideTemp.compareTo(outsideTemp) == 0 &&
        other.desiredTemp.compareTo(desiredTemp) == 0 &&
        other.insideTemp.compareTo(insideTemp) == 0 &&
        Math.abs(other.relativeHumidity - relativeHumidity) < 0.0001 &&
        other.waterLoss == waterLoss &&
        other.country == country &&
        other.name.equals(name);
  }
}
