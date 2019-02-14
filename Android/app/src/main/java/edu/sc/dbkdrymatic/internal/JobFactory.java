package edu.sc.dbkdrymatic.internal;

import java.util.HashSet;
import java.util.List;

import static javax.measure.unit.NonSI.LITER;
import static javax.measure.unit.SI.KELVIN;

import org.jscience.physics.amount.Amount;

import javax.measure.quantity.Volume;
import javax.measure.unit.Unit;

import edu.sc.dbkdrymatic.internal.database.AppDatabase;


public class JobFactory {

  private AppDatabase appDatabase;

  public JobFactory(AppDatabase appDatabase) {
    this.appDatabase = appDatabase;
  }

  public Job emptyJob() {
    Unit<Volume> cubicFoot = LITER.divide(28.31685);
    List<SiteInfo> list = appDatabase.siteInfoDao().getAll().getValue();
    SiteInfo siteInfo = null;
    if (list.size() == 0) {
      siteInfo = new SiteInfo(
          Amount.valueOf(0.0, cubicFoot),
          Amount.valueOf(290, KELVIN),
          Amount.valueOf(290, KELVIN),
          Amount.valueOf(290, KELVIN),
          50,
          Damage.CLASS1,
          Country.USA,
          "Default Site"
      );
      appDatabase.siteInfoDao().insertAll(siteInfo);
    } else {
      siteInfo = list.get(0);
    }
    return new Job(new HashSet<BoostBox>(), siteInfo);
  }
}
