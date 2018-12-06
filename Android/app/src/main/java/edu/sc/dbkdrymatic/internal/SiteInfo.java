package edu.sc.dbkdrymatic.internal;

import org.jscience.physics.amount.Amount;

import javax.measure.quantity.Energy;
import javax.measure.quantity.Power;
import javax.measure.quantity.Temperature;
import javax.measure.quantity.Volume;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

public class SiteInfo {
  public static final Unit<Volume> CUBIC_FOOT = NonSI.LITER.divide(28.31685);
  public static final Unit<Energy> BTU = SI.JOULE.divide(1055.056);

  public Amount<Volume> volume;
  public Amount<Temperature> insideTemp;
  public Amount<Temperature> desiredTemp;
  public Amount<Temperature> outsideTemp;
  public double relativeHumidity;
  public Damage waterLoss;
  public Country country;

  public SiteInfo(Amount<Volume> volume, Amount<Temperature> insideTemp,
                  Amount<Temperature> desiredTemp, Amount<Temperature> outsideTemp,
                  double relativeHumidity, Damage waterLoss, Country country) {
    this.volume = volume;
    this.outsideTemp = outsideTemp;
    this.desiredTemp = desiredTemp;
    this.insideTemp = insideTemp;
    this.relativeHumidity = relativeHumidity;
    this.waterLoss = waterLoss;
    this.country = country;
  }

  public Amount<Energy> getAdjustedEnergy() {
    double energyBtu = this.getEnergy().doubleValue(BTU);
    double value = 0.0;
    switch(this.country) {
      case USA:
        value = energyBtu - 6000 - (getD2Requirement() * 4700);
        break;
      case UK:
      case AUS:  // fallthrough
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
}
