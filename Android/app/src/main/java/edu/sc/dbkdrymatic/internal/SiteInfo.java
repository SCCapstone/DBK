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

  public SiteInfo(Amount<Volume> volume, Amount<Temperature> insideTemp,
                  Amount<Temperature> desiredTemp, Amount<Temperature> outsideTemp,
                  double relativeHumidity, Damage waterLoss) {
    this.volume = volume;
    this.outsideTemp = outsideTemp;
    this.desiredTemp = desiredTemp;
    this.insideTemp = insideTemp;
    this.relativeHumidity = relativeHumidity;
    this.waterLoss = waterLoss;
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

  public Amount<Power> getPower() {
    return Amount.valueOf(this.getEnergy().doubleValue(BTU) / 3400.0, SI.KILO(SI.WATT));
  }

  public double getD2Requirement() {
    return volume.doubleValue(CUBIC_FOOT) / 6000.0;
  }

  public double getBoostBoxRequirement() {
    return this.getPower().doubleValue(SI.KILO(SI.WATT)) / 1.4;
  }
}
