package edu.sc.dbkdrymatic.internal;

import java.util.HashSet;

import static javax.measure.unit.NonSI.LITER;
import static javax.measure.unit.SI.KELVIN;

import org.jscience.physics.amount.Amount;

import javax.measure.quantity.Volume;
import javax.measure.unit.Unit;


public class JobFactory {

  public Job emptyJob() {
    Unit<Volume> cubicFoot = LITER.divide(28.31685);
    SiteInfo siteInfo = new SiteInfo(
        Amount.valueOf(0.0, cubicFoot),
        Amount.valueOf(290, KELVIN),
        Amount.valueOf(290, KELVIN),
        Amount.valueOf(290, KELVIN),
        50,
        Damage.CLASS1
    );
    return new Job(new HashSet<BoostBox>(), siteInfo);
  }
}
