package edu.sc.dbkdrymatic.internal.database;

import android.arch.persistence.room.Room;

import junit.framework.Assert;

import org.jscience.physics.amount.Amount;
import org.junit.Test;

import java.util.List;

import javax.measure.unit.NonSI;

import edu.sc.dbkdrymatic.internal.Country;
import edu.sc.dbkdrymatic.internal.Damage;
import edu.sc.dbkdrymatic.internal.SiteInfo;

public class AppDatabaseTest {

  @Test
  public void storageAndRetrieval() {
    AppDatabase db = Room.inMemoryDatabaseBuilder(
        null, AppDatabase.class).allowMainThreadQueries().build();
    SiteInfo si = new SiteInfo(
        Amount.valueOf(12000, SiteInfo.CUBIC_FOOT),
        Amount.valueOf(80, NonSI.FAHRENHEIT),
        Amount.valueOf(105, NonSI.FAHRENHEIT),
        Amount.valueOf(60, NonSI.FAHRENHEIT),
        80,
        Damage.CLASS2,
        Country.USA,
        "Default Site"
    );
    db.siteInfoDao().insertAll(si);

    List<SiteInfo> all = db.siteInfoDao().getAll().getValue();
    Assert.assertEquals(1, all.size());
    Assert.assertEquals(si, all.get(0));
  }
}
