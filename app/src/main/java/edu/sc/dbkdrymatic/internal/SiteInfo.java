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

/**
 * Object representing the properties of a site including identifying information and measurements
 * taken at the start of the job.
 *
 * This information is used to compute the values to be displayed by the calculator and some others.
 * Namely: energy requirement, power requirement, D2 requirement, Boost Box requirement, and
 * other requirements as needed.
 *
 * Two {@code SiteInfo}s are considered to be equal if and only if all of their fields are equal.
 * In practice, one may assume two are equal if they share a primary key and were instantiated from
 * a database query.
 */
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

  /**
   * This function is not to be used externally. The database requires this method in order to
   * generate the primary key.
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Gives the energy requirement of the job from initial conditions, adjusted for job locale.
   */
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

  /**
   * Gives the theoretical energy requirement of the job, without adjusting for the difference
   * in energy depending on configuration of the local energy grid. In practice, this will introduce
   * losses and as such, {@code getAdjustedEnergy()} is preferred.
   */
  public Amount<Energy> getEnergy() {
    // Do not use the `org.jscience.amount` mathematical operations.
    // For some reason they introduce math errors. Convert to doubles and then do math
    // on those values.
    double desiredOutsideTempDiff = desiredTemp.doubleValue(NonSI.FAHRENHEIT);
    desiredOutsideTempDiff -= outsideTemp.doubleValue(NonSI.FAHRENHEIT);

    double desiredInsideTempDiff = desiredTemp.doubleValue(NonSI.FAHRENHEIT);
    desiredInsideTempDiff -= insideTemp.doubleValue(NonSI.FAHRENHEIT);

    // This formula is lifted from the spreadsheet provided by DBK. Numeric coefficients
    // have been simplified to a single value per term in the sum, but no other changes have been
    // made.
    double energyValue =
          (volume.doubleValue(CUBIC_FOOT) * desiredOutsideTempDiff * 0.008568)
        + (volume.doubleValue(CUBIC_FOOT) * waterLoss.getValue() * 12.6)
        + (volume.doubleValue(CUBIC_FOOT) * desiredInsideTempDiff * waterLoss.getValue() * 0.012)
        + (volume.doubleValue(CUBIC_FOOT) * desiredInsideTempDiff * 0.055);
    return Amount.valueOf(energyValue, BTU);
  }

  /**
   * Computes the power requirement at the outlet to complete the job in the optimal amount of time
   * based on the characteristics of the local electrical grid.
   */
  public Amount<Power> getAdjustedPower() {
    return Amount.valueOf(
        this.getAdjustedEnergy().doubleValue(BTU) / 3400.0, SI.KILO(SI.WATT));
  }

  /**
   * Computes the theoretical power requirement to complete the job, disregarding losses due to
   * electrical grid configuration.
   */
  public Amount<Power> getPower() {
    return Amount.valueOf(this.getEnergy().doubleValue(BTU) / 3400.0, SI.KILO(SI.WATT));
  }

  /**
   * Computes the number of D2 air moving units that should be installed at the job site for optimal
   * job completion time to complement the Boost Boxes.
   */
  public double getD2Requirement() {
    return volume.doubleValue(CUBIC_FOOT) / 6000.0;
  }

  /**
   * Computes the number of Boost Box heating units that should be installed at the job site for
   * optimal job completion time, given the parameters of the particular site.
   */
  public double getBoostBoxRequirement() {
    return this.getAdjustedPower().doubleValue(SI.KILO(SI.WATT)) / country.getKilowattRating();
  }

  /**
   * Compares a {@code SiteInfo} to another object. If that object is not a {@code SiteInfo}, or
   * if the other object is a {@code SiteInfo} differing by more than 0.0001 in any floating point
   * field or having any disagreement in discrete fields, we return false. Otherwise, return true.
   */
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
